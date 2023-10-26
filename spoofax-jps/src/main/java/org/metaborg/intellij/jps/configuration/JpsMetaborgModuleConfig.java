/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.jps.configuration;

import com.google.inject.*;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;
import org.metaborg.intellij.configuration.IMetaborgModuleConfig;
import org.metaborg.intellij.configuration.MetaborgModuleConfigState;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

/**
 * Module-specific JPS configuration.
 */
public final class JpsMetaborgModuleConfig
        extends AbstractMetaborgConfig<MetaborgModuleConfigState, JpsMetaborgModuleConfig>
        implements IMetaborgModuleConfig {

    public static final JpsElementChildRole<JpsMetaborgModuleConfig> ROLE
            = JpsElementChildRoleBase.create("Metaborg Module");

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    private final IJpsMetaborgModuleConfigFactory configFactory;
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
     * Initializes a new instance of the {@link JpsMetaborgModuleConfig} class.
     */
    @jakarta.inject.Inject @javax.inject.Inject
    public JpsMetaborgModuleConfig(final IJpsMetaborgModuleConfigFactory configFactory) {
        super(new MetaborgModuleConfigState());
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        this.configFactory = configFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JpsMetaborgModuleConfig createCopy() {
        final JpsMetaborgModuleConfig config = this.configFactory.create();
        config.applyChanges(this);
        return config;
    }

    /**
     * {@inheritDoc}
     *
     * Don't use the logger here. It hasn't been injected yet.
     */
    @Override
    public void loadState(final MetaborgModuleConfigState state) {
        super.loadState(state);
        // Initialize fields that depend on state here.
    }
}
