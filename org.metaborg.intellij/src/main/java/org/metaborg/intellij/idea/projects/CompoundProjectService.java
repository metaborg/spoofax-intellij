/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.idea.projects;

// TODO: Move this to metaborg core?

import com.google.common.base.*;
import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

/**
 * A project service that combines multiple project services.
 */
public final class CompoundProjectService implements IProjectService {

    @InjectLogger
    private ILogger logger;
    private final Set<IProjectService> services;

    @Inject
    /* package private */ CompoundProjectService(@Compound final Set<IProjectService> services) {
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
        Preconditions.checkNotNull(resource);

        final List<IProject> projects = new ArrayList<>(1);
        for (final IProjectService service : this.services) {
            @Nullable final IProject project = service.get(resource);
            if (project != null)
                projects.add(project);
        }

        if (projects.size() > 1) {
            throw new MultipleServicesRespondedException(this.logger.format(
                    "Multiple project services provided a project for the resource {}.",
                    resource
            ));
        } else if (projects.size() == 1) {
            return projects.get(0);
        } else {
            return null;
        }
    }
}
