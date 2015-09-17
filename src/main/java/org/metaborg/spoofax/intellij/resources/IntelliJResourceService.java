package org.metaborg.spoofax.intellij.resources;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileSystemManager;
import org.metaborg.core.resource.ResourceService;

import javax.annotation.Nullable;
import org.apache.commons.vfs2.FileObject;

public class IntelliJResourceService extends ResourceService implements IIntelliJResourceService {

    @Inject
    public IntelliJResourceService(FileSystemManager fileSystemManager,
                                   @Named("ResourceClassLoader") ClassLoader classLoader) {
        super(fileSystemManager, classLoader);
    }

    @Override
    public FileObject resolve(VirtualFile resource) {
        return resolve("file://" + resource.getPath());
//        return resolve("idea://" + resource.getPath());
    }

    @Nullable
    @Override
    public VirtualFile unresolve(FileObject resource) {
        if (resource instanceof IntelliJFileObject) {
            final IntelliJFileObject intellijResource = (IntelliJFileObject)resource;
            final VirtualFile intellijFile = intellijResource.getIntelliJFile();
            if (intellijFile != null)
                return intellijFile;
        }

        return LocalFileSystem.getInstance().findFileByPath(resource.getName().getPath());
    }
}
