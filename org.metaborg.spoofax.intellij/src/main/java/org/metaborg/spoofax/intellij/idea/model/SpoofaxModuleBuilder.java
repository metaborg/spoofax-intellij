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

package org.metaborg.spoofax.intellij.idea.model;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.DisposeAwareRunnable;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.language.LanguageVersion;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.ProjectException;
import org.metaborg.core.project.settings.IProjectSettings;
import org.metaborg.core.project.settings.ProjectSettings;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
import org.metaborg.spoofax.generator.language.NewProjectGenerator;
import org.metaborg.spoofax.generator.project.GeneratorProjectSettings;
import org.metaborg.spoofax.intellij.factories.IProjectFactory;
import org.metaborg.spoofax.intellij.project.IIntelliJProjectService;
import org.metaborg.spoofax.intellij.project.IntelliJProject;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.slf4j.Logger;

import javax.swing.*;
import java.io.IOException;

/**
 * Builds a new Spoofax module.
 */
@Singleton
public final class SpoofaxModuleBuilder extends ModuleBuilder {

    @NotNull
    private final IIntelliJResourceService resourceService;
    @NotNull
    private final IProjectFactory projectFactory;
    @NotNull
    private final IIntelliJProjectService projectService;
    @InjectLogger
    private Logger logger;
    // The project.
//    private IntelliJProject project;

    private String name;

    /**
     * Gets the name of the module.
     *
     * @return The name; or <code>null</code>.
     */
    @Nullable
    public String getName() { return this.name; }

    /**
     * Sets the name of the module.
     *
     * @param name The name; or <code>null</code>.
     */
    public void setName(@Nullable String name) { this.name = name; }

    private LanguageIdentifier languageId;
    /**
     * Gets the language ID of the module.
     *
     * @return The language identifier; or <code>null</code>.
     */
    @Nullable
    public LanguageIdentifier getLanguageIdentifier() {
        return this.languageId;
    }

    /**
     * Sets the language ID of the module.
     *
     * @param languageId The language identifier; or <code>null</code>.
     */
    public void setLanguageIdentifier(@Nullable LanguageIdentifier languageId) {
        this.languageId = languageId;
    }


    @Inject
    private SpoofaxModuleBuilder(
            @NotNull final IIntelliJResourceService resourceService,
            @NotNull final IProjectFactory projectFactory,
            @NotNull final IIntelliJProjectService projectService) {
        this.resourceService = resourceService;
        this.projectFactory = projectFactory;
        this.projectService = projectService;
    }

    public final void displayInitError(@NotNull final String error, @NotNull final Project project) {
        SwingUtilities.invokeLater(() -> {
            String text = "<html><a href=\"openBrowser\" target=\"_top\">How do I fix this?</a></html>";
            Notifications.Bus.notify(new Notification("SourceFinder",
                                                      error,
                                                      text,
                                                      NotificationType.ERROR,
                                                      (notification, event) -> {
                                                          if (event.getDescription().equals("openBrowser")) {
                                                              //launchBrowser("https://bitbucket.org/mtiigi/intellij-sourcefinder-plugin");
                                                          }
                                                      }), project);
        });


    }
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
        SpoofaxProjectWizardStep step = new SpoofaxProjectWizardStep(context);
        Disposer.register(parentDisposable, step);
        return step;
    }

    @Override
    @Nullable
    public final ModuleWizardStep modifySettingsStep(@NotNull final SettingsStep settingsStep) {
        return SpoofaxModuleType.getModuleType().modifySettingsStep(settingsStep, this);
    }

    @Override
    @NotNull
    public final ModuleWizardStep modifyProjectTypeStep(@NotNull final SettingsStep settingsStep) {
        return StdModuleTypes.JAVA.modifyProjectTypeStep(settingsStep, this);
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
        // Add the content entry path as a content root.
        final ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry == null) {
            // LOG: No content entry path for the module.
            return;
        }


        setSdk(rootModel);

        // Set the module.
        final Module module = rootModel.getModule();
        final Project project = module.getProject();

        runWhenInitialized(project, new DumbAwareRunnable() {
            @Override
            public void run() {
                // Generate the module structure (files and directories).
                System.out.println("Running");
                FileObject location = resourceService.resolve(contentEntry.getFile());
                IntelliJProject intelliJProject = projectFactory.create(module, location);
                projectService.open(intelliJProject);
                WriteCommandAction.runWriteCommandAction(project, "Create new Spoofax module", null, () -> {
                    generateModuleStructure(intelliJProject);
                });
            }
        });

    }

//    @Nullable
//    @Override
//    public Module commitModule(@NotNull final Project project, @Nullable final ModifiableModuleModel model) {
//        Module module = super.commitModule(project, model);
//
//        if (module != null) {
//            // Generate the module structure (files and directories).
//            FileObject location = resourceService.resolve(contentEntry.getFile());
//            IntelliJProject intelliJProject = this.projectFactory.create(module, location);
//            this.projectService.open(intelliJProject);
//            generateModuleStructure(intelliJProject);
//        }
//
//        return module;
//    }

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
     * @param project      The project.
//     * @param rootModel    The root model.
//     * @param contentEntry The content entry.
     */
    private final void generateModuleStructure(
            @NotNull final IProject project) {
//            @NotNull final ModifiableRootModel rootModel,
//            @NotNull final ContentEntry contentEntry) {
        // TODO: Specify name in wizard
        final String name = "TestProject";
        // TODO: Specify in wizard
        final LanguageIdentifier identifier = new LanguageIdentifier("org.metaborg.test",
                                                                     "lang-id",
                                                                     new LanguageVersion(1, 0, 0, ""));
        final IProjectSettings settings = new ProjectSettings(identifier, name);

        try {
            final FileObject location = project.location();
            final SpoofaxProjectSettings spoofaxSettings = new SpoofaxProjectSettings(settings, location);
            final GeneratorProjectSettings generatorSettings = new GeneratorProjectSettings(spoofaxSettings);
            // TODO: Get from SDK.
            generatorSettings.setMetaborgVersion("1.5.0-SNAPSHOT");
            // TODO: Specify extension in wizard.
            final NewProjectGenerator generator = new NewProjectGenerator(generatorSettings, new String[]{"spx"});
            generator.generateAll();

            // TODO: Get the source folders and exclude folders from the generator, and add them to the `contentEntry`.
            final VirtualFile f = resourceService.unresolve(project.location().resolveFile("editor/java/"));
//            contentEntry.addSourceFolder(f, false, "");


        } catch (ProjectException e) {
            // Invalid project settings
            logger.error("Unhandled exception.", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            // Failed to generate project files
            logger.error("Unhandled exception.", e);
            throw new RuntimeException(e);
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

}