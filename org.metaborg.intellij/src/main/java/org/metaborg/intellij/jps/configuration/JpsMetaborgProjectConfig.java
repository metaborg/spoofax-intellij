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

package org.metaborg.intellij.jps.configuration;

import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.ex.*;
import org.metaborg.intellij.configuration.*;

/**
 * Project-wide JPS configuration.
 */
public final class JpsMetaborgProjectConfig
        extends AbstractMetaborgConfig<MetaborgProjectConfigState, JpsMetaborgProjectConfig> {

    public static final JpsElementChildRole<JpsMetaborgProjectConfig> ROLE
            = JpsElementChildRoleBase.create("Metaborg Project");

    /**
     * Initializes a new instance of the {@link JpsMetaborgProjectConfig} class.
     */
    public JpsMetaborgProjectConfig() {
        super(new MetaborgProjectConfigState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JpsMetaborgProjectConfig createCopy() {
        final JpsMetaborgProjectConfig config = new JpsMetaborgProjectConfig();
        config.applyChanges(this);
        return config;
    }

}
