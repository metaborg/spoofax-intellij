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

package org.metaborg.spoofax.intellij.idea.languages;

import org.metaborg.core.language.ILanguage;

import java.util.Set;

/**
 * Manages the Spoofax languages loaded in IntelliJ IDEA.
 */
public interface IIdeaLanguageManager {

    /**
     * Loads a language.
     *
     * Execute this method in a write action.
     *
     * @param language The language to load.
     */
    void load(ILanguage language);

    /**
     * Unloads a language.
     *
     * Execute this method in a write action.
     *
     * @param language The language to unload.
     * @return <code>true</code> when the language was successfully unloaded;
     * otherwise, <code>false</code> when the language was not loaded.
     */
    boolean unload(ILanguage language);

    /**
     * Returns whether the specified language is currently loaded.
     *
     * @param language The language to check.
     * @return <code>true</code> when the language
     * is loaded; otherwise <code>false</code>.
     */
    boolean isLoaded(ILanguage language);

    /**
     * Returns whether the specified language could ever be loaded by this manager.
     * <p>
     * The result of this method does not depend on whether the language is already loaded.
     *
     * @param language The language to check.
     * @return <code>true</code> when this manager can load the language;
     * otherwise, <code>false</code>.
     */
    boolean canLoad(ILanguage language);

    /**
     * Returns a collection of loaded languages.
     *
     * @return The loaded languages.
     */
    Set<ILanguage> getLoaded();

}
