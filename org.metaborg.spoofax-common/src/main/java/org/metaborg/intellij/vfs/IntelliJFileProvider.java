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

import com.google.common.collect.ImmutableList;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;

import java.util.Collection;

/**
 * IntelliJ file system provider.
 */
public class IntelliJFileProvider extends AbstractOriginatingFileProvider {

    // @formatter:off
    public static final Collection<Capability> capabilities = ImmutableList.of(
            Capability.CREATE,
            Capability.DELETE,
            Capability.RENAME,
            Capability.GET_TYPE,
            Capability.LIST_CHILDREN,
            Capability.READ_CONTENT,
            Capability.URI,
            Capability.WRITE_CONTENT
    );
    // @formatter:on

    /**
     * Initializes a new instance of the {@link IntelliJFileProvider} class.
     */
    public IntelliJFileProvider() {

    }

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
    protected FileSystem doCreateFileSystem(final FileName rootName,
                                            final FileSystemOptions fileSystemOptions)
            throws FileSystemException {
        return new IntelliJFileSystem(rootName, fileSystemOptions);
    }
}
