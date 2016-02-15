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

import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.*;
import org.apache.commons.vfs2.provider.local.*;
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
