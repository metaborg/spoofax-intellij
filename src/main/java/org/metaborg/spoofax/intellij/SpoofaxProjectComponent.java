package org.metaborg.spoofax.intellij;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.serialization.SpoofaxProjectService;

// Remove this class.
public final class SpoofaxProjectComponent implements ProjectComponent {

    @NotNull
    private final Project project;

    public SpoofaxProjectComponent(@NotNull Project project) {
        this.project = project;
    }

    @Override
    public final void projectOpened() {
        SpoofaxProjectService.getInstance(this.project).getState().setMyName("Project name!");
    }

    @Override
    public final void projectClosed() {

    }

    @Override
    public final void initComponent() {

    }

    @Override
    public final void disposeComponent() {

    }

    @NotNull
    @Override
    public final String getComponentName() {
        return this.getClass().getName();
    }
}
