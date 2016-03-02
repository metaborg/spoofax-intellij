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

package org.metaborg.intellij.jps.projects;

import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.project.*;
import org.metaborg.core.resource.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.spoofax.meta.core.config.*;
import org.metaborg.spoofax.meta.core.project.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

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
    @Inject
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

        if(project instanceof IdeaLanguageSpec) {
            return true;
        }

        if(project instanceof IdeaProject) {
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

        final SpoofaxLanguageSpecPaths paths = new SpoofaxLanguageSpecPaths(rootFolder, config);

        return new JpsLanguageSpec(ideaProject.getModule(), rootFolder, config, paths);
    }
}
