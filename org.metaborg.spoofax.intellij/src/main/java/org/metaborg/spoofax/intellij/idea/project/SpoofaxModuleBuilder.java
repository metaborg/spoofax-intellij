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
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.DisposeAwareRunnable;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.UnhandledException;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.language.LanguageVersion;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.project.ILanguageSpec;
//import org.metaborg.core.project.IProject;
import org.metaborg.core.project.ProjectException;
//import org.metaborg.core.project.configuration.ILanguageSpecConfig;
//import org.metaborg.core.project.settings.IProjectSettings;
//import org.metaborg.core.project.settings.ProjectSettings;
import org.metaborg.idea.gui2.wizards.MetaborgModuleWizardStep;
import org.metaborg.idea.project.IIdeaProjectService;
import org.metaborg.idea.project.IdeaLanguageSpecProject;
//import org.metaborg.idea.project.IdeaProject;
import org.metaborg.spoofax.core.project.configuration.ISpoofaxLanguageSpecConfig;
import org.metaborg.spoofax.core.project.configuration.ISpoofaxLanguageSpecConfigBuilder;
//import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
import org.metaborg.spoofax.generator.language.NewLanguageSpecGenerator;
//import org.metaborg.spoofax.generator.language.NewProjectGenerator;
//import org.metaborg.spoofax.generator.project.GeneratorProjectSettings;
import org.metaborg.idea.project.IIdeaProjectFactory;
import org.metaborg.spoofax.generator.project.LanguageSpecGeneratorScope;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.slf4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a new Spoofax module.
 */
@Singleton
public final class SpoofaxModuleBuilder extends ModuleBuilder implements SourcePathsBuilder {

    private final IIntelliJResourceService resourceService;
    private final IIdeaProjectFactory projectFactory;
    private final IIdeaProjectService projectService;
    private final ISpoofaxLanguageSpecConfigBuilder configBuilder;
    @InjectLogger
    private Logger logger;

    private List<Pair<String,String>> sourcePaths;
    // The project.
//    private IdeaProject project;

    private String name = "Untitled";

    /**
     * Gets the name of the module.
     *
     * @return The name.
     */
    public String getName() { return this.name; }

    /**
     * Sets the name of the module.
     *
     * @param name The name.
     */
    public void setName(String name) { this.name = name; }

    private String extension = "u";

    /**
     * Gets the extension of the language.
     *
     * @return The language file extension.
     */
    public String getExtension() { return this.extension; }

    /**
     * Sets the extension of the language.
     *
     * @param extension The language file extension.
     */
    public void setExtension(String extension) { this.extension = extension; }

    private LanguageIdentifier languageId = new LanguageIdentifier("org.example", "untitled", LanguageVersion.parse("1.0.0-SNAPSHOT"));
    /**
     * Gets the language ID of the module.
     *
     * @return The language identifier.
     */
    public LanguageIdentifier getLanguageIdentifier() {
        return this.languageId;
    }

    /**
     * Sets the language ID of the module.
     *
     * @param languageId The language identifier; or <code>null</code>.
     */
    public void setLanguageIdentifier(LanguageIdentifier languageId) {
        this.languageId = languageId;
    }


