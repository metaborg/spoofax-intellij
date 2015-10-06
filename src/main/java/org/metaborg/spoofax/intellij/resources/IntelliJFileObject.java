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
     * Creates a new instance of the IntelliJFileObject class.
     *
     * @param name The name of the file.
     * @param fs   The file system.
     */
    public IntelliJFileObject(@NotNull AbstractFileName name, @NotNull AbstractFileSystem fs) {
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

    @Override
    protected final void doAttach() throws Exception {
        assert (!isAttached());
        assert (this.file == null);

        this.file = LocalFileSystem.getInstance().findFileByPath(this.getName().getPath());
    }

    @Override
    protected final void doDetach() throws Exception {
        assert (isAttached());
        assert (this.file != null);

        this.file = null;
    }

    @Override
    protected final void onChange() throws Exception {
        assert (isAttached());

        // Re-attach.
        doDetach();
        doAttach();
    }

    @Override
    protected final FileType doGetType() throws Exception {
        assert (isAttached());

        if (!file.exists())
            return FileType.IMAGINARY;
        else if (file.isDirectory())
            return FileType.FOLDER;
        else
            return FileType.FILE;
    }

    @Override
    protected final boolean doIsHidden() throws Exception {
        if (this.file != null)
            return this.file.is(VFileProperty.HIDDEN);
        else
            return new File(this.getName().getPath()).isHidden();
    }

    @Override
    protected final boolean doIsReadable() throws Exception {
        if (this.file != null)
            // There is no property for this?
            return true;
        else
            return new File(this.getName().getPath()).canRead();
    }

    @Override
    protected final boolean doIsWriteable() throws Exception {
        if (this.file != null)
            return this.file.isWritable();
        else
            return new File(this.getName().getPath()).canWrite();
    }

    protected final boolean doIsExecutable() throws Exception {
        if (this.file != null)
            // There is no property for this?
            return false;
        else
            return new File(this.getName().getPath()).canExecute();
    }

    @Override
    protected final long doGetLastModifiedTime() throws Exception {
        return super.doGetLastModifiedTime();
    }

    @Override
    protected final boolean doSetLastModifiedTime(final long modtime) throws Exception {
        return super.doSetLastModifiedTime(modtime);
    }

    @Override
    @NotNull
    protected final Map<String, Object> doGetAttributes() throws Exception {
        return super.doGetAttributes();
    }

    @Override
    protected final void doSetAttribute(@NotNull final String attrName, @Nullable final Object value) throws Exception {
        super.doSetAttribute(attrName, value);
    }

    @Override
    protected final void doRemoveAttribute(@NotNull final String attrName) throws Exception {
        super.doRemoveAttribute(attrName);
    }

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

    @Override
    protected final long doGetContentSize() throws Exception {
        assert (isAttached());

        return file.getLength();
    }

    @Override
    protected final InputStream doGetInputStream() throws Exception {
        assert (isAttached());

        return file.getInputStream();
    }

    @Override
    protected final OutputStream doGetOutputStream(final boolean append) throws Exception {
        assert (isAttached());

        // NOTE: Because the file system has the Capability.APPEND_CONTENT capability,
        // we need to support `append == true`.

        // FIXME: We're ignoring `append`.

        return file.getOutputStream(null);
    }

    @Override
    protected final RandomAccessContent doGetRandomAccessContent(@NotNull final RandomAccessMode mode) throws
            Exception {
        assert (isAttached());

        // FIXME: Implement this.
        return super.doGetRandomAccessContent(mode);
    }

    @Override
    protected final void doCreateFolder() throws Exception {
        assert (isAttached());

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.getParent().createChildDirectory(null, this.getName().getBaseName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected final void doRename(@NotNull final FileObject newfile) throws Exception {
        assert (isAttached());

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.rename(null, newfile.getName().getBaseName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected final void doDelete() throws Exception {
        assert (isAttached());

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                this.file.delete(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    @NotNull
    protected final Certificate[] doGetCertificates() throws Exception {
        return super.doGetCertificates();
    }
}