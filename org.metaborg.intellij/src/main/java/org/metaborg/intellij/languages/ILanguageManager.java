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

import com.google.common.collect.*;
import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;

import java.io.*;
import java.util.Collection;

/**
 * Manages loaded languages.
 *
 * A loaded language is a language that is known to Metaborg Core.
 *
 * The implementation must be thread-safe.
 */
public interface ILanguageManager {

    /**
     * Gets a collection of all loaded languages.
     *
     * @return A collection of all loaded languages.
     */
    Collection<ILanguage> getLoadedLanguages();

    /**
     * Loads a language component with the specified identifier.
     *
     * This uses the installed language sources to discover the language.
     * The loaded languages are not automatically activated.
     *
     * @param id The language identifier of the language component to load.
     * @return The loaded language components, or an empty list if none where
     * discovered or if no language source knows the
     * language identifier.
     */
    Collection<ILanguageComponent> discover(LanguageIdentifier id)
            throws LanguageLoadingFailedException;

    /**
     * Loads a language component from the specified language discovery request.
     *
     * The loaded languages are not automatically activated.
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
     * Loads language component from the specified language discovery requests.
     *
     * This uses the installed language sources to discover the language.
     * The loaded languages are not automatically activated.
     *
     * @param ids The language identifiers of the language components to load.
     * @return The loaded language components.
     */
    Collection<ILanguageComponent> discoverRange(Iterable<LanguageIdentifier> ids)
            throws LanguageLoadingFailedException;

    /**
     * Loads language component from the specified language discovery requests.
     *
     * The loaded languages are not automatically activated.
     *
     * @param requests The requests whose language components to load.
     * @return The loaded language components.
     */
    Collection<ILanguageComponent> loadRange(Iterable<ILanguageDiscoveryRequest> requests)
            throws LanguageLoadingFailedException;

    /**
     * Unloads language components.
     *
     * The components are not automatically deactivated.
     *
     * @param components The components to unload.
     */
    void unloadRange(Iterable<ILanguageComponent> components);

}
