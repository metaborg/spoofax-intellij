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
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = SpoofaxGlobalSerializer.NAME,
        storages = {
                @Storage(file = StoragePathMacros.APP_CONFIG + "/" + SpoofaxGlobalSerializer.CONFIG_FILE)
        }
)
public final class SpoofaxGlobalService implements PersistentStateComponent<SpoofaxGlobalState> {

    @Nullable
    private SpoofaxGlobalState state;

    public SpoofaxGlobalService() {
        this.state = new SpoofaxGlobalState();
    }

    @NotNull
    public static SpoofaxGlobalService getInstance() {
        final SpoofaxGlobalService service = ServiceManager.getService(SpoofaxGlobalService.class);
        assert service != null;
        return service;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public SpoofaxGlobalState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadState(@NotNull final SpoofaxGlobalState state) {
        this.state = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 31)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (!(obj instanceof SpoofaxGlobalService))
            return false;
        return this.state.equals(((SpoofaxGlobalService)obj).state);
    }
}
