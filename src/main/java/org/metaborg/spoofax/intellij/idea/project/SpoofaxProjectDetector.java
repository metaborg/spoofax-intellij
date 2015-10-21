package org.metaborg.spoofax.intellij.idea.project;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.ide.util.importProject.ModuleDescriptor;
import com.intellij.ide.util.importProject.ProjectDescriptor;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectJdkForModuleStep;
import com.intellij.ide.util.projectWizard.importSources.DetectedProjectRoot;
import com.intellij.ide.util.projectWizard.importSources.ProjectFromSourcesBuilder;
import com.intellij.ide.util.projectWizard.importSources.ProjectStructureDetector;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxModuleType;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.spoofax.intellij.sdk.SpoofaxSdkType;
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
    private Logger logger;
    @NotNull
    private SpoofaxModuleType moduleType;
    @NotNull
    private SpoofaxSdkType sdkType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxProjectDetector() {
        IdeaPlugin.injector().injectMembers(this);
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
    public DirectoryProcessingResult detectRoots(@NotNull final File dir,
                                                 @NotNull final File[] children,
                                                 @NotNull final File base,
                                                 @NotNull final List<DetectedProjectRoot> result) {

        this.logger.info("Detecting Spoofax project in {}", dir);
        if (dir.getName().equals("editor")) {
            for (File child : children) {
//                if (child.getName().equals("java") && child.isDirectory()) {
//                    result.add(new JavaModuleSourceRoot(child, "", "Java"));
//                }
                if (child.getName().endsWith(".main.esv") && child.isFile()) {
                    this.logger.info("Detected Spoofax project in {}", dir);
                    result.add(new SpoofaxProjectRoot(dir.getParentFile()));
//                    result.add(new DetectedContentRoot(dir.getParentFile(), "Spoofax", this.moduleType, JavaModuleType.getModuleType()));
                    return DirectoryProcessingResult.SKIP_CHILDREN;
                }
            }
        }
//        for (File child : children) {
//            if (child.isDirectory() && child.getName() == "editor")
//            {
//                FileFilter fileFilter = new WildcardFileFilter("*.main.esv");
//                if (child.listFiles(fileFilter).length > 0) {
//                    result.add(new DetectedContentRoot(dir, "Spoofax", this.moduleType, JavaModuleType.getModuleType()));
//                    return DirectoryProcessingResult.SKIP_CHILDREN;
//                }
//            }
//        }
        return DirectoryProcessingResult.PROCESS_CHILDREN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ModuleWizardStep> createWizardSteps(final ProjectFromSourcesBuilder builder,
                                                    final ProjectDescriptor projectDescriptor,
                                                    final Icon stepIcon) {
        return Collections.singletonList(new ProjectJdkForModuleStep(builder.getContext(), this.sdkType));
//        return Collections.singletonList(ProjectWizardStepFactory.getInstance().createProjectJdkStep(builder.getContext()));
//        SpoofaxModuleInsight moduleInsight = insightFactory.create(new DelegatingProgressIndicator(), builder.getExistingModuleNames(), builder.getExistingProjectLibraryNames());
//        final List<ModuleWizardStep> steps = new ArrayList<>();
//        steps.add(new ModulesDetectionStep(this, builder, projectDescriptor, moduleInsight, stepIcon, "reference.dialogs.new.project.fromCode.page1"));
//        return steps;
    }

    @Override
    public void setupProjectStructure(@NotNull final Collection<DetectedProjectRoot> roots,
                                      @NotNull final ProjectDescriptor projectDescriptor,
                                      @NotNull final ProjectFromSourcesBuilder builder) {
        if (roots.isEmpty() || builder.hasRootsFromOtherDetectors(this))
            return;

        List<ModuleDescriptor> modules = projectDescriptor.getModules();
        if (!modules.isEmpty())
            return;
        modules = new ArrayList<>();
        for (DetectedProjectRoot root : roots) {

            // TODO: Fix source roots
            File rootDir = root.getDirectory();
            ModuleDescriptor descriptor = new ModuleDescriptor(rootDir,
                                                               this.moduleType,
                                                               ContainerUtil.emptyList());
//            File javaDir = root.getDirectory().toPath().resolve("target/java").toFile();
//            descriptor.addSourceRoot(root.getDirectory(), new JavaModuleSourceRoot(javaDir, "", "Java"));
            modules.add(descriptor);
        }
        projectDescriptor.setModules(modules);
    }
}
