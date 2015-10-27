package org.metaborg.spoofax.intellij.project;

// TODO: Move this to metaborg core?

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.spoofax.intellij.StringFormatter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A project service that combines multiple project services.
 */
public final class CompoundProjectService implements IProjectService {

    @NotNull
    private final Set<IProjectService> services;

    @Inject
    private CompoundProjectService(@NotNull @Compound final Set<IProjectService> services) {
        this.services = services;
    }

    /**
     * {@inheritDoc}
     *
     * @throws MetaborgRuntimeException More than one project service provided a project
     * for the specified resource.
     */
    @Nullable
    @Override
    public IProject get(final FileObject resource) {
        List<IProject> projects = new ArrayList<>(1);
        for(IProjectService service : this.services) {
            IProject project = service.get(resource);
            if (project != null)
                projects.add(project);
        }

        if (projects.size() > 1) {
            throw new MetaborgRuntimeException(StringFormatter.format(
                    "Multiple project services provided a project for the resource {}.",
                    resource));
        }
        else if (projects.size() == 1) {
            return projects.get(0);
        } else {
            return null;
        }
    }
}
