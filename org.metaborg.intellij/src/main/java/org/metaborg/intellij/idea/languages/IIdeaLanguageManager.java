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

package org.metaborg.intellij.idea.languages;

import org.metaborg.core.language.*;
import org.metaborg.intellij.languages.*;

import java.util.*;

/**
 * Manages loaded and activated languages.
 *
 * A loaded language is a language that is known to Metaborg Core.
 * An activated language is a language whose effects (syntax highlighting,
 * transformations, parsing) are visible in IntelliJ IDEA.
 *
 * The implementation must be thread-safe.
 */
public interface IIdeaLanguageManager extends ILanguageManager {

    /**
     * Gets a collection of all currently activated languages.
     *
     * @return A collection of activated languages.
     */
    Collection<ILanguage> getActiveLanguages();

    /**
     * Gets whether the specified language is currently active.
     *
     * @param language The language to test.
     * @return <code>true</code> when the language is active;
     * otherwise, <code>false</code>.
     */
    boolean isActive(ILanguage language);

    /**
     * Activates a language.
     *
     * If the language is already active, nothing happens.
     *
     * @param language The language to activate.
     */
    void activate(ILanguage language);

    /**
     * Deactivates a language.
     *
     * If the language is already inactive, nothing happens.
     *
     * @param language The language to deactivate.
     */
    void deactivate(ILanguage language);

    /**
     * Activates the given languages.
     *
     * If the language is already active, nothing happens.
     *
     * @param languages The languages to activate.
     */
    void activateRange(Iterable<ILanguage> languages);

    /**
     * Deactivates the given languages.
     *
     * If the language is already inactive, nothing happens.
     *
     * @param languages The languages to deactivate.
     */
    void deactivateRange(Iterable<ILanguage> languages);

    /**
     * Gets the {@link ILanguage} object that corresponds to the
     * specified {@link MetaborgIdeaLanguage} object.
     *
     * @param language The Spoofax IDEA language.
     * @return The associated {@link ILanguage}.
     */
    ILanguage getLanguage(MetaborgIdeaLanguage language);

}
