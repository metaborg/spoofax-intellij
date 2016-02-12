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

import com.google.common.collect.*;
import com.google.inject.*;
import com.intellij.openapi.components.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.discovery.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
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
        name = "MetaborgApplicationConfigManager",
        storages = {
                @Storage(file = StoragePathMacros.APP_CONFIG + "/metaborg.xml")
        }
)
public final class MetaborgApplicationConfig implements ApplicationComponent,
        PersistentStateComponent<MetaborgApplicationConfigState> {

    private ILanguageSource languageSource;
    private ILanguageManager languageManager;
    private MetaborgApplicationConfigState state = new MetaborgApplicationConfigState();
    @InjectLogger
    private ILogger logger;
    private final Set<LanguageIdentifier> loadedLanguages;

    /**
     * Gets thea mutable set of identifiers of languages that should be loaded and activated.
     *
     * @return A mutable set of language identifiers.
     */
    public Set<LanguageIdentifier> getLoadedLanguages() {
        return this.loadedLanguages;
    }

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgApplicationConfig() {
        this.loadedLanguages = new AdaptingSet<>(this.state.loadedLanguages,
                LanguageIdentifier::toString, LanguageIdentifier::parse);

        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final ILanguageSource languageSource, final ILanguageManager languageManager) {
        this.languageSource = languageSource;
        this.languageManager = languageManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initComponent() {
        // Occurs when the application is starting.

        loadAndActivateLanguages();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeComponent() {
        // Occurs when the application is quitting.
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getComponentName() {
        return MetaborgApplicationConfig.class.getName();
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
    }

    /**
     * Loads the languages.
     */
    private void loadAndActivateLanguages() {
        for (final LanguageIdentifier id : this.getLoadedLanguages()) {
            @Nullable final FileObject rootLocation = this.languageSource.find(id);
            if (rootLocation == null) {
                this.logger.error("Could not find language with id '{}'.", id);
                continue;
            }
            loadAndActivateLanguage(id, rootLocation);
        }
    }

    /**
     * Loads and activates a language from the specified source.
     *
     * @param id The language identifier.
     * @param rootLocation The language's root location.
     */
    private void loadAndActivateLanguage(final LanguageIdentifier id, final FileObject rootLocation) {
        final Iterable<ILanguageDiscoveryRequest> requests = this.languageManager.discover(rootLocation);
        if (Iterables.isEmpty(requests)) {
            this.logger.error("Got no discovery requests for language '{}' at: {}", id, rootLocation);
            return;
        }
        final Collection<ILanguageComponent> components;
        try {
            components = this.languageManager.loadRange(requests);
        } catch (final LanguageLoadingFailedException e) {
            this.logger.error("Could not load language '{}' at: {}", e, id, rootLocation);
            return;
        }
        this.languageManager.activateRange(LanguageUtils2.getLanguagesOfComponents(components));
    }

//    /**
//     * Parses the language identifier string.
//     *
//     * @param idString The language identifier string.
//     * @return The parsed identifier; or <code>null</code> when invalid.
//     */
//    @Nullable
//    private LanguageIdentifier parseIdentifier(final String idString) {
//        try {
//            return LanguageIdentifier.parse(idString);
//        } catch (final IllegalArgumentException ex) {
//            this.logger.error("Invalid language identifier: {}", idString);
//            return null;
//        }
//    }
}
