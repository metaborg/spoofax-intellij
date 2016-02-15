/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.vfs;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.*;

import java.util.*;

/**
 * The IntelliJ IDEA file system.
 */
public class IntelliJFileSystem extends AbstractFileSystem {

    /**
     * Initializes a new instance of the {@link IntelliJFileSystem} class.
     *
     * @param rootName          The root name.
     * @param fileSystemOptions The file system options.
     */
    public IntelliJFileSystem(final FileName rootName, final FileSystemOptions fileSystemOptions) {
        super(rootName, null, fileSystemOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FileObject createFile(final AbstractFileName name) throws Exception {
        return new IntelliJFileObject(name, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addCapabilities(final Collection<Capability> capabilities) {
        capabilities.addAll(IntelliJFileProvider.capabilities);
    }
}
