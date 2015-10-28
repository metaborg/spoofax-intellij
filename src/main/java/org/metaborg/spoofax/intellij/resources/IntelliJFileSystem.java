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
