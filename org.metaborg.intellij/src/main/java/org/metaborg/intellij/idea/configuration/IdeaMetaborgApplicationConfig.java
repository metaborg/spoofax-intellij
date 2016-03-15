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

import com.google.inject.*;
import com.intellij.openapi.components.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * Application-level configuration of the plugin.
 *
 * This component will perform any start-up actions (such as loading and activating configured languages)
 * but will not respond to configuration changes (e.g. newly added languages are not loaded or activated
 * until IntelliJ IDEA is restarted).
 */
@State(
        name = IMetaborgApplicationConfig.CONFIG_NAME,
        storages = {
                @Storage(file = StoragePathMacros.APP_CONFIG + "/" + IMetaborgApplicationConfig.CONFIG_FILE)
        }
)
public final class IdeaMetaborgApplicationConfig
        implements IMetaborgApplicationConfig, PersistentStateComponent<MetaborgApplicationConfigState> {

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    private MetaborgApplicationConfigState state;
    private Set<LanguageIdentifier> loadedLanguages;
    @InjectLogger
    private ILogger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<LanguageIdentifier> getLoadedLanguages() {
        return this.loadedLanguages;
    }

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public IdeaMetaborgApplicationConfig() {
        SpoofaxIdeaPlugin.injector().injectMembers(this);
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        loadState(new MetaborgApplicationConfigState());
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
    public MetaborgApplicationConfigState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     *
     * This method is only called if the configuration has changed.
     */
    @Override
    public void loadState(final MetaborgApplicationConfigState state) {
        this.state = state;
        // Initialize fields that depend on state here.
        this.loadedLanguages = new AdaptingSet<>(state.loadedLanguages,
                LanguageIdentifier::toString, LanguageIdentifier::parse);
    }

}
