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

package org.metaborg.intellij.idea.projects.newproject;

import com.google.inject.*;
import com.intellij.ide.util.importProject.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.ide.util.projectWizard.importSources.*;
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
    private void inject(final MetaborgModuleType moduleType) {
        this.moduleType = moduleType;
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

        this.logger.info("Detecting Spoofax project in {}", dir);

        // FIXME: Alternatively, detect a Spoofax project if it has a metaborg.yaml root config file.

        if (dir.getName().equals("editor")) {
            for (final File child : children) {
                if (child.getName().endsWith(".main.esv") && child.isFile()) {
                    this.logger.info("Detected Spoofax project in {}", dir);
                    result.add(new MetaborgProjectRoot(dir.getParentFile()));
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
