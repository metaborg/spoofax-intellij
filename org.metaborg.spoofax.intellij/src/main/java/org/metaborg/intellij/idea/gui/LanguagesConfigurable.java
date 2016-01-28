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
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;

import java.util.*;

/**
 * {@see <a href="https://confluence.jetbrains.com/display/IDEADEV/Customizing+the+IDEA+Settings+Dialog">Customizing the
 * IDEA Settings Dialog</a>}
 */
public abstract class LanguagesConfigurable extends BaseConfigurable {

    private ILanguageService languageService;
    private final Project project;
//    private final List<FileObject> artifactsToLoad = new ArrayList<FileObject>();
//    private final List<FileObject> foldersToLoad = new ArrayList<FileObject>();
    private Set<ILanguageImpl> languages = null;
    private final Set<ILanguageImpl> languagesToLoad = new HashSet<>();
    private final Set<ILanguageImpl> languagesToUnload = new HashSet<>();

    /**
     * This instance is created by IntelliJ's plugin system.
     */
    protected LanguagesConfigurable(final Project project) {
        Preconditions.checkNotNull(project);

        this.project = project;
//        this.languageService = languageService;
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
     * Adds a language implementation.
     *
     * @param language The language implementation to add.
     */
    public void addLanguage(ILanguageImpl language) {
        this.languagesToUnload.remove(language);
        if (!this.languages.contains(language)) {
            this.languagesToLoad.add(language);
        }
        updateLanguagesList();
    }

    /**
     * Removes a language implementation.
     *
     * @param language The language implementation to remove.
     */
    public void removeLanguage(ILanguageImpl language) {
        this.languagesToLoad.remove(language);
        if (this.languages.contains(language)) {
            this.languagesToUnload.add(language);
        }
        updateLanguagesList();
    }

    /**
     * Gets the languages.
     *
     * @return A list of language implementations.
     */
    public Set<ILanguageImpl> getLanguages() {
        HashSet<ILanguageImpl> languages = Sets.newHashSet(this.languages);
        languages.removeAll(this.languagesToUnload);
//        return CollectionUtils.toSortedList(languages, new ComparatorDelegate<>(x -> x.id().toString(), new ComparableComparator()));
        languages.addAll(this.languagesToLoad);
        return languages;
    }

    protected abstract void updateLanguagesList();

//    public void addLanguage() {
//        // TODO
//    }
//    public void removeLanguage(final ILanguageImpl language) {
//        // TODO: Unload the specified language
//    }
//    public void editLanguage(final ILanguageImpl language) {
//        // Nothing to do.
//    }
//    public boolean canEditLanguage(final ILanguageImpl language) {
//        return false;
//    }
//    public boolean canRemoveLanguage(final ILanguageImpl language) {
//        return true;
//    }
//
//    private static class LanguageIterable implements Iterator<ILanguageImpl> {
//
//        private final LanguagesConfigurable configurable;
//
//        public LanguageIterable(final LanguagesConfigurable configurable) {
//            this.configurable = configurable;
//        }
//
//        @Override
//        public boolean hasNext() {
//            return false;
//        }
//
//        @Override
//        public ILanguageImpl next() {
//            return null;
//        }
//    }
}
