package org.metaborg.spoofax.intellij.project.settings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.project.settings.IProjectSettings;
import org.metaborg.core.project.settings.IProjectSettingsService;

import javax.annotation.Nullable;

@Singleton
public final class IntelliJProjectSettingsService implements IProjectSettingsService {

    @NotNull
    private final IProjectService projectService;
    @NotNull
    private final IProjectSettingsService2 projectSettingsService;

    @Inject
    public IntelliJProjectSettingsService(@NotNull IProjectService projectService,
                                          @NotNull IProjectSettingsService2 projectSettingsService) {
        this.projectService = projectService;
        this.projectSettingsService = projectSettingsService;
    }

    @Nullable
    @Override
    public final IProjectSettings get(@NotNull final IProject project) {
        return this.projectSettingsService.get(project);
    }

    @Nullable
    @Override
    public final IProjectSettings get(@NotNull final FileObject location) {
        IProject project = projectService.get(location);
        if (project == null)
            return null;
        return get(project);
    }
}