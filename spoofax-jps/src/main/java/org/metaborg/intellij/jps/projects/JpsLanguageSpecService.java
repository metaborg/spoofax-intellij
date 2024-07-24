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

package org.metaborg.intellij.jps.projects;


import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.config.ConfigException;
import org.metaborg.core.config.ConfigRequest;
import org.metaborg.core.messages.StreamMessagePrinter;
import org.metaborg.core.project.IProject;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.core.source.ISourceTextService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.spoofax.meta.core.config.ISpoofaxLanguageSpecConfig;
import org.metaborg.spoofax.meta.core.config.ISpoofaxLanguageSpecConfigService;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpec;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpecService;
import org.metaborg.util.log.ILogger;

import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A language specification service for JPS.
 * <p>
 * Due to how JPS works, we'll have one project service per JPS module.
 */
@Singleton
public final class JpsLanguageSpecService implements ISpoofaxLanguageSpecService {

    private final List<MetaborgJpsProject> projects = new ArrayList<>();
    private final ISourceTextService sourceTextService;
    private final ISpoofaxLanguageSpecConfigService configService;
    private final IResourceService resourceService;
    private final ISpoofaxLanguageSpecConfigService languageSpecConfigService;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link JpsLanguageSpecService} class.
     */
    @jakarta.inject.Inject
    public JpsLanguageSpecService(final ISourceTextService sourceTextService,
                                  final ISpoofaxLanguageSpecConfigService configService,
                                  final IResourceService resourceService,
                                  final ISpoofaxLanguageSpecConfigService languageSpecConfigService) {

        this.sourceTextService = sourceTextService;
        this.configService = configService;
        this.resourceService = resourceService;
        this.languageSpecConfigService = languageSpecConfigService;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean available(final IProject project) {

        if(project instanceof JpsLanguageSpec) {
            return true;
        }

        if(project instanceof MetaborgJpsProject) {
            return this.configService.available(project.location());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ISpoofaxLanguageSpec get(final IProject project)
            throws ConfigException {

        if(project instanceof JpsLanguageSpec) {
            return (JpsLanguageSpec) project;
        }

        if(!(project instanceof MetaborgJpsProject)) {
            this.logger.error("Project {} is not a JPS project, and can therefore not be converted " +
                    "to a language specification project", project);
            return null;
        }

        final MetaborgJpsProject ideaProject = (MetaborgJpsProject) project;

        final FileObject rootFolder = project.location();
        @Nullable final ConfigRequest<ISpoofaxLanguageSpecConfig> configRequest = this.configService.get(rootFolder);
        if(configRequest == null || !configRequest.valid()) {
            this.logger.error(
                    "Errors occurred when retrieving language specification configuration from project {}",
                    rootFolder);
            if (configRequest != null) {
                configRequest.reportErrors(new StreamMessagePrinter(this.sourceTextService, false, false, this.logger));
            }
            return null;
        }

        @Nullable final ISpoofaxLanguageSpecConfig config = configRequest.config();
        if(config == null) {
            // Configuration should never be null if it is available, but sanity check anyway.
            this.logger.error(
                    "Could not get the configuration of the project {}",
                    rootFolder);
            return null;
        }

        return new JpsLanguageSpec(ideaProject.getModule(), rootFolder, config);
    }
}
