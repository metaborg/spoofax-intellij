/*
 * Copyright © 2015-2015
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

package org.metaborg.intellij.idea.gui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.*;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.spoofax.intellij.languages.LanguageManager;

import java.util.HashSet;
import java.util.Set;

/**
 * {@see <a href="https://confluence.jetbrains.com/display/IDEADEV/Customizing+the+IDEA+Settings+Dialog">Customizing the
 * IDEA Settings Dialog</a>}
 */
public abstract class LanguagesConfigurable extends BaseConfigurable implements PersistentStateComponent<LanguagesConfigurable> {

    private LanguageManager languageManager;
    private ILanguageService languageService;
    private IIdeaLanguageManager ideaLanguageManager;
    private final Project project;
    private Set<ILanguageImpl> languages = Sets.newHashSet();
    private final Set<ILanguageDiscoveryRequest> languagesToLoad = new HashSet<>();
    private final Set<ILanguageComponent> languagesToUnload = new HashSet<>();

    /**
     * This instance is created by IntelliJ's plugin system.
     */
    protected LanguagesConfigurable(final Project project) {
        super();
        Preconditions.checkNotNull(project);

        this.project = project;
    }

    protected void inject(final LanguageManager languageManager, final ILanguageService languageService,
                          final IIdeaLanguageManager ideaLanguageManager) {
        this.languageManager = languageManager;
        this.languageService = languageService;
        this.ideaLanguageManager = ideaLanguageManager;
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
        unloadAllIdeaLanguages();
        unloadLanguages(this.languagesToUnload);
        loadLanguages(this.languagesToLoad);
        loadAllIdeaLanguages();

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
        this.languageManager.unloadLanguages(components);
    }

    /**
     * Loads the languages from their discovery requests.
     * @param requests The requests to load.
     */
    private void loadLanguages(final Set<ILanguageDiscoveryRequest> requests) {
        try {
            this.languageManager.loadLanguages(requests);
        } catch (final MetaborgException e) {
            throw new RuntimeException("An error occurred.", e);
        }
    }

    /**
     * Unloads all IDEA languages.
     */
    private void unloadAllIdeaLanguages() {
        // FIXED: ConcurrentModificationException
        final ILanguage[] loadedLanguages = this.ideaLanguageManager.getLoaded().toArray(new ILanguage[0]);

        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (final ILanguage language : loadedLanguages) {
                this.ideaLanguageManager.unload(language);
            }
        });
    }

    /**
     * Loads all IDEA languages.
     */
    private void loadAllIdeaLanguages() {
        final Iterable<? extends ILanguage> languages = this.languageService.getAllLanguages();

        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (final ILanguage language : languages) {
                if (this.ideaLanguageManager.canLoad(language))
                    this.ideaLanguageManager.load(language);
            }
        });
    }
}