    @Inject
    private SpoofaxModuleBuilder(
            final IIntelliJResourceService resourceService,
            final IIdeaProjectFactory projectFactory,
            final IIdeaProjectService projectService,
            final ISpoofaxLanguageSpecConfigBuilder configBuilder) {
        this.resourceService = resourceService;
        this.projectFactory = projectFactory;
        this.projectService = projectService;
        this.configBuilder = configBuilder;
    }

//    public final void displayInitError(@NotNull final String error, @NotNull final Project project) {
//        SwingUtilities.invokeLater(() -> {
//            String text = "<html><a href=\"openBrowser\" target=\"_top\">How do I fix this?</a></html>";
//            Notifications.Bus.notify(new Notification("SourceFinder",
//                                                      error,
//                                                      text,
//                                                      NotificationType.ERROR,
//                                                      (notification, event) -> {
//                                                          if (event.getDescription().equals("openBrowser")) {
//                                                              //launchBrowser("https://bitbucket.org/mtiigi/intellij-sourcefinder-plugin");
//                                                          }
//                                                      }), project);
//        });
//
//
//    }
    /**
     * Gets the wizard step shown under the SDK selection.
     *
     * @param context          The wizard context.
     * @param parentDisposable The parent disposable.
     * @return The wizard step.
     */
    @Override
    @Nullable
    public final ModuleWizardStep getCustomOptionsStep(
            @NotNull final WizardContext context,
            @NotNull final Disposable parentDisposable) {

        return new MetaborgModuleWizardStep(this, context);
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(
            @NotNull final WizardContext wizardContext, @NotNull final ModulesProvider modulesProvider) {
        return ModuleWizardStep.EMPTY_ARRAY;
    }

    @Override
    @Nullable
    public final ModuleWizardStep modifySettingsStep(@NotNull final SettingsStep settingsStep) {
        return SpoofaxModuleType.getModuleType().modifySettingsStep(settingsStep, this);
    }

    @Override
    @NotNull
    public final ModuleWizardStep modifyProjectTypeStep(@NotNull final SettingsStep settingsStep) {
        ModuleWizardStep wizardStep = StdModuleTypes.JAVA.modifyProjectTypeStep(settingsStep, this);
        assert wizardStep != null;
        return wizardStep;
        //return super.modifyProjectTypeStep(settingsStep);
    }

    /**
     * Setups the root model.
     *
     * @param rootModel The root model.
     * @throws ConfigurationException
     */
    @Override
    public void setupRootModel(@NotNull final ModifiableRootModel rootModel) throws ConfigurationException {

        final ContentEntry contentEntry = doAddContentEntryAndSourceRoots(rootModel);
        if (contentEntry != null) {
            // TODO: Add this information to Metaborg Core somewhere?
            contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + ".idea");
            contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + ".cache");
            contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + "lib");
            contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + "src-gen");
            contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + "include");

        }
