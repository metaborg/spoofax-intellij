/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.vfs;

import com.intellij.openapi.application.*;
import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.*;
import org.metaborg.intellij.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

/**
 * An IntelliJ VFS file object.
 */
public final class IntelliJFileObject extends AbstractFileObject {

    /**
     * The attached file. This is null when the file is not attached or doesn't exist.
     */
    @Nullable private VirtualFile file = null;

    /**
     * Initializes a new instance of the {@link IntelliJFileObject} class.
     *
     * @param filename The file name.
     * @param fileSystem The file system.
     */
    public IntelliJFileObject(final AbstractFileName filename, final IntelliJFileSystem fileSystem) {
        super(filename, fileSystem);
    }

    /**
     * Attaches this file object to its underlying resource.
     */
    @Override
    protected void doAttach() throws Exception {
        assert !isAttached();
        assert this.file == null;

        // May return null when the file doesn't exist.
        this.file = LocalFileSystem.getInstance().refreshAndFindFileByPath(this.getName().getPath());
    }

    /**
     * Detaches this file object from its underlying resource.
     */
    @Override
    protected void doDetach() throws Exception {
        assert isAttached();

        this.file = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FileType doGetType() throws Exception {
        assert isAttached();

        if (this.file == null || !this.file.exists())
            return FileType.IMAGINARY;
        else if (this.file.isDirectory())
            return FileType.FOLDER;
        else
            return FileType.FILE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doIsHidden() throws Exception {
        assert isAttached();
        assert this.file != null;

        return this.file.is(VFileProperty.HIDDEN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doIsReadable() throws Exception {
        assert isAttached();
        assert this.file != null;

        // There is no property for this?
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doIsWriteable() throws Exception {
        assert isAttached();
        assert this.file != null;

        return this.file.isWritable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final String[] doListChildren() throws Exception {
        assert isAttached();
        assert this.file != null;

        return Arrays.stream(doListChildrenResolved())
                .map((c) -> c.getName().getBaseName())
                .toArray(String[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final FileObject[] doListChildrenResolved() throws Exception {
        assert isAttached();
        assert this.file != null;

        return Arrays.stream(this.file.getChildren())
                .map(this::fileSystemResolve)
                .toArray(FileObject[]::new);
    }

    /**
     * Resolves a virtual file to a file object,
     * and rethrows any exceptions as unchecked.
     *
     * @param file The IntelliJ file.
     * @return The Apache file.
     */
    private FileObject fileSystemResolve(final VirtualFile file) {
        try {
            return getFileSystem().resolveFile(file.getName());
        } catch (final FileSystemException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDelete() throws Exception {
        assert isAttached();
        assert this.file != null;

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.delete(null);
            } catch (final IOException e) {
                throw new UnhandledException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doRename(final FileObject newFile) throws Exception {
        assert isAttached();
        assert this.file != null;

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.rename(null, newFile.getName().getBaseName());
            } catch (final IOException e) {
                throw new UnhandledException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doCreateFolder() throws Exception {
        assert isAttached();
        assert this.file != null;

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.getParent().createChildDirectory(null, this.getName().getBaseName());
            } catch (final IOException e) {
                throw new UnhandledException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long doGetContentSize() throws Exception {
        assert isAttached();
        assert this.file != null;

        return this.file.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InputStream doGetInputStream() throws Exception {
        assert isAttached();
        assert this.file != null;

        return this.file.getInputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected OutputStream doGetOutputStream(final boolean append) throws Exception {
        assert isAttached();
        assert !append : "The file system doesn't have the Capability.APPEND_CONTENT capability.";

        ensureExists();

        assert this.file != null;

        return this.file.getOutputStream(null);
    }

    /**
     * Returns the associated IntelliJ virtual file.
     *
     * @return The associated {@link VirtualFile}.
     */
    public final VirtualFile asVirtualFile() throws FileSystemException {
        synchronized(this.getFileSystem()) {
            // HACK: This forces the file to be attached
            // if it wasn't already.
            getType();

            assert isAttached();
            assert this.file != null;

            return this.file;
        }
    }

    private void ensureExists()
            throws FileSystemException {
        if (this.file != null) return;

        try {
            new File(this.getName().getPath()).createNewFile();
        } catch (final IOException e) {
            throw new FileSystemException(e);
        }

        refresh();
        // HACK: This forces the file to be attached
        // if it wasn't already.
        getType();

        assert this.file != null;
    }
}
