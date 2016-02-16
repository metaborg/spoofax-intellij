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
import org.jetbrains.annotations.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.languages.*;
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
public final class IdeaMetaborgApplicationConfig implements IMetaborgApplicationConfig, ApplicationComponent,
        PersistentStateComponent<MetaborgApplicationConfigState> {

    private ConfigurationUtils configurationUtils;
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
    private void inject(final ConfigurationUtils configurationUtils) {
        this.configurationUtils = configurationUtils;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initComponent() {
        this.logger.debug("Initializing application configuration.");
        this.configurationUtils.loadAndActivateLanguages(null, this.getLoadedLanguages());
        this.logger.info("Initialized application configuration.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeComponent() {
        this.logger.debug("Disposing application configuration.");
        this.logger.info("Disposed application configuration.");
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getComponentName() {
        return IdeaMetaborgApplicationConfig.class.getName();
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
