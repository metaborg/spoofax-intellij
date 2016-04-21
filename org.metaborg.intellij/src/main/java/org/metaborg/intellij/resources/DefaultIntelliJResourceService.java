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

package org.metaborg.intellij.resources;

import com.google.inject.*;
import com.google.inject.name.*;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.*;
import com.intellij.testFramework.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.resource.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.intellij.vfs.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.io.*;
import java.net.*;

/**
 * The default implementation of the {@link IIntelliJResourceService} interface.
 */
@Singleton
public final class DefaultIntelliJResourceService extends ResourceService implements IIntelliJResourceService {

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link DefaultIntelliJResourceService} class.
     */
    @Inject
    public DefaultIntelliJResourceService(
            final FileSystemManager fileSystemManager,
            @Named("ResourceClassLoader") final ClassLoader classLoader) {
        super(fileSystemManager, classLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public final FileObject resolve(final VirtualFile resource) {
        @Nullable VirtualFile file = resource;
        if (file instanceof LightVirtualFile) {
            file = ((LightVirtualFile)resource).getOriginalFile();
            if (file == null) {
                // Only in-memory (non-physical) files have no associated virtual file.
                return null;
            }
        }
        return resolve(IntelliJFileSystemManagerProvider.IntelliJScheme + "://" + resource.getPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public final FileObject resolve(final PsiFile file) {
        @Nullable final VirtualFile virtualFile = file.getOriginalFile().getVirtualFile();
        if(virtualFile == null) {
            // Only in-memory (non-physical) files have no associated virtual file.
            return null;
        }
        return resolve(virtualFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final VirtualFile unresolve(final FileObject resource) {
        if (resource instanceof IntelliJFileObject) {
            final IntelliJFileObject intellijResource = (IntelliJFileObject)resource;
            try {
                return intellijResource.asVirtualFile();
            } catch (final FileSystemException e) {
                throw LoggerUtils.exception(this.logger, UnhandledException.class,
                        "Unexpected exception while resolving file: {}", e, resource);
            }
        }

        final URI uri = toUri(resource.getName().getURI());
        @Nullable final VirtualFileSystem fileSystem = getFileSystem(uri);
        @Nullable final String path = getPath(uri);
        if (fileSystem == null || path == null) {
            throw LoggerUtils.exception(this.logger, IllegalStateException.class, "Can't unresolve this URI: {}", uri);
        }
        return fileSystem.refreshAndFindFileByPath(path);
    }

    /**
     * Returns the IntelliJ virtual file system for the specified URI.
     *
     * @param uri The URI.
     * @return The file system; or <code>null</code> if not found.
     */
    @Nullable
    private VirtualFileSystem getFileSystem(final URI uri) {
        switch (uri.getScheme()) {
            case "file":
                return StandardFileSystems.local();
            case "zip":
            case "jar":
                return StandardFileSystems.jar();
            default:
                return null;
        }
    }

    /**
     * Returns the path of the URI.
     *
     * @param uri The URI.
     * @return The path of the URI; or <code>null</code> when the URI contains no path.
     */
    @Nullable
    private String getPath(final URI uri) {
        if (uri.getPath() == null) {
            final String part = uri.getSchemeSpecificPart();
            return getPath(toUri(part));
        }
        return uri.getPath();
    }

    /**
     * Converts an URI string to an URI object.
     *
     * @param uri The URI string.
     * @return The URI object.
     */
    private URI toUri(final String uri) {
        try {
            return new URI(uri);
        } catch (final URISyntaxException e) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class, "An unexpected exception occurred.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public File localPath(final FileObject resource) {
        if(!(resource instanceof IntelliJFileObject)) {
            return super.localPath(resource);
        }

        @Nullable final VirtualFile virtualFile = unresolve(resource);
        if (virtualFile == null) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
                    "Couldn't unresolve the resource: {}", resource);
        }
        return new File(virtualFile.getPath());
    }
}
