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
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.metaborg.core.language.ILanguageDiscoveryRequest;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;

import java.util.HashSet;
import java.util.Set;

/**
 * {@see <a href="https://confluence.jetbrains.com/display/IDEADEV/Customizing+the+IDEA+Settings+Dialog">Customizing the
 * IDEA Settings Dialog</a>}
 */
public abstract class LanguagesConfigurable extends BaseConfigurable {

    private ILanguageService languageService;
    private final Project project;
    private Set<ILanguageImpl> languages = null;
    private final Set<ILanguageDiscoveryRequest> languagesToLoad = new HashSet<>();
    private final Set<ILanguageImpl> languagesToUnload = new HashSet<>();

    /**
     * This instance is created by IntelliJ's plugin system.
     */
    protected LanguagesConfigurable(final Project project) {
        super();
        Preconditions.checkNotNull(project);

        this.project = project;
    }

    protected void inject(final ILanguageService languageService) {
        this.languageService = languageService;
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
        // TODO: Actually load/unload languages.
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
//        this.languagesToUnload.remove(language);
//        if (!this.languages.contains(language)) {
        this.languagesToLoad.add(request);
//        }
//        updateLanguagesList();
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
     * Removes a language implementation.
     *
     * @param language The language implementation to remove.
     */
    public void removeLanguageImpl(final ILanguageImpl language) {
//        this.languagesToLoad.remove(language);
//        if (this.languages.contains(language)) {
        this.languagesToUnload.add(language);
//        }
//        updateLanguagesList();
    }

    /**
     * Gets the languages.
     *
     * @return A list of language implementations.
     */
    public Set<ILanguageImpl> getLanguages() {
        final HashSet<ILanguageImpl> languages = Sets.newHashSet(this.languages);
//        languages.removeAll(this.languagesToUnload);
//        return CollectionUtils.toSortedList(languages, new ComparatorDelegate<>(x -> x.id().toString(), new ComparableComparator()));
//        languages.addAll(this.languagesToLoad);
        return languages;
    }

    protected abstract void updateLanguagesList();

}
