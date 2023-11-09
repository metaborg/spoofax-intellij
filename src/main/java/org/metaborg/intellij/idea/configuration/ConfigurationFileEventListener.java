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

package org.metaborg.intellij.idea.configuration;


import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.idea.modules.SpoofaxModuleUtils;
import org.metaborg.intellij.idea.projects.IdeaProject;
import org.metaborg.intellij.idea.projects.IdeaProjectService;
import org.metaborg.intellij.idea.projects.MetaborgModuleType;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.intellij.idea.projects.ProjectUtils;
import org.metaborg.util.log.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Listens to events that occur for the project's configuration file.
 */
public final class ConfigurationFileEventListener extends VirtualFileAdapter {

//    private static final String CONFIG_FILE = "metaborg.yaml";
    private final MetaborgModuleType metaborgModuleType;
    private final IdeaProjectService projectService;
    private final ConfigurationUtils configurationUtils;
    private final ProjectUtils projectUtils;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link ConfigurationFileEventListener} class.
     */
    @jakarta.inject.Inject @javax.inject.Inject
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

        this.logger.info("Recognized change in {}!", SpoofaxModuleUtils.METABORG_CONFIG_FILENAME);
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
                throw LoggerUtils2.exception(this.logger, IllegalStateException.class,
                        "No associated Metaborg IProject found for Metaborg module: {}",
                        module);
            }

            metaborgProjects.add(metaborgProject);
        }

        return metaborgProjects;
    }

    private boolean respondsToFile(final VirtualFile file) {
        return file.getName().equals(SpoofaxModuleUtils.METABORG_CONFIG_FILENAME);
    }

}
