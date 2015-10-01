package org.metaborg.spoofax.intellij.resources;

import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.resource.IResourceService;

import javax.annotation.Nullable;

/**
 * IntelliJ resource service.
 */
public interface IIntelliJResourceService extends IResourceService {

    /**
     * Converts an IntelliJ resource into a VFS resource.
     *
     * @param resource The IntelliJ resource to convert.
     * @return The corresponding VFS resource.
     */
    @NotNull
    FileObject resolve(@NotNull VirtualFile resource);

    /**
     * Converts a VFS resource into an IntelliJ resource, if possible.
     *
     * @param resource The VFS resource to convert.
     * @return The corresponding IntelliJ resource,
     * or <code>null</code> if it could not be converted.
     */
    @Nullable
    VirtualFile unresolve(@NotNull FileObject resource);

}
