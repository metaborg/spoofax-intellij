package org.metaborg.spoofax.intellij.idea.model;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.language.LanguageVersion;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.ProjectException;
import org.metaborg.core.project.settings.IProjectSettings;
import org.metaborg.core.project.settings.ProjectSettings;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
import org.metaborg.spoofax.generator.NewProjectGenerator;
import org.metaborg.spoofax.generator.project.GeneratorProjectSettings;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import javax.swing.*;
import java.io.IOException;

//import org.jetbrains.idea.maven.project.MavenProjectsManager;

@Singleton
public final class SpoofaxModuleBuilder extends ModuleBuilder implements ModuleBuilderListener {

    @NotNull
    private final IIntelliJResourceService resourceService;
    private Project myProject;
    private IntelliJProject myModule;

    @Inject
    private SpoofaxModuleBuilder(@NotNull final IIntelliJResourceService resourceService) {
        this.resourceService = resourceService;
        addListener(this);
    }

    @NotNull
    public final String getPresentableName() {
        return "Spoofax";
    }

    @NotNull
    public final String getDescription() {
        return "Spoofax Module - description";
    }

    @NotNull
    public final Icon getBigIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    @NotNull
    public final Icon getNodeIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    public void setupRootModel(@NotNull final ModifiableRootModel rootModel) throws ConfigurationException {
        // Add the content entry path as a content root.
        final ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry == null) {
            // LOG: No content entry path for the module.
            return;
        }

        setSdk(rootModel);

        // Set the project and module.
        setMyProject(rootModel.getProject(), resourceService.resolve(contentEntry.getFile()));
        setMyModule(rootModel.getModule(), resourceService.resolve(contentEntry.getFile()));

        // Generate the module structure (files and directories).
        generateModuleStructure(this.myModule, rootModel, contentEntry);
    }

    private final void generateModuleStructure(@NotNull final IProject project, @NotNull final ModifiableRootModel rootModel, @NotNull final ContentEntry contentEntry) {
        final String name = "TestProject";

        try {
            final LanguageIdentifier identifier = new LanguageIdentifier("org.metaborg.test", "lang-id", new LanguageVersion(1, 0, 0, ""));
            final FileObject location = project.location();
            final IProjectSettings settings = new ProjectSettings(identifier, name);
            final SpoofaxProjectSettings spoofaxSettings = new SpoofaxProjectSettings(settings, location);
            final GeneratorProjectSettings generatorSettings = new GeneratorProjectSettings(spoofaxSettings);
            generatorSettings.setMetaborgVersion("1.5.0-SNAPSHOT");
            final NewProjectGenerator generator = new NewProjectGenerator(generatorSettings, new String[]{"spx"});
            generator.generateAll();

            // TODO: Get the source folders and exclude folders from the generator, and add them to the `contentEntry`.
            final VirtualFile f = resourceService.unresolve(project.location().resolveFile("editor/java/"));
            contentEntry.addSourceFolder(f, false, "");


        } catch (ProjectException e) {
            // Invalid project settings
            e.printStackTrace();
        } catch (IOException e) {
            // Failed to generate project files
            e.printStackTrace();
        }
    }

    @NotNull
    public final String getGroupName() {
        return "Spoofax Group";
    }


    public final void displayInitError(@NotNull final String error, @NotNull final Project project) {
        SwingUtilities.invokeLater(() -> {
            String text = "<html><a href=\"openBrowser\" target=\"_top\">How do I fix this?</a></html>";
            Notifications.Bus.notify(new Notification("SourceFinder", error, text, NotificationType.ERROR, (notification, event) -> {
                if (event.getDescription().equals("openBrowser")) {
                    //launchBrowser("https://bitbucket.org/mtiigi/intellij-sourcefinder-plugin");
                }
            }), project);
        });


    }

    @Nullable
    public final ModuleWizardStep getCustomOptionsStep(@NotNull final WizardContext context, @NotNull final Disposable parentDisposable) {
        SpoofaxProjectWizardStep step = new SpoofaxProjectWizardStep(context);
        Disposer.register(parentDisposable, step);
        return step;
    }

    public final void setMyProject(@NotNull final Project myProject, @NotNull final FileObject contentPath) {
        this.myProject = myProject;
    }

    public final void setMyModule(@NotNull final Module myModule, @NotNull final FileObject contentPath) {
        ProjectFactory projectFactory = IdeaPlugin.injector().getInstance(ProjectFactory.class);
        this.myModule = projectFactory.create(myModule, contentPath);
    }


    private final void setSdk(@NotNull final ModifiableRootModel rootModel) {
        if (this.myJdk != null) {
            // An SDK was selected in the wizard.
            rootModel.setSdk(this.myJdk);
        } else {
            // No SDK was selected in the wizard.
            rootModel.inheritSdk();
        }
    }

    @NotNull
    public final ModuleType getModuleType() {
        return SpoofaxModuleType.getModuleType();
    }

    @Override
    @NotNull
    public final ModuleWizardStep modifyProjectTypeStep(@NotNull final SettingsStep settingsStep) {
        return StdModuleTypes.JAVA.modifyProjectTypeStep(settingsStep, this);
        //return super.modifyProjectTypeStep(settingsStep);
    }

    @Nullable
    public final ModuleWizardStep modifySettingsStep(@NotNull final SettingsStep settingsStep) {
        if (settingsStep == null) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"settingsStep", "com/intellij/perlplugin/extensions/module/builder", "modifySettingsStep"}));
        }
        return SpoofaxModuleType.getModuleType().modifySettingsStep(settingsStep, this);
    }


    @Override
    public final void moduleCreated(@NotNull final Module module) {
        System.out.println("Creating Spoofax module...");

        System.out.println("Spoofax module created!");
    }

}