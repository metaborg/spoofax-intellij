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

package org.metaborg.intellij.idea.gui.languagesettings;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.metaborg.core.language.*;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.configuration.IMetaborgApplicationConfig;
import org.metaborg.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.intellij.languages.LanguageLoadingFailedException;
import org.metaborg.intellij.languages.LanguageUtils2;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.util.log.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for a language configuration dialog.
 *
 * {@see <a href="https://confluence.jetbrains.com/display/IDEADEV/Customizing+the+IDEA+Settings+Dialog">Customizing the
 * IDEA Settings Dialog</a>}
 */
public abstract class LanguagesSettings extends BaseConfigurable {

    protected IMetaborgApplicationConfig applicationConfig;
    protected ILanguageService languageService;
    protected IIdeaLanguageManager languageManager;
    protected final Project project;
    private Set<ILanguageImpl> languages = Sets.newHashSet();
    private final Set<ILanguageDiscoveryRequest> languagesToLoad = new HashSet<>();
    private final Set<ILanguageComponent> languagesToUnload = new HashSet<>();
    @InjectLogger
    protected ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    protected LanguagesSettings(final Project project) {
        super();
        Preconditions.checkNotNull(project);

        this.project = project;
    }

    @Inject
    @SuppressWarnings("unused")
    protected void inject(final IIdeaLanguageManager languageManager,
                          final ILanguageService languageService,
                          final IMetaborgApplicationConfig applicationConfig) {
        assert languageManager != null;
        assert languageService != null;
        assert applicationConfig != null;
        this.languageManager = languageManager;
        this.languageService = languageService;
        this.applicationConfig = applicationConfig;
    }

    /**
     * Gets the IntelliJ project whose languages are configured.
     *
     * @return The IntelliJ project.
     */
    public Project project() { return this.project; }

    /**
     * Called on OK or Apply.
     *
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        unloadLanguages(this.languagesToUnload);
        loadLanguages(this.languagesToLoad);

        this.languagesToUnload.clear();
        this.languagesToLoad.clear();
    }

    /**
     * Called on form load and Cancel.
     */
    @Override
    public void reset() {
        this.languages = Sets.newHashSet(this.languageService.getAllImpls());
        this.languagesToLoad.clear();
        this.languagesToUnload.clear();
        updateLanguagesList();
    }

    /**
     * Called to check whether the Apply button must be enabled.
     *
     * @return <code>true</code> when there are pending changes;
     * otherwise, <code>false</code>.
     */
    @Override
    public boolean isModified() {
        return !this.languagesToLoad.isEmpty()
                || !this.languagesToUnload.isEmpty();
    }

    /**
     * Adds a language discovery request.
     *
     * @param request The language request to add.
     */
    public void addLanguageRequest(final ILanguageDiscoveryRequest request) {
        this.languagesToLoad.add(request);
    }

    /**
     * Adds a language discovery request.
     *
     * @param request The language request to add.
     */
    public void removeLanguageRequest(final ILanguageDiscoveryRequest request) {
        this.languagesToLoad.remove(request);
    }

    /**
     * Removes a language component.
     *
     * @param language The language component to remove.
     */
    public void removeLanguageComponent(final ILanguageComponent language) {
        this.languagesToUnload.add(language);
    }

    /**
     * Gets the currently loaded languages.
     *
     * @return A list of language implementations.
     */
    public Set<ILanguageImpl> getLanguages() {
        return Sets.newHashSet(this.languages);
    }

    protected abstract void updateLanguagesList();

    /**
     * Unloads language components.
     * @param components The components to unload.
     */
    private void unloadLanguages(final Set<ILanguageComponent> components) {
        final Set<LanguageIdentifier> ids = LanguageUtils2.getIdentifiersOfLanguageImplementations(
                LanguageUtils2.getLanguageImplsOfComponents(components));
        this.applicationConfig.getLoadedLanguages().removeAll(ids);

        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            this.languageManager.deactivateRange(LanguageUtils2.getLanguagesOfComponents(components));
        });
        this.languageManager.unloadComponentRange(components);
    }

    /**
     * Loads the languages from their discovery requests.
     * @param requests The requests to load.
     */
    private void loadLanguages(final Set<ILanguageDiscoveryRequest> requests) {

        final Collection<ILanguageComponent> components;
        try {
            components = this.languageManager.loadRange(requests);

        } catch (final LanguageLoadingFailedException e) {
            throw LoggerUtils2.exception(this.logger, UnhandledException.class,
                    "Unexpected error occurred while loading languages: {}", e, requests);
        }

        final Set<LanguageIdentifier> ids = LanguageUtils2.getIdentifiersOfLanguageImplementations(
                LanguageUtils2.getLanguageImplsOfComponents(components));
        this.applicationConfig.getLoadedLanguages().addAll(ids);

        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            this.languageManager.activateRange(LanguageUtils2.getLanguagesOfComponents(components));
        });
    }
}
