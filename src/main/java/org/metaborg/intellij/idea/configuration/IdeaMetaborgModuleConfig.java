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

package org.metaborg.intellij.idea.configuration;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.module.Module;
import org.metaborg.intellij.configuration.IMetaborgModuleConfig;
import org.metaborg.intellij.configuration.MetaborgModuleConfigState;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import jakarta.annotation.Nullable;

/**
 * Module-level configuration of the plugin.
 */
@State(
        name = IMetaborgModuleConfig.CONFIG_NAME,
        storages = {
                @Storage(file = StoragePathMacros.MODULE_FILE)
        }
)
public final class IdeaMetaborgModuleConfig
        implements IMetaborgModuleConfig, PersistentStateComponent<MetaborgModuleConfigState> {

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    private MetaborgModuleConfigState state;
    private final Module module;
    @InjectLogger
    private ILogger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.state.myName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(final String value) {
        this.state.myName = value;
    }

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public IdeaMetaborgModuleConfig(final Module module) {
        this.module = module;
        SpoofaxIdeaPlugin.injector().injectMembers(this);
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        loadState(new MetaborgModuleConfigState());
    }

    @jakarta.inject.Inject @javax.inject.Inject
    @SuppressWarnings("unused")
    private void inject() {
    }


    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MetaborgModuleConfigState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     *
     * This method is only called if the configuration has changed.
     */
    @Override
    public void loadState(final MetaborgModuleConfigState state) {
        this.state = state;
        // Initialize fields that depend on state here.
    }

}
