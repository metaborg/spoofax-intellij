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
