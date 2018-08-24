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

import com.google.inject.Inject;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.configuration.IMetaborgProjectConfig;
import org.metaborg.intellij.configuration.MetaborgProjectConfigState;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

/**
 * Project-level configuration of the plugin.
 */
@State(
        name = IMetaborgProjectConfig.CONFIG_NAME,
        storages = { @Storage(IMetaborgProjectConfig.CONFIG_FILE) }
)
public final class IdeaMetaborgProjectConfig
        implements IMetaborgProjectConfig, PersistentStateComponent<MetaborgProjectConfigState> {

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    private MetaborgProjectConfigState state;
    private final Project project;
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
    public IdeaMetaborgProjectConfig(final Project project) {
        this.project = project;
        SpoofaxIdeaPlugin.injector().injectMembers(this);
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        loadState(new MetaborgProjectConfigState());
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
    public MetaborgProjectConfigState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     *
     * This method is only called if the configuration has changed.
     */
    @Override
    public void loadState(final MetaborgProjectConfigState state) {
        this.state = state;
        // Initialize fields that depend on state here.
    }

}
