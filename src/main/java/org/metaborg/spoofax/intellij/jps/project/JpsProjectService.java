package org.metaborg.spoofax.intellij.jps.project;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.JpsUrlList;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.serialization.JpsModelSerializationDataService;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.resource.IResourceService;

import javax.annotation.Nullable;

/**
 * A project service for JPS.
 *
 * Due to how JPS works, we'll have one project service per JPS module.
 */
public final class JpsProjectService implements IProjectService {

    private final IResourceService resourceService;
    private final JpsModule module;

    @Inject private JpsProjectService(JpsModule module, IResourceService resourceService) {
        this.module = module;
        this.resourceService = resourceService;
    }

    @Nullable
    @Override
    public IProject get(FileObject resource) {
        if (!isInContentRoot(resource))
            return null;

        // FIXME: A module can have multiple content roots, or none at all.
        // Instead of trying to do everything relative to the project root,
        // we should use explicit directories for generated files and such.
        // Those would be configurable in the IDE, and can be found in the
        // content roots list as directories for generated files. Then we'd
        // simply pick the first, or error when there are none.
        String projectRoot = this.module.getContentRootsList().getUrls().get(0);
        return new SpoofaxJpsProject(this.resourceService.resolve(projectRoot));
    }

    private boolean isInContentRoot(FileObject resource) {
        JpsUrlList contentRootsList = this.module.getContentRootsList();
        for (String url : contentRootsList.getUrls()) {
            if (hasDescendant(url, resource))
                return true;
        }
        return false;
    }

    private boolean hasDescendant(String ancestor, FileObject descendant) {
        FileObject contentRoot = this.resourceService.resolve(ancestor);
        return contentRoot.getName().isDescendent(descendant.getName());
    }

}
