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

package org.metaborg.intellij.idea.languages;

import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.languages.*;

import java.io.*;
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

    /**
     * Requests languages from a language artifact.
     *
     * @param artifact The artifact file.
     * @return The language discovery requests.
     */
    Iterable<ILanguageDiscoveryRequest> requestFromArtifact(final VirtualFile artifact) throws IOException;

    /**
     * Requests languages from a language artifact.
     *
     * @param artifact The artifact file.
     * @return The language discovery requests.
     */
    Iterable<ILanguageDiscoveryRequest> requestFromArtifact(final FileObject artifact) throws IOException;

    /**
     * Requests languages from a folder.
     *
     * @param folder The folder.
     * @return The language discovery requests.
     */
    Iterable<ILanguageDiscoveryRequest> requestFromFolder(final VirtualFile folder) throws IOException;

    /**
     * Requests languages from a folder.
     *
     * @param folder The folder.
     * @return The language discovery requests.
     */
    Iterable<ILanguageDiscoveryRequest> requestFromFolder(final FileObject folder) throws IOException;

    /**
     * Reloads the languages of the specified language specification project.
     *
     * @param project The project.
     */
    void reloadLanguageSpec(final IdeaLanguageSpec project);

    /**
     * Unloads the languages of the specified language specification project.
     *
     * @param project The project.
     */
    void unloadLanguageSpec(final IdeaLanguageSpec project);

    /**
     * Loads the languages of the specified language specification project.
     *
     * @param project The project.
     */
    void loadLanguageSpec(final IdeaLanguageSpec project);

}
