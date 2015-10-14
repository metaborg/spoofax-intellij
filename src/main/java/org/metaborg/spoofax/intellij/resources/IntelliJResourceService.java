package org.metaborg.spoofax.intellij.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.resource.ResourceService;

import javax.annotation.Nullable;

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

        return LocalFileSystem.getInstance().findFileByPath(resource.getName().getPath());
    }
}
