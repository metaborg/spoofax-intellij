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

package org.metaborg.intellij.idea.gui.languagesettings;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.inject.*;
import com.intellij.openapi.command.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * Base class for a language configuration dialog.
 *
 * {@see <a href="https://confluence.jetbrains.com/display/IDEADEV/Customizing+the+IDEA+Settings+Dialog">Customizing the
 * IDEA Settings Dialog</a>}
 */
public abstract class LanguagesConfiguration extends BaseConfigurable {

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
    protected LanguagesConfiguration(final Project project) {
        super();
        Preconditions.checkNotNull(project);

        this.project = project;
    }

    @Inject
    @SuppressWarnings("unused")
    protected void inject(final IIdeaLanguageManager languageManager, final ILanguageService languageService) {
        this.languageManager = languageManager;
        this.languageService = languageService;
        this.languageManager = languageManager;
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
        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            this.languageManager.deactivateRange(LanguageUtils2.getLanguagesOfComponents(components));
        });
        this.languageManager.unloadRange(components);
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
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
                    "Unexpected error occurred while loading languages: {}", e, requests);
        }
        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            this.languageManager.activateRange(LanguageUtils2.getLanguagesOfComponents(components));
        });
    }
}
