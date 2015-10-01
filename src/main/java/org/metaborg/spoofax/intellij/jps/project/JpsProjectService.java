package org.metaborg.spoofax.intellij.jps.project;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    private final List<SpoofaxJpsProject> projects = new ArrayList<>();
    @NotNull private final IResourceService resourceService;

    @Inject private JpsProjectService(@NotNull final IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    public SpoofaxJpsProject create(@NotNull final JpsModule module) {
        final FileObject location = resourceService.resolve(module.getContentRootsList().getUrls().get(0));
        final SpoofaxJpsProject project = new SpoofaxJpsProject(module, location);
        this.projects.add(project);
        return project;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public SpoofaxJpsProject get(@NotNull final FileObject resource) {
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
    public SpoofaxJpsProject get(@NotNull final JpsModule module) {
        for (SpoofaxJpsProject project : this.projects) {
            if (project.module().equals(module))
                return project;
        }
        return null;
    }

    private boolean isInContentRoot(@NotNull final JpsModule module, @NotNull final FileObject resource) {
        final JpsUrlList contentRootsList = module.getContentRootsList();
        for (String url : contentRootsList.getUrls()) {
            if (hasDescendant(url, resource))
                return true;
        }
        return false;
    }

    private boolean hasDescendant(@NotNull final String ancestor, @NotNull final FileObject descendant) {
        final FileObject contentRoot = this.resourceService.resolve(ancestor);
        final FileName lhs = contentRoot.getName();
        final FileName rhs = descendant.getName();
        return lhs.equals(rhs) || lhs.isDescendent(rhs);
    }

}
