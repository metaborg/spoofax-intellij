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

package org.metaborg.core.project.settings;

import org.metaborg.core.language.LanguageContributionIdentifier;
import org.metaborg.core.language.LanguageIdentifier;

/**
 * Settings for a project.
 */
public interface IProjectSettings {

    /**
     * Gets the identifier of the project's language.
     *
     * @return The language identifier.
     */
    LanguageIdentifier identifier();

    /**
     * Gets the name of the project.
     *
     * @return The name.
     */
    String name();

    /**
     * Gets the compile dependencies.
     *
     * @return An iterable of compile dependency identifiers.
     */
    Iterable<LanguageIdentifier> compileDependencies();

    /**
     * Gets the runtime dependencies.
     *
     * @return An iterable of runtime dependency identifiers.
     */
    Iterable<LanguageIdentifier> runtimeDependencies();

    /**
     * Gets the language contributions.
     *
     * @return An iterable of language contribution identifiers.
     */
    Iterable<LanguageContributionIdentifier> languageContributions();

}
