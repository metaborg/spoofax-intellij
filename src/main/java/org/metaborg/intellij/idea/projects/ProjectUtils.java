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


import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.projects.IdeaProject;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.meta.core.config.ILanguageSpecConfig;
import org.metaborg.meta.core.project.ILanguageSpec;
import org.metaborg.util.log.ILogger;

import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

/**
 * Project utility functions.
 */
public final class ProjectUtils {

    private final IProjectService projectService;
    @InjectLogger
    private ILogger logger;

    @jakarta.inject.Inject @javax.inject.Inject
    public ProjectUtils(final IProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Gets the compile dependencies of the module.
     *
     * @return The compile dependencies.
     */
    public Collection<LanguageIdentifier> getCompileDeps(@Nullable final ILanguageSpec languageSpec) {
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
    public Collection<LanguageIdentifier> getCompileDeps(@Nullable final IdeaProject project) {
        @Nullable final IProject metaborgProject = this.projectService.get(project.location());
        if (metaborgProject == null) {
            this.logger.error("Can't get language specification for project: {}", project);
            return Collections.emptyList();
        }
        return metaborgProject.config().compileDeps();
    }

}
