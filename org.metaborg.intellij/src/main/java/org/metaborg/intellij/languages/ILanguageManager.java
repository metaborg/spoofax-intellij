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

package org.metaborg.intellij.languages;

import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.languages.*;

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
    void unloadComponentRange(Iterable<ILanguageComponent> components);

    /**
     * Unloads language implementations.
     *
     * The implementations are not automatically deactivated.
     *
     * @param implementations The implementations to unload.
     */
    void unloadImplRange(Iterable<? extends ILanguageImpl> implementations);

    /**
     * Unloads languages.
     *
     * The languages are not automatically deactivated.
     *
     * @param languages The languages to unload.
     */
    void unloadRange(Iterable<ILanguage> languages);
}
