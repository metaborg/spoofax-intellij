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

package org.metaborg.intellij.idea.facets;

import com.google.inject.Inject;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.projects.IIdeaProjectFactory;
import org.metaborg.intellij.idea.projects.IIdeaProjectService;
import org.metaborg.intellij.idea.projects.IdeaProject;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.*;

/**
 * A Metaborg facet.
 *
 * This facet on a module indicates that the module uses files written in a Metaborg language.
 */
public class MetaborgFacet extends Facet {

    public static final FacetTypeId<MetaborgFacet> ID = new FacetTypeId<>(MetaborgFacetType.ID);

    private IProjectConfigBuilder configBuilder;
    private IProjectConfigWriter configWriter;
    private IProjectConfigService configService;
    private IIdeaProjectService projectService;
    private IIdeaProjectFactory projectFactory;
    private ISourceTextService sourceTextService;
    private IIntelliJResourceService resourceService;
    @InjectLogger
    private ILogger logger;

    /**
     * Gets the {@link MetaborgFacet} that corresponds to the specified module.
     *
     * @param module The module.
     * @return The facet; or <code>null</code> when the facet is not applied to the specified module.
     */
    @Nullable
    public static MetaborgFacet getInstance(final Module module) {
        return FacetManager.getInstance(module).getFacetByType(ID);
    }

    /**
     * Initializes a new instance of the {@link MetaborgFacet} class.
     *
     * @param facetType The facet type.
     * @param module The module to which the facet is applied.
     * @param name The name of the facet.
     * @param configuration The configuration of the facet.
     * @param underlyingFacet The underlying facet.
     */
    public MetaborgFacet(final MetaborgFacetType facetType,
                         final Module module,
                         final String name,
                         final FacetConfiguration configuration,
                         @Nullable final Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final IProjectConfigBuilder configBuilder,
                        final IProjectConfigWriter configWriter,
                        final IProjectConfigService configService,
                        final IIdeaProjectService projectService,
                        final IIdeaProjectFactory projectFactory,
                        final ISourceTextService sourceTextService,
                        final IIntelliJResourceService resourceService) {
        this.configBuilder = configBuilder;
        this.configWriter = configWriter;
        this.configService = configService;
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.sourceTextService = sourceTextService;
        this.resourceService = resourceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initFacet() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeFacet() {

    }

    public void applyFacet(final ModifiableRootModel model) {
        this.logger.debug("Facet initializing!");

        final Module module = model.getModule();
        final VirtualFile[] contentRoots = model.getContentRoots();

        if (contentRoots.length == 0) {
            throw LoggerUtils2.exception(this.logger, RuntimeException.class,
                    "The module {} has no content roots.", module);
        }

        // Try get the IDEA project.
        @Nullable IdeaProject ideaProject = this.projectService.get(module);

        if (ideaProject == null) {
            this.logger.debug("Generating project files.");

            // FIXME: We shouldn't just pick the first content root.
            final FileObject rootFolder = this.resourceService.resolve(contentRoots[0]);

            final ConfigRequest<IProjectConfig> configRequest = this.configService.get(rootFolder);
            if (!configRequest.valid()) {
                this.logger.error(
                        "An error occurred while retrieving the configuration for the project at {}", rootFolder);
                configRequest.reportErrors(new StreamMessagePrinter(this.sourceTextService, false, false, this.logger));
                return;
            }

            // Try to get the configuration.
            @Nullable IProjectConfig config = configRequest.config();
            if (config == null) {
                this.logger.info("Project has no Metaborg configuration {}", rootFolder);

                config = this.configBuilder
                        .reset()
                        .build(rootFolder);

                ideaProject = this.projectFactory.create(module, rootFolder, config);

                assert ideaProject != null;

                final IdeaProject finalProject = ideaProject;
                final IProjectConfig finalConfig = config;
                WriteCommandAction.runWriteCommandAction(
                        this.getModule().getProject(), "Write Metaborg configuration file", null, () -> {
                            try {
                                this.configWriter.write(finalProject, finalConfig, null);
                            } catch (final ConfigException e) {
                                throw LoggerUtils2.exception(this.logger, UnhandledException.class,
                                        "An unexpected exception occurred while writing the configuration for project {}",
                                        finalProject);
                            }
                        });

                this.logger.info("Generated project files.");
            } else {
                ideaProject = this.projectFactory.create(module, rootFolder, config);
            }

            assert ideaProject != null;

            this.projectService.open(ideaProject);
        }

        this.logger.info("Facet initialized!");
    }

}
