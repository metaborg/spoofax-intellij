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

package org.metaborg.intellij.idea.projects.newproject;


import com.google.inject.Singleton;
import com.intellij.ide.util.importProject.ModuleDescriptor;
import com.intellij.ide.util.importProject.ProjectDescriptor;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.util.projectWizard.importSources.DetectedProjectRoot;
import com.intellij.ide.util.projectWizard.importSources.ProjectFromSourcesBuilder;
import com.intellij.ide.util.projectWizard.importSources.ProjectStructureDetector;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Pair;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.config.ConfigRequest;
import org.metaborg.core.config.ILanguageComponentConfig;
import org.metaborg.core.config.ILanguageComponentConfigService;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.projects.MetaborgModuleType;
import org.metaborg.intellij.idea.projects.ModuleBuilderUtils;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.ILogger;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Detects whether a project is a Spoofax project when imported through the <em>Create from existing sources</em>
 * mode of the New Project/Module wizard.
 */
@Singleton
public final class MetaborgProjectDetector extends ProjectStructureDetector {

    private IIntelliJResourceService resourceService;
    private ILanguageComponentConfigService configService;
    @InjectLogger
    private ILogger logger;
    private MetaborgModuleType moduleType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgProjectDetector() {
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @jakarta.inject.Inject @javax.inject.Inject
    @SuppressWarnings("unused")
    private void inject(final MetaborgModuleType moduleType, final IIntelliJResourceService resourceService,
                        final ILanguageComponentConfigService configService) {
        this.moduleType = moduleType;
        this.resourceService = resourceService;
        this.configService = configService;
    }

    /**
     * {@inheritDoc}
     * <p>
     * We determine that a directory is the root of a Spoofax project when it
     * has a file `editor/*.main.esv`.
     */
    @Override
    public DirectoryProcessingResult detectRoots(
            final File dir,
            final File[] children,
            final File base,
            final List<DetectedProjectRoot> result) {

        this.logger.info("Detecting Spoofax project in subdirectory {} of base {}", dir, base);

        if(this.configService.available(this.resourceService.resolve(dir))) {
            this.logger.info("Detected Spoofax project in {}", base);
            result.add(new MetaborgProjectRoot(base));
            return DirectoryProcessingResult.SKIP_CHILDREN;
        }

        return DirectoryProcessingResult.PROCESS_CHILDREN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ModuleWizardStep> createWizardSteps(
            final ProjectFromSourcesBuilder builder,
            final ProjectDescriptor projectDescriptor,
            final Icon stepIcon) {

        final WizardContext context = builder.getContext();
        final ModuleWizardStep sdkStep = ProjectWizardStepFactory.getInstance().createProjectJdkStep(context);
        return Collections.singletonList(sdkStep);
    }

    @Override
    public void setupProjectStructure(
            final Collection<DetectedProjectRoot> roots,
            final ProjectDescriptor projectDescriptor,
            final ProjectFromSourcesBuilder builder) {

        if (roots.isEmpty() || builder.hasRootsFromOtherDetectors(this))
            return;

        List<ModuleDescriptor> modules = projectDescriptor.getModules();
        if (!modules.isEmpty())
            return;
        modules = new ArrayList<>();
        for (final DetectedProjectRoot root : roots) {

            final File rootDir = root.getDirectory();
            final ModuleDescriptor descriptor = new ModuleDescriptor(
                    rootDir,
                    this.moduleType,
                    Collections.emptyList()
            );

            final ConfigRequest<ILanguageComponentConfig> request = this.configService.get(this.resourceService.resolve(rootDir));
            final LanguageIdentifier languageId = request.config().identifier();

            descriptor.addConfigurationUpdater(new ModuleBuilder.ModuleConfigurationUpdater() {
                @Override
                public void update(@NotNull final Module module, @NotNull final ModifiableRootModel rootModel) {
                    final ContentEntry contentEntry = rootModel.getContentEntries()[0];
                    final FileObject contentEntryFile = MetaborgProjectDetector.this.resourceService.resolve(contentEntry.getUrl());
                    try {
                        final List<Pair<String, String>> sourcePaths = ModuleBuilderUtils.getSourcePaths(languageId, contentEntryFile);
                        ModuleBuilderUtils.addSourceRoots(contentEntry, sourcePaths);
                        ModuleBuilderUtils.excludeRoots(contentEntry, contentEntryFile);
                    } catch (ConfigurationException e) {
                        throw new UnhandledException(e);
                    }
                }
            });

            modules.add(descriptor);
        }
        projectDescriptor.setModules(modules);
    }

}
