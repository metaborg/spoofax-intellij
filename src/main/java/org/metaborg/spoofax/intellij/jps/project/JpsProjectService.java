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
import java.util.ArrayList;
import java.util.List;

/**
 * A project service for JPS.
 *
 * Due to how JPS works, we'll have one project service per JPS module.
 */
public final class JpsProjectService implements IProjectService {

    private final List<SpoofaxJpsProject> projects = new ArrayList<>();
    private final IResourceService resourceService;

    @Inject private JpsProjectService(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * Adds a project to this service.
     * @param project The project to add.
     */
    public void add(SpoofaxJpsProject project) {
        Preconditions.checkNotNull(project);

        this.projects.add(project);
    }

    @Nullable
    @Override
    public IProject get(FileObject resource) {
        for (SpoofaxJpsProject project : this.projects) {
            JpsModule module = project.module();
            if (isInContentRoot(module, resource))
                return project;
        }
        return null;
    }

    private boolean isInContentRoot(JpsModule module, FileObject resource) {
        JpsUrlList contentRootsList = module.getContentRootsList();
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
