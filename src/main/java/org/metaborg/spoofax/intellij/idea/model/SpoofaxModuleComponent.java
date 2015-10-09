package org.metaborg.spoofax.intellij.idea.model;

import com.google.inject.Inject;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.spoofax.intellij.idea.IIntelliJProjectService;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.jps.JpsPlugin;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SpoofaxModuleComponent implements ModuleComponent {
    @NotNull private final Module module;
    @NotNull private IIntelliJProjectService projectService;
    @NotNull private ProjectFactory projectFactory;
    @NotNull private IIntelliJResourceService resourceService;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxModuleComponent(@NotNull final Module module) {
        this.module = module;
        IdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(@NotNull final IIntelliJProjectService projectService,
                        @NotNull final ProjectFactory projectFactory,
                        @NotNull final IIntelliJResourceService resourceService) {
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.resourceService = resourceService;
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "SpoofaxModuleComponent";
    }

    public void projectOpened() {
        FileObject root = this.resourceService.resolve(module.getModuleFile().getParent());
        IntelliJProject project = this.projectFactory.create(module, root);
        this.projectService.open(project, this.module);

    }

    public void projectClosed() {
        this.projectService.close(this.module);
    }

    public void moduleAdded() {
        // Invoked when the module corresponding to this component instance has been completely
        // loaded and added to the project.
    }
}
