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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * Global JPS configuration.
 */
public final class SpoofaxGlobalConfig extends SpoofaxConfig<SpoofaxGlobalState, SpoofaxGlobalConfig> {
    @NotNull
    public static final JpsElementChildRole<SpoofaxGlobalConfig> ROLE = JpsElementChildRoleBase.create("Spoofax");

    /**
     * Initializes a new instance of the {@link SpoofaxGlobalConfig} class.
     */
    public SpoofaxGlobalConfig() {
        super(new SpoofaxGlobalState());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final SpoofaxGlobalConfig createCopy() {
        SpoofaxGlobalConfig config = new SpoofaxGlobalConfig();
        config.applyChanges(this);
        return config;
    }
}
