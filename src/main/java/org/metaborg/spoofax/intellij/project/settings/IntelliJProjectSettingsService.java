package org.metaborg.spoofax.intellij.project.settings;

import javax.annotation.Nullable;

import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.project.settings.IProjectSettings;
import org.metaborg.core.project.settings.IProjectSettingsService;

import com.google.inject.Inject;

@Singleton
public final class IntelliJProjectSettingsService implements IProjectSettingsService {

    private final IProjectService projectService;
    private final IProjectSettingsService2 projectSettingsService;

    @Inject public IntelliJProjectSettingsService(IProjectService projectService, IProjectSettingsService2 projectSettingsService) {
        this.projectService = projectService;
        this.projectSettingsService = projectSettingsService;
    }

    @Override public @Nullable IProjectSettings get(IProject project) {
        return this.projectSettingsService.get(project);
    }

    @Override public @Nullable IProjectSettings get(FileObject location) {
        IProject project = projectService.get(location);
        return get(project);
    }
}