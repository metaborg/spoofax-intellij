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

package org.metaborg.spoofax.intellij.project;

// TODO: Move this to metaborg core?

import com.google.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.StringFormatter;

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
     *                                  for the specified resource.
     */
    @Nullable
    @Override
    public IProject get(final FileObject resource) {
        List<IProject> projects = new ArrayList<>(1);
        for (IProjectService service : this.services) {
            IProject project = service.get(resource);
            if (project != null)
                projects.add(project);
        }

        if (projects.size() > 1) {
            throw new MetaborgRuntimeException(StringFormatter.format(
                    "Multiple project services provided a project for the resource {}.",
                    resource));
        } else if (projects.size() == 1) {
            return projects.get(0);
        } else {
            return null;
        }
    }
}
