/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.spoofax.intellij.resources;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VFileProperty;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.RandomAccessContent;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileObject;
import org.apache.commons.vfs2.provider.AbstractFileSystem;
import org.apache.commons.vfs2.util.RandomAccessMode;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.Map;

/**
 * A file object in the IntelliJ VFS.
 */
public final class IntelliJFileObject extends AbstractFileObject {

    // NOTE: The Eclipse implementation can use the `isAttached()` method instead of tracking `attached` itself.
    // I believe it can even do without `isAttached()`, as `doAttach` is never called for an attached resource,
    // and `doDetach()` is never called for an unattached resource.

    // NOTE: Eclipse: doIsHidden(), doIsReadable() and doIsWritable() are not necessarily called on attached files.

    /**
     * The IntelliJ VirtualFile object.
     */
    @Nullable
    private VirtualFile file = null;

    /**
     * Initializes a new instance of the {@link IntelliJFileObject} class.
     *
     * @param name The name of the file.
     * @param fs   The file system.
     */
    public IntelliJFileObject(@NotNull final AbstractFileName name, @NotNull final AbstractFileSystem fs) {
        super(name, fs);
    }

    /**
     * Returns the associated IntelliJ virtual file.
     *
     * @return The associated VirtualFile; or <code>null</code> when the file object wasn't attached.
     */
    @Nullable
    public final VirtualFile getIntelliJFile() {
        return this.file;
    }

    /**
     * Gets whether the file is executable.
     *
     * @return <code>true</code> when the file is executable;
     * otherwise, <code>false</code>.
     * @throws Exception
     */
    protected final boolean doIsExecutable() throws Exception {
        if (this.file != null)
            // There is no property for this?
            return false;
        else
            return new File(this.getName().getPath()).canExecute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void doAttach() throws Exception {
        assert (!isAttached());
        assert (this.file == null);

        this.file = LocalFileSystem.getInstance().findFileByPath(this.getName().getPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void doDetach() throws Exception {
        assert (isAttached());
        assert (this.file != null);

        this.file = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void onChange() throws Exception {
        assert (isAttached());

        // Re-attach.
        doDetach();
        doAttach();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final FileType doGetType() throws Exception {
        assert (isAttached());

        if (!this.file.exists())
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
    protected final boolean doIsHidden() throws Exception {
        if (this.file != null)
            return this.file.is(VFileProperty.HIDDEN);
        else
            return new File(this.getName().getPath()).isHidden();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final boolean doIsReadable() throws Exception {
        if (this.file != null)
            // There is no property for this?
            return true;
        else
            return new File(this.getName().getPath()).canRead();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final boolean doIsWriteable() throws Exception {
        if (this.file != null)
            return this.file.isWritable();
        else
            return new File(this.getName().getPath()).canWrite();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected final long doGetLastModifiedTime() throws Exception {
        return super.doGetLastModifiedTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final boolean doSetLastModifiedTime(final long modtime) throws Exception {
        return super.doSetLastModifiedTime(modtime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    protected final Map<String, Object> doGetAttributes() throws Exception {
        return super.doGetAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void doSetAttribute(@NotNull final String attrName, @Nullable final Object value) throws Exception {
        super.doSetAttribute(attrName, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void doRemoveAttribute(@NotNull final String attrName) throws Exception {
        super.doRemoveAttribute(attrName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    protected final String[] doListChildren() throws Exception {
        assert (isAttached());

        // This is essentially a complex way to write:
        //     return doListChildrenResolved().map(c => c.getName().getBaseName());
        final FileObject[] children = doListChildrenResolved();
        final String[] result = new String[children.length];
        for (int i = 0; i < children.length; i++) {
            result[i] = children[i].getName().getBaseName();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    protected final FileObject[] doListChildrenResolved() throws Exception {
        assert (isAttached());

        // This is essentially a complex way to write:
        //     return this.file.getChildren().map(c => getFileSystem().resolveFile(c.getName()));
        final VirtualFile[] children = this.file.getChildren();
        final FileObject[] result = new FileObject[children.length];
        for (int i = 0; i < children.length; i++) {
            result[i] = getFileSystem().resolveFile(children[i].getName());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final long doGetContentSize() throws Exception {
        assert (isAttached());

        return this.file.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final InputStream doGetInputStream() throws Exception {
        assert (isAttached());

        return this.file.getInputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final OutputStream doGetOutputStream(final boolean append) throws Exception {
        assert (isAttached());

        // NOTE: Because the file system has the Capability.APPEND_CONTENT capability,
        // we need to support `append == true`.

        // FIXME: We're ignoring `append`.

        return this.file.getOutputStream(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final RandomAccessContent doGetRandomAccessContent(@NotNull final RandomAccessMode mode) throws
            Exception {
        assert (isAttached());

        // FIXME: Implement this.
        return super.doGetRandomAccessContent(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void doCreateFolder() throws Exception {
        assert (isAttached());

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.getParent().createChildDirectory(null, this.getName().getBaseName());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void doRename(@NotNull final FileObject newfile) throws Exception {
        assert (isAttached());

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.rename(null, newfile.getName().getBaseName());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void doDelete() throws Exception {
        assert (isAttached());

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.delete(null);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    protected final Certificate[] doGetCertificates() throws Exception {
        return super.doGetCertificates();
    }
}