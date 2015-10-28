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

package org.metaborg.spoofax.intellij.serialization;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = SpoofaxProjectSerializer.NAME,
        storages = {
                @Storage(file = StoragePathMacros.PROJECT_FILE),
                @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/" + SpoofaxProjectSerializer.CONFIG_FILE, scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public final class SpoofaxProjectService implements PersistentStateComponent<SpoofaxProjectState> {

    @Nullable
    private SpoofaxProjectState state;

    public SpoofaxProjectService() {
        state = new SpoofaxProjectState();
    }

    @NotNull
    public final static SpoofaxProjectService getInstance(@NotNull final Project project) {
        final SpoofaxProjectService service = ServiceManager.getService(project, SpoofaxProjectService.class);
        assert service != null;
        return service;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final SpoofaxProjectState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadState(@Nullable final SpoofaxProjectState state) {
        this.state = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return this.state.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof SpoofaxProjectService))
            return false;
        return this.state.equals(((SpoofaxProjectService) obj).state);
    }
}
