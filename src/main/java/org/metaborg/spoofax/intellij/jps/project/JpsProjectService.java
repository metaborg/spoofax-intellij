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
@Singleton
public final class JpsProjectService implements IJpsProjectService {

    private final List<SpoofaxJpsProject> projects = new ArrayList<>();
    private final IResourceService resourceService;

    @Inject private JpsProjectService(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

//    /**
//     * {@inheritDoc}
//     */
//    public void add(SpoofaxJpsProject project) {
//        Preconditions.checkNotNull(project);
//
//        this.projects.add(project);
//    }

    /**
     * {@inheritDoc}
     */
    public SpoofaxJpsProject create(JpsModule module) {
        FileObject location = resourceService.resolve(module.getContentRootsList().getUrls().get(0));
        SpoofaxJpsProject project = new SpoofaxJpsProject(module, location);
        this.projects.add(project);
        return project;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public SpoofaxJpsProject get(FileObject resource) {
        for (SpoofaxJpsProject project : this.projects) {
            JpsModule module = project.module();
            if (isInContentRoot(module, resource))
                return project;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    public SpoofaxJpsProject get(JpsModule module) {
        for (SpoofaxJpsProject project : this.projects) {
            if (project.module().equals(module))
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
