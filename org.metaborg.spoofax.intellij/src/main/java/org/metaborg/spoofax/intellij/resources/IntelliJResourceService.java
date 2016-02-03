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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.resource.ResourceService;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;

@Singleton
public final class IntelliJResourceService extends ResourceService implements IIntelliJResourceService {

    @Inject
    /* package private */ IntelliJResourceService(
            final FileSystemManager fileSystemManager,
            @Named("ResourceClassLoader") final ClassLoader classLoader) {
        super(fileSystemManager, classLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final FileObject resolve(final VirtualFile resource) {
        return resolve("file://" + resource.getPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final VirtualFile unresolve(final FileObject resource) {
        if (resource instanceof IntelliJFileObject) {
            final IntelliJFileObject intellijResource = (IntelliJFileObject)resource;
            final VirtualFile intellijFile = intellijResource.getIntelliJFile();
            if (intellijFile != null)
                return intellijFile;
        }

        final URI uri = toUri(resource.getName().getURI());
        final VirtualFileSystem fileSystem = getFileSystem(uri);
        final String path = getPath(uri);
        if (fileSystem == null || path == null)
            throw new MetaborgRuntimeException("Can't unresolve this URI: " + uri);
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
                return StandardFileSystems.jar();
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
            if (part == null)
                return null;
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
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}
