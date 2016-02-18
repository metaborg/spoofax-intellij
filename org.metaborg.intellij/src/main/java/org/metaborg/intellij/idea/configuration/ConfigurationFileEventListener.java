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

package org.metaborg.intellij.idea.configuration;

import com.google.inject.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.intellij.projects.*;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * Listens to events that occur for the project's configuration file.
 */
public final class ConfigurationFileEventListener extends VirtualFileAdapter {

    private static final String CONFIG_FILE = "metaborg.yaml";
    private final MetaborgModuleType metaborgModuleType;
    private final IdeaProjectService projectService;
    private final ConfigurationUtils configurationUtils;
    private final ProjectUtils projectUtils;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link ConfigurationFileEventListener} class.
     */
    @Inject
    public ConfigurationFileEventListener(final MetaborgModuleType metaborgModuleType,
                                          final IdeaProjectService projectService,
                                          final ConfigurationUtils configurationUtils,
                                          final ProjectUtils projectUtils) {
        this.metaborgModuleType = metaborgModuleType;
        this.projectService = projectService;
        this.configurationUtils = configurationUtils;
        this.projectUtils = projectUtils;
    }

    @Override
    public void contentsChanged(@NotNull final VirtualFileEvent event) {
        final Set<IdeaProject> projects = getProjectsForFile(event.getFile());
        if (projects.isEmpty()) return;

        if (!respondsToFile(event.getFile())) return;

        // TODO: Reload languages?

        this.logger.info("Recognized change in {}!", CONFIG_FILE);
    }

    /**
     * Gets all the projects that contain the specified file.
     *
     * @param file The file to look for.
     * @return The projects that contain the file;
     * or an empty set if no Metaborg projects contain the file.
     */
    private Set<IdeaProject> getProjectsForFile(final VirtualFile file) {
        final Set<IdeaProject> metaborgProjects = new HashSet<>();

        final Project[] allProjects = ProjectManager.getInstance().getOpenProjects();
        for (final Project project : allProjects) {

            @Nullable final Module module = ModuleUtil.findModuleForFile(file, project);
            if (module == null) {
                continue;
            }

            final ModuleType moduleType = ModuleType.get(module);
            if (moduleType != this.metaborgModuleType) {
                continue;
            }

            @Nullable final IdeaProject metaborgProject = this.projectService.get(module);
            if (metaborgProject == null) {
                throw LoggerUtils.exception(this.logger, IllegalStateException.class,
                        "No associated Metaborg IProject found for Metaborg module: {}",
                        module);
            }

            metaborgProjects.add(metaborgProject);
        }

        return metaborgProjects;
    }

    private boolean respondsToFile(final VirtualFile file) {
        return file.getName().equals(CONFIG_FILE);
    }

}
