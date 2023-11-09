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
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.model.JpsUrlList;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.StreamMessagePrinter;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.core.source.ISourceTextService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.ILogger;

import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A project service for JPS.
 * <p>
 * Due to how JPS works, we'll have one project service per JPS module.
 */
@Singleton
public final class JpsProjectService implements IJpsProjectService {

    private final List<MetaborgJpsProject> projects = new ArrayList<>();
    private final ISourceTextService sourceTextService;
    private final IResourceService resourceService;
    private final IProjectConfigService projectConfigService;
    @InjectLogger
    private ILogger logger;

    @jakarta.inject.Inject @javax.inject.Inject
    public JpsProjectService(final ISourceTextService sourceTextService,
                             final IResourceService resourceService,
                             final IProjectConfigService projectConfigService) {
        this.sourceTextService = sourceTextService;
        this.resourceService = resourceService;
        this.projectConfigService = projectConfigService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public MetaborgJpsProject create(final JpsModule module) {
        final FileObject rootFolder = this.resourceService.resolve(module.getContentRootsList().getUrls().get(0));
        final ConfigRequest<? extends IProjectConfig> configRequest = this.projectConfigService.get(rootFolder);
        if(!configRequest.valid()) {
            this.logger.error(
                    "An error occurred while retrieving the configuration for the project at {}", rootFolder);
            configRequest.reportErrors(new StreamMessagePrinter(this.sourceTextService, false, false, this.logger));
            return null;
        }

        @Nullable final IProjectConfig config = configRequest.config();
        if(config == null) {
            // Configuration should never be null if it is available, but sanity check anyway.
            this.logger.error(
                    "Could not get the configuration of the project {}",
                    rootFolder);
            return null;
        }

        final MetaborgJpsProject project = new MetaborgJpsProject(module, rootFolder, config);
        this.projects.add(project);
        return project;

        /*
        final FileObject location = this.resourceService.resolve(module.getContentRootsList().getUrls().get(0));
        final ConfigRequest<ISpoofaxLanguageSpecConfig> configRequest = this.languageSpecConfigService.get(location);
        if(!configRequest.valid()) {
            this.logger.error("Errors occurred when retrieving language specification configuration from project "+
            "location {}",
                location);
            configRequest.reportErrors(new StreamMessagePrinter(this.sourceTextService, false, false, this.logger));
            // TODO: what to return/throw?
        }
        final ISpoofaxLanguageSpecConfig config = configRequest.config();
        if(config == null) {
            // TODO: what to return/throw?
        }
        final ISpoofaxLanguageSpecPaths paths = new SpoofaxLanguageSpecPaths(location, config);
        final MetaborgJpsProject project = new MetaborgJpsProject(module, location, config, paths);
        this.projects.add(project);
        return project;
         */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public MetaborgJpsProject get(final JpsModule module) {
        for (final MetaborgJpsProject project : this.projects) {
            if (project.getModule().equals(module))
                return project;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MetaborgJpsProject get(final FileObject resource) {
        for (final MetaborgJpsProject project : this.projects) {
            final JpsModule module = project.getModule();
            if (isInContentRoot(module, resource))
                return project;
        }
        return null;
    }

    /**
     * Determines whether the file is in a content root of the specified module.
     *
     * @param module   The module to look at.
     * @param resource The file to find.
     * @return <code>true</code> when the file is in a content root of the module;
     * otherwise, <code>false</code>.
     */
    private boolean isInContentRoot(final JpsModule module, final FileObject resource) {
        final JpsUrlList contentRootsList = module.getContentRootsList();
        for (final String url : contentRootsList.getUrls()) {
            if (isEqualOrDescendant(url, resource))
                return true;
        }
        return false;
    }

    /**
     * Determines whether the specified file is equal to or a descendant of the specified path.
     *
     * @param ancestor   The path.
     * @param descendant The descendant.
     * @return <code>true</code> when the file is equal to or a descendant of the path;
     * otherwise, <code>false</code>.
     */
    private boolean isEqualOrDescendant(final String ancestor, final FileObject descendant) {
        final FileObject contentRoot = this.resourceService.resolve(ancestor);
        final FileName lhs = contentRoot.getName();
        final FileName rhs = descendant.getName();
        return lhs.equals(rhs) || lhs.isDescendent(rhs);
    }

}
