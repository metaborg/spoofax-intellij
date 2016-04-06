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

import com.google.inject.*;
import com.intellij.ide.util.importProject.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.ide.util.projectWizard.importSources.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.build.*;
import org.metaborg.core.resource.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Detects whether a project is a Spoofax project when imported through the <em>Create from existing sources</em>
 * mode of the New Project/Module wizard.
 */
@Singleton
public final class MetaborgProjectDetector extends ProjectStructureDetector {

    private IResourceService resourceService;
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

    @Inject
    @SuppressWarnings("unused")
    private void inject(final MetaborgModuleType moduleType, final IResourceService resourceService) {
        this.moduleType = moduleType;
        this.resourceService = resourceService;
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

        final CommonPaths paths = new CommonPaths(this.resourceService.resolve(base));
        try {
            if (this.resourceService.resolve(dir).equals(paths.esvMainFile().getParent())) {
                for (final File child : children) {
                    if (child.isFile() && this.resourceService.resolve(child).equals(paths.esvMainFile())) {
                        this.logger.info("Detected Spoofax project in {}", base);
                        result.add(new MetaborgProjectRoot(base));
                        return DirectoryProcessingResult.SKIP_CHILDREN;
                    }
                }
            }
        } catch (final FileSystemException e) {
            throw new UnhandledException(e);
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

            final File directory = new File(root.getDirectory(), "editor/java");
            final DetectedSourceRoot javaFolder = new JavaModuleSourceRoot(directory, "", "Spoofax");
            final File rootDir = root.getDirectory();
            final ModuleDescriptor descriptor = new ModuleDescriptor(
                    rootDir,
                    this.moduleType,
                    Collections.singletonList(javaFolder)
            );

            modules.add(descriptor);
        }
        projectDescriptor.setModules(modules);
    }

}
