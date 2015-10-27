package org.metaborg.spoofax.intellij.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.resource.ResourceService;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;

@Singleton
public final class IntelliJResourceService extends ResourceService implements IIntelliJResourceService {

    @Inject
    private IntelliJResourceService(@NotNull final FileSystemManager fileSystemManager,
                                    @NotNull @Named("ResourceClassLoader") final ClassLoader classLoader) {
        super(fileSystemManager, classLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final FileObject resolve(@NotNull final VirtualFile resource) {
        return resolve("file://" + resource.getPath());
//        return resolve("idea://" + resource.getPath());
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final VirtualFile unresolve(@NotNull final FileObject resource) {
        if (resource instanceof IntelliJFileObject) {
            final IntelliJFileObject intellijResource = (IntelliJFileObject) resource;
            final VirtualFile intellijFile = intellijResource.getIntelliJFile();
            if (intellijFile != null)
                return intellijFile;
        }

        URI uri = toUri(resource.getName().getURI());
        VirtualFileSystem fileSystem = getFileSystem(uri);
        String path = getPath(uri);
        if (fileSystem == null || path == null)
            throw new MetaborgRuntimeException("Can't unresolve this URI: " + uri);
        return fileSystem.findFileByPath(path);
//        return LocalFileSystem.getInstance().findFileByPath(resource.getName().getPath());
    }

    /**
     * Returns the IntelliJ virtual file system for the specified URI.
     *
     * @param uri The URI.
     * @return The file system; or <code>null</code> if not found.
     */
    @Nullable
    private VirtualFileSystem getFileSystem(@NotNull final URI uri) {
        switch (uri.getScheme()) {
            case "file": return StandardFileSystems.local();
            case "zip": return StandardFileSystems.jar();
            case "jar": return StandardFileSystems.jar();
            default: return null;
        }
    }

    /**
     * Returns the path of the URI.
     *
     * @param uri The URI.
     * @return The path of the URI; or <code>null</code> when the URI contains no path.
     */
    @Nullable
    private String getPath(@NotNull final URI uri) {
        if (uri.getPath() == null) {
            String part = uri.getSchemeSpecificPart();
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
    @NotNull
    private URI toUri(@NotNull final String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}