//        if (contentEntry == null) {
//            // LOG: No content entry path for the module.
//            return;
//        }

        setSdk(rootModel);

        // Set the module.
        final Module module = rootModel.getModule();
        final Project project = module.getProject();

        runWhenInitialized(project, new DumbAwareRunnable() {
            @Override
            public void run() {
                // Generate the module structure (files and directories).
                System.out.println("Running");
                FileObject location = resourceService.resolve(getContentEntryPath());//contentEntry.getFile());
                IdeaLanguageSpecProject ideaProject = projectFactory.create(module, location);
                projectService.open(ideaProject);
                WriteCommandAction.runWriteCommandAction(project, "Create new Spoofax module", null, () -> {
                    generateModuleStructure(ideaProject);
                });
            }
        });

    }

    /**
     * Runs a runnable once the specified project has been initialized.
     *
     * @param project The project.
     * @param runnable The runnable.
     */
    private static void runWhenInitialized(@NotNull final Project project, @NotNull final Runnable runnable) {
        if (project.isDisposed())
            // Project is disposed. Nothing to do.
            return;

        Application application = ApplicationManager.getApplication();
        if (application.isHeadlessEnvironment() || application.isUnitTestMode()) {
            // Runnable cannot be run in background. Just run it.
            runnable.run();
        } else if (!project.isInitialized()) {
            // Run runnable once project has initialized.
            StartupManager.getInstance(project).registerPostStartupActivity(DisposeAwareRunnable.create(runnable, project));
        } else if (DumbService.isDumbAware(runnable)) {
            // The runnable is dumb aware. Just run it.
            runnable.run();
        } else {
            // The runnable is not dumb aware. Run it when applicable.
            DumbService.getInstance(project).runWhenSmart(DisposeAwareRunnable.create(runnable, project));
        }
    }

    /**
     * Sets the SDK.
     *
     * @param rootModel The root model.
     */
    private final void setSdk(@NotNull final ModifiableRootModel rootModel) {
        if (this.myJdk != null) {
            // An SDK was selected in the wizard.
            rootModel.setSdk(this.myJdk);
        } else {
            // No SDK was selected in the wizard.
            rootModel.inheritSdk();
        }
    }

    /**
     * Generates the module directory structure and files.
     *
     * @param languageSpec      The language specification.
     */
    private final void generateModuleStructure(
            @NotNull final ILanguageSpec languageSpec) {
        final String name = getName();
        final LanguageIdentifier identifier = getLanguageIdentifier();

        try {
            final FileObject location = languageSpec.location();
            ISpoofaxLanguageSpecConfig config = this.configBuilder
                    .reset()
                    .withIdentifier(identifier)
                    .withName(name)
                    .build();
            final LanguageSpecGeneratorScope scope = new LanguageSpecGeneratorScope(location, config);
//            // TODO: Get from SDK.
//            generatorSettings.setMetaborgVersion("1.5.0-SNAPSHOT");
            final NewLanguageSpecGenerator generator = new NewLanguageSpecGenerator(scope, new String[]{getExtension()});
            generator.generateAll();

//            // TODO: Get the source folders and exclude folders from the generator, and add them to the `contentEntry`.
//            final VirtualFile f = resourceService.unresolve(project.location().resolveFile("editor/java/"));
//            contentEntry.addSourceFolder(f, false, "");


        } catch (ProjectException | IOException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Gets the module type.
     *
     * @return The module type.
     */
    @Override
    @NotNull
    public final ModuleType getModuleType() {
        return SpoofaxModuleType.getModuleType();
    }

    /**
     * Gets the module's big icon.
     *
     * @return The big icon.
     */
    // TODO: Use project's ILanguage facet defined icon.
    @Override
    @NotNull
    public final Icon getBigIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    /**
     * Gets the module's normal icon.
     *
     * @return The normal icon.
     */
    // TODO: Use project's ILanguage facet defined icon.
    @Override
    @NotNull
    public final Icon getNodeIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    /**
     * Gets the module builder's description.
     *
     * @return The module builder's description.
     */
    @Override
    @NotNull
    public final String getDescription() {
        return "Creates a new <b>Spoofax Language</b> module, used for developing domain-specific languages " +
                "using the <b>Spoofax Language Workbench</b>.";
    }

    /**
     * Gets the module builder's presentable name.
     * <p>
     * This name is shown in the <em>New Project</em> and <em>New Module</em> wizards.
     *
     * @return The module builder's presentable name.
     */
    @Override
    @NotNull
    public final String getPresentableName() {
        return "Spoofax Language";
    }

    /**
     * Gets the module builder's group name.
     * <p>
     * I suspect module builders with the same group name are grouped
     * in the <em>New Project</em> and <em>New Module</em> wizards.
     *
     * @return The group name.
     */
    @Override
    @NotNull
    public final String getGroupName() {
        return "Spoofax";
    }

    /**
     * Gets a list of source paths.
     *
     * @return A list of (path, packagePrefix) pairs.
     * @throws ConfigurationException
     */
    @Override
    public List<Pair<String, String>> getSourcePaths() throws ConfigurationException {
        if (this.sourcePaths == null) {
            final List<Pair<String, String>> paths = new ArrayList<>();
            final String path = getContentEntryPath() + File.separator + "editor" + File.separator + "java";
            new File(path).mkdirs();
            paths.add(Pair.create(path, ""));
            return paths;
        }
        return this.sourcePaths;
    }

    /**
     * Sets the list of source paths.
     *
     * @param sourcePaths A list of (path, packagePrefix) pairs.
     */
    @Override
    public void setSourcePaths(final List<Pair<String, String>> sourcePaths) {
        this.sourcePaths = sourcePaths != null ? new ArrayList<>(sourcePaths) : null;
    }

    /**
     * Adds a source path.
     *
     * @param sourcePathInfo A (path, packagePrefix) pair.
     */
    @Override
    public void addSourcePath(final Pair<String, String> sourcePathInfo) {
        if (this.sourcePaths == null) {
            this.sourcePaths = new ArrayList<>();
        }
        this.sourcePaths.add(sourcePathInfo);
    }

    @Nullable
    protected ContentEntry doAddContentEntryAndSourceRoots(ModifiableRootModel rootModel) throws ConfigurationException {
        // Add the content entry path as a content root.
        final ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry == null)
            return null;

//        if (contentEntry != null) {
        final List<Pair<String,String>> sourcePaths = getSourcePaths();

        if (sourcePaths == null)
            return null;
//            if (sourcePaths != null) {
        for (final Pair<String, String> sourcePath : sourcePaths) {
            String first = sourcePath.first;
            try {
                VfsUtil.createDirectories(first);
            } catch (IOException e) {
                throw new UnhandledException(e);
            }
            final VirtualFile sourceRoot = LocalFileSystem.getInstance()
                    .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(first));
            if (sourceRoot != null) {
                contentEntry.addSourceFolder(sourceRoot, false, sourcePath.second);
            }
        }
//            }
//        }
        return contentEntry;
    }
}