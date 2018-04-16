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

import com.google.inject.Inject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.provider.local.DefaultLocalFileProvider;
import org.metaborg.core.resource.*;

public final class IntelliJFileSystemManagerProvider extends DefaultFileSystemManagerProvider {

    /**
     * The IntelliJ scheme.
     */
    public static final String IntelliJScheme = "intellij";

    private final IIntelliJFileProviderFactory fileProviderFactory;

    /**
     * Initializes a new instance of the {@link IntelliJFileSystemManagerProvider} class.
     *
     * @param fileProviderFactory The file provider factory.
     */
    @Inject
    public IntelliJFileSystemManagerProvider(final IIntelliJFileProviderFactory fileProviderFactory) {
        this.fileProviderFactory = fileProviderFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setBaseFile(final DefaultFileSystemManager manager) throws FileSystemException {
        manager.setBaseFile(manager.resolveFile(IntelliJScheme + ":///"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addDefaultProvider(final DefaultFileSystemManager manager) throws FileSystemException {
        final IntelliJFileProvider fileProvider = this.fileProviderFactory.create();
        manager.addProvider(IntelliJScheme, fileProvider);
        manager.setDefaultProvider(fileProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addProviders(final DefaultFileSystemManager manager) throws FileSystemException {
        super.addProviders(manager);
        manager.addProvider("file", new DefaultLocalFileProvider());
    }

}
