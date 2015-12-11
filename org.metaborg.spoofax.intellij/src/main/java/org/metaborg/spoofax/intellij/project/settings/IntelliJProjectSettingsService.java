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

/**
 * Project settings service for IntelliJ.
 */
@Deprecated
@Singleton
public final class IntelliJProjectSettingsService implements IProjectSettingsService {

    @NotNull
    private final IProjectService projectService;
    @NotNull
    private final IProjectSettingsService2 projectSettingsService;

    @Inject
    /* package private */ IntelliJProjectSettingsService(
            @NotNull IProjectService projectService,
            @NotNull IProjectSettingsService2 projectSettingsService) {
        this.projectService = projectService;
        this.projectSettingsService = projectSettingsService;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final IProjectSettings get(@NotNull final IProject project) {
        return this.projectSettingsService.get(project);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final IProjectSettings get(@NotNull final FileObject location) {
        IProject project = projectService.get(location);
        if (project == null)
            return null;
        return get(project);
    }
}