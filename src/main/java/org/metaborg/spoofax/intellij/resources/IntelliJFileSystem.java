package org.metaborg.spoofax.intellij.resources;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The IntelliJ IDEA file system.
 */
public final class IntelliJFileSystem extends AbstractFileSystem {

    public IntelliJFileSystem(@NotNull final FileName rootName, @NotNull final FileObject parentLayer, @NotNull final FileSystemOptions fileSystemOptions) {
        super(rootName, parentLayer, fileSystemOptions);
    }

    @Override
    protected final FileObject createFile(@NotNull final AbstractFileName name) throws Exception {
        return new IntelliJFileObject(name, this);
    }

    @Override
    protected final void addCapabilities(@NotNull final Collection<Capability> collection) {
        collection.addAll(IntelliJResourceProvider.capabilities);
    }

}
