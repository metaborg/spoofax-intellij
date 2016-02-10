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
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Loads and saves the project-wide configuration state.
 */
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

    /**
     * Initializes a new instance of the {@link SpoofaxProjectService} class.
     */
    public SpoofaxProjectService() {
        this.state = new SpoofaxProjectState();
    }

    /**
     * Gets the instance of this service.
     *
     * @return The instance.
     */
    public static SpoofaxProjectService getInstance(final Project project) {
        return ServiceManager.getService(project, SpoofaxProjectService.class);
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
        return new HashCodeBuilder(19, 31)
                // Don't add anything here. None of the field are final.
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof SpoofaxProjectService))
            return false;
        return Objects.equals(this.state, ((SpoofaxProjectService)obj).state);
    }
}
