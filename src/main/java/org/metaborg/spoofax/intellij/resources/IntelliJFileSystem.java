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

    /**
     * Initializes a new instance of the {@link IntelliJFileSystem} class.
     *
     * @param rootName          The root name.
     * @param parentLayer       The parent layer.
     * @param fileSystemOptions The file system options.
     */
    public IntelliJFileSystem(@NotNull final FileName rootName,
                              @NotNull final FileObject parentLayer,
                              @NotNull final FileSystemOptions fileSystemOptions) {
        super(rootName, parentLayer, fileSystemOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final FileObject createFile(@NotNull final AbstractFileName name) throws Exception {
        return new IntelliJFileObject(name, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void addCapabilities(@NotNull final Collection<Capability> collection) {
        collection.addAll(IntelliJResourceProvider.capabilities);
    }

}
