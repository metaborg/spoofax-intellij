/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.jps.serialization;

import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.ex.*;

/**
 * Project-wide JPS configuration.
 */
public final class SpoofaxProjectConfig extends SpoofaxConfig<SpoofaxProjectState, SpoofaxProjectConfig> {
    public static final JpsElementChildRole<SpoofaxProjectConfig> ROLE = JpsElementChildRoleBase.create(
            "Spoofax Project");

    /**
     * Initializes a new instance of the {@link SpoofaxProjectConfig} class.
     */
    public SpoofaxProjectConfig() {
        super(new SpoofaxProjectState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SpoofaxProjectConfig createCopy() {
        final SpoofaxProjectConfig config = new SpoofaxProjectConfig();
        config.applyChanges(this);
        return config;
    }

}
