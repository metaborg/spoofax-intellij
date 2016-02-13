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

import com.google.inject.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.ex.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

/**
 * Project-wide JPS configuration.
 */
public final class JpsMetaborgProjectConfig
        extends AbstractMetaborgConfig<MetaborgProjectConfigState, JpsMetaborgProjectConfig>
        implements IMetaborgProjectConfig {

    public static final JpsElementChildRole<JpsMetaborgProjectConfig> ROLE
            = JpsElementChildRoleBase.create("Metaborg Project");

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    private final IJpsMetaborgProjectConfigFactory configFactory;
    @InjectLogger
    private ILogger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.getState().myName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(final String value) {
        this.getState().myName = value;
    }

    /**
     * Initializes a new instance of the {@link JpsMetaborgProjectConfig} class.
     */
    @Inject
    public JpsMetaborgProjectConfig(final IJpsMetaborgProjectConfigFactory configFactory) {
        super(new MetaborgProjectConfigState());
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        this.configFactory = configFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JpsMetaborgProjectConfig createCopy() {
        final JpsMetaborgProjectConfig config = this.configFactory.create();
        config.applyChanges(this);
        return config;
    }

    /**
     * {@inheritDoc}
     *
     * Don't use the logger here. It hasn't been injected yet.
     */
    @Override
    public void loadState(final MetaborgProjectConfigState state) {
        super.loadState(state);
        // Initialize fields that depend on state here.
    }

}
