/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.idea.project;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.ide.util.importProject.ModuleDescriptor;
import com.intellij.ide.util.importProject.ProjectDescriptor;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.importSources.*;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.spoofax.intellij.sdk.SpoofaxSdkType;
import org.metaborg.util.log.ILogger;
import org.slf4j.Logger;

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
public final class SpoofaxProjectDetector extends ProjectStructureDetector {

    @InjectLogger
    private ILogger logger;
    @NotNull
    private SpoofaxModuleType moduleType;
    @NotNull
    private SpoofaxSdkType sdkType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxProjectDetector() {
        super();
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(@NotNull final SpoofaxModuleType moduleType, @NotNull final SpoofaxSdkType sdkType) {
        this.moduleType = moduleType;
        this.sdkType = sdkType;
    }

    /**
     * {@inheritDoc}
     * <p>
     * We determine that a directory is the root of a Spoofax project when it
     * has a file `editor/*.main.esv`.
     */
    @NotNull
    @Override
    public DirectoryProcessingResult detectRoots(
            @NotNull final File dir,
            @NotNull final File[] children,
            @NotNull final File base,
            @NotNull final List<DetectedProjectRoot> result) {

        this.logger.info("Detecting Spoofax project in {}", dir);
        if (dir.getName().equals("editor")) {
            for (final File child : children) {
                if (child.getName().endsWith(".main.esv") && child.isFile()) {
                    this.logger.info("Detected Spoofax project in {}", dir);
                    result.add(new SpoofaxProjectRoot(dir.getParentFile()));
                    return DirectoryProcessingResult.SKIP_CHILDREN;
                }
            }
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

        final ModuleWizardStep sdkStep = ProjectWizardStepFactory.getInstance().createProjectJdkStep(builder.getContext());
        return Collections.singletonList(sdkStep);
    }

    @Override
    public void setupProjectStructure(
            @NotNull final Collection<DetectedProjectRoot> roots,
            @NotNull final ProjectDescriptor projectDescriptor,
            @NotNull final ProjectFromSourcesBuilder builder) {
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
