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

package org.metaborg.intellij.projects;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.projects.IIdeaProjectService;
import org.metaborg.intellij.idea.projects.IdeaProject;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.meta.core.config.ILanguageSpecConfig;
import org.metaborg.meta.core.project.ILanguageSpec;
import org.metaborg.util.log.ILogger;

import com.google.inject.Inject;

/**
 * Project utility functions.
 */
public final class ProjectUtils {

    private final IProjectService projectService;
    @InjectLogger
    private ILogger logger;

    @Inject
    public ProjectUtils(final IProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Gets the compile dependencies of the module.
     *
     * @return The compile dependencies.
     */
    public Collection<LanguageIdentifier> getCompileDependencies(@Nullable final ILanguageSpec languageSpec) {
        @Nullable final ILanguageSpecConfig config = languageSpec.config();
        if (config == null) {
            this.logger.error("Got no configuration for language specification: {}", languageSpec);
            return Collections.emptyList();
        }
        return config.compileDeps();
    }


    /**
     * Gets the compile dependencies of the project.
     *
     * @param project The project.
     * @return The compile dependencies.
     */
    public Collection<LanguageIdentifier> getCompileDependencies(@Nullable final IdeaProject project) {
        @Nullable final IProject metaborgProject = this.projectService.get(project.location());
        if (metaborgProject == null) {
            this.logger.error("Can't get language specification for project: {}", project);
            return Collections.emptyList();
        }
        return metaborgProject.config().compileDeps();
    }

}
