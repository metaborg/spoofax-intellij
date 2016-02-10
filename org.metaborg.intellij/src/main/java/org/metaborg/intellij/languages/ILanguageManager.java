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

package org.metaborg.intellij.languages;

import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryRequest;

import java.util.Collection;

/**
 * Manages loaded and activated languages.
 *
 * A loaded language is a language that is known to Metaborg Core.
 * An activated language is a language whose effects (syntax highlighting,
 * transformations, parsing) are visible in IntelliJ IDEA.
 *
 * The implementation must be thread-safe.
 */
public interface ILanguageManager {
    /**
     * Gets a collection of all loaded languages.
     *
     * @return A collection of all loaded languages.
     */
    Collection<ILanguage> getAllLanguages();

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
     * Determines whether the specified language can be activated by this manager.
     *
     * One reason why a language might not be activatable is that it's actually
     * a library and not a language (such as the Spoofax runtime library).
     *
     * The result of this method does not depend on whether the language is already activated.
     *
     * @param language The language to test.
     * @return <code>true</code> when the language can be activated;
     * otherwise, <code>false</code>.
     */
    boolean canActivate(ILanguage language);

    /**
     * Loads a language component from the specified language discovery request.
     *
     * The loaded languages are not automatically acivated.
     *
     * @param request The request whose language component to load.
     * @return The loaded language component.
     */
    ILanguageComponent load(ILanguageDiscoveryRequest request)
            throws LanguageLoadingFailedException;

    /**
     * Unloads a language component.
     *
     * A language component can only be unloaded when the languages to which
     * it contributes are not active.
     *
     * @param component The language component to unload.
     */
    void unload(ILanguageComponent component);

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

}
