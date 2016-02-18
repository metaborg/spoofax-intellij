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

package org.metaborg.intellij.idea.configuration;

import com.google.inject.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.module.Module;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.annotation.Nullable;

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

    @Inject
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
