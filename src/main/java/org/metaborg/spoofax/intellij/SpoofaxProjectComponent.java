package org.metaborg.spoofax.intellij;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.serialization.SpoofaxProjectService;

public final class SpoofaxProjectComponent implements ProjectComponent {

    private final Project project;

    public SpoofaxProjectComponent(Project project)
    {
     this.project = project;
    }

    @Override
    public void projectOpened() {
        SpoofaxProjectService.getInstance(this.project).getState().setMyName("Project name!");
    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getName();
    }
}
