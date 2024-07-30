/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.projects;

// TODO: Move this to metaborg core?


import jakarta.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.Compound;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A project service that combines multiple project services.
 */
public final class CompoundProjectService implements IProjectService {

    @InjectLogger
    private ILogger logger;
    private final Set<IProjectService> services;

    @Inject
    public CompoundProjectService(@Compound final Set<IProjectService> services) {
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
        if (resource == null) {
          throw new NullPointerException();
        }

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
