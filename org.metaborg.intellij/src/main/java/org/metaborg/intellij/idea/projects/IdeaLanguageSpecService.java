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

import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.project.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.spoofax.meta.core.config.*;
import org.metaborg.spoofax.meta.core.project.*;
import org.metaborg.util.log.*;

import javax.annotation.*;

/**
 * Language specification service implementation for IntelliJ IDEA.
 */
public final class IdeaLanguageSpecService implements ISpoofaxLanguageSpecService {

    private final IIdeaLanguageSpecFactory languageSpecFactory;
    private final ISourceTextService sourceTextService;
    private final ISpoofaxLanguageSpecConfigService configService;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link IdeaLanguageSpecService} class.
     */
    @Inject
    public IdeaLanguageSpecService(final ISourceTextService sourceTextService,
                                   final ISpoofaxLanguageSpecConfigService configService,
                                   final IIdeaLanguageSpecFactory languageSpecFactory) {

        this.sourceTextService = sourceTextService;
        this.configService = configService;
        this.languageSpecFactory = languageSpecFactory;
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

//    /**
//     * Gets a Spoofax language specification from the specified project.
//     *
//     * @param ideaModule
//     *            The IDEA module.
//     * @param rootFolder
//     *            The root folder.
//     * @return The Spoofax language specification, or <code>null</code> when the project is not a Spoofax language
//     *         specification.
//     * @throws ConfigException
//     *             When reading Spoofax language specification configuration fails.
//     */
//    @Nullable ISpoofaxLanguageSpec create(final Module ideaModule,
//                                          final FileObject rootFolder)
//            throws ConfigException {
//
//
//
//
//    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ISpoofaxLanguageSpec get(final IProject project)
            throws ConfigException {

        if(project instanceof IdeaLanguageSpec) {
            return (IdeaLanguageSpec) project;
        }

        if(!(project instanceof IdeaProject)) {
            this.logger.error("Project {} is not an IntelliJ IDEA project, and can therefore not be converted " +
                    "to a language specification project", project);
            return null;
        }

        final IdeaProject ideaProject = (IdeaProject) project;

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

        return this.languageSpecFactory.create(ideaProject.getModule(), rootFolder, config, paths);
    }
}
