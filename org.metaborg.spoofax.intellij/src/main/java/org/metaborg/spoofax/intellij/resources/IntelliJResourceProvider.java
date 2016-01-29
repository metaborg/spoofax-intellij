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


import com.google.common.collect.ImmutableList;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;

import java.util.Collection;

/**
 * Provides the resources in the <code>idea://</code> VFS.
 */
public final class IntelliJResourceProvider extends AbstractOriginatingFileProvider {

    // @formatter:off
    public static final Collection<Capability> capabilities = ImmutableList.of(
            Capability.CREATE,
            Capability.DELETE,
            Capability.RENAME,
            Capability.GET_TYPE,
            Capability.GET_LAST_MODIFIED,
            Capability.SET_LAST_MODIFIED_FILE,
            Capability.SET_LAST_MODIFIED_FOLDER,
            Capability.LIST_CHILDREN,
            Capability.READ_CONTENT,
            Capability.URI,
            Capability.WRITE_CONTENT,
            Capability.APPEND_CONTENT,
            Capability.RANDOM_ACCESS_READ,
            Capability.RANDOM_ACCESS_WRITE
    );
    // @formatter:on

    /**
     * {@inheritDoc}
     */
    @Override
    public final Collection<Capability> getCapabilities() {
        return capabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final FileSystem doCreateFileSystem(
            final FileName rootName,
            final FileSystemOptions fileSystemOptions)
            throws FileSystemException {
        return new IntelliJFileSystem(rootName, null, fileSystemOptions);
    }

}