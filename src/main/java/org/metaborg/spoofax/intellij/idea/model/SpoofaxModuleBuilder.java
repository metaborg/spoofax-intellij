package org.metaborg.spoofax.intellij.idea.model;

import com.google.inject.Singleton;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
//import org.jetbrains.idea.maven.project.MavenProjectsManager;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public final class SpoofaxModuleBuilder extends ModuleBuilder implements ModuleBuilderListener {

    private Project myProject;
    private IntelliJProject myModule;
    private IIntelliJResourceService resourceService;

    public SpoofaxModuleBuilder()
    {
        this.resourceService = IdeaPlugin.injector().getInstance(IIntelliJResourceService.class);
        addListener(this);
    }

    public String getPresentableName() {
        return "Spoofax";
    }

    public String getDescription() {
        return "Spoofax Module - description";
    }

    public Icon getBigIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    public Icon getNodeIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        // Add the content entry path as a content root.
        ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry == null)
        {
            // LOG: No content entry path for the module.
            return;
        }

        setSdk(rootModel);

        // Set the project and module.
        setMyProject(rootModel.getProject(), resourceService.resolve(contentEntry.getFile()));
        setMyModule(rootModel.getModule(), resourceService.resolve(contentEntry.getFile()));

        // Generate the module structure (files and directories).
        generateModuleStructure(this.myModule, rootModel, contentEntry);

        addAsMavenProject(rootModel);
    }

    private void generateModuleStructure(IProject project, ModifiableRootModel rootModel, ContentEntry contentEntry)
    {
        String name = "TestProject";

        try {
            LanguageIdentifier identifier = new LanguageIdentifier("org.metaborg.test", "lang-id", new LanguageVersion(1,0,0,""));
            FileObject location = project.location();
            IProjectSettings settings = new ProjectSettings(identifier, name);
            SpoofaxProjectSettings spoofaxSettings = new SpoofaxProjectSettings(settings, location);
            GeneratorProjectSettings generatorSettings = new GeneratorProjectSettings(spoofaxSettings);
            generatorSettings.setMetaborgVersion("1.5.0-SNAPSHOT");
            //EclipseProjectGenerator generator = new EclipseProjectGenerator(generatorSettings);
            //IntelliJProjectGenerator generator = new IntelliJProjectGenerator(generatorSettings);
            NewProjectGenerator generator = new NewProjectGenerator(generatorSettings, new String[]{"spx"});
            generator.generateAll();

            // TODO: Get the source folders and exclude folders from the generator, and add them to the `contentEntry`.

        } catch (ProjectException e) {
            // Invalid project settings
            e.printStackTrace();
        } catch (IOException e) {
            // Failed to generate project files
            e.printStackTrace();
        }
    }

    public String getGroupName() {
        return "Spoofax Group";
    }

    public Project getMyProject() {
        return this.myProject;
    }
    public IntelliJProject getMyModule() {
        return this.myModule;
    }


    private void addAsMavenProject(ModifiableRootModel rootModel)
    {
        ProjectManager.getInstance().addProjectManagerListener(rootModel.getProject(), new ProjectManagerListener() {

            @Override
            public void projectOpened(Project project) {
                /*
                MavenProjectsManager manager = MavenProjectsManager.getInstance(project);
                String pomPath = project.getBaseDir().getPath() + "/pom.xml";
                manager.setIgnoredFilesPaths(Collections.singletonList(pomPath));
                */

                /*
                VirtualFile pomFile = project.getBaseDir().findChild("pom.xml");

                if (pomFile == null) {
                    displayInitError(String.format("Couldn't find POM?"), project);
                } else {

                    manager.addManagedFiles(Collections.singletonList(pomFile));
                }
*/
            }

            @Override
            public boolean canCloseProject(Project project) {
                return true;
            }

            @Override
            public void projectClosed(Project project) {
            }

            @Override
            public void projectClosing(Project project) {
            }
        });
    }


    public void displayInitError(final String error, final Project project){
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
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        SpoofaxProjectWizardStep step = new SpoofaxProjectWizardStep(context);
        Disposer.register(parentDisposable, step);
        return step;
    }

    public void setMyProject(Project myProject, FileObject contentPath) {
        this.myProject = myProject;
    }

    public void setMyModule(Module myModule, FileObject contentPath) {
        ProjectFactory projectFactory = IdeaPlugin.injector().getInstance(ProjectFactory.class);
        this.myModule = projectFactory.create(myModule, contentPath);
    }


    private void setSdk(ModifiableRootModel rootModel) {
        if (this.myJdk != null) {
            // An SDK was selected in the wizard.
            rootModel.setSdk(this.myJdk);
        } else {
            // No SDK was selected in the wizard.
            rootModel.inheritSdk();
        }
    }

    public ModuleType getModuleType() {
        return SpoofaxModuleType.getModuleType();
    }


    @Nullable
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        if (settingsStep == null) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"settingsStep", "com/intellij/perlplugin/extensions/module/builder", "modifySettingsStep"}));
        }
        return SpoofaxModuleType.getModuleType().modifySettingsStep(settingsStep, this);
    }


    @Override
    public void moduleCreated(@NotNull Module module) {
        System.out.println("Creating Spoofax module...");

        //setupFacet(module);

        System.out.println("Spoofax module created!");
    }



    // https://github.com/jsinglet/VerilyIdeaPlugin/blob/32a01a89ed323819bc021d3279c2d9b452412561/src/verily/module/VerilyMavenModuleBuilder.java
    // https://github.com/JetBrains/intellij-sdk-docs/blob/7b75f3ad99d956211585f457af711ed91e6074fe/tutorials/project_wizard/module_types.md
    // https://github.com/rayshade/stardust/blob/8a20932e1e4637a1a04c57412b46a87559181c7d/src/stardust/wizard/StardustModuleBuilder.java
}