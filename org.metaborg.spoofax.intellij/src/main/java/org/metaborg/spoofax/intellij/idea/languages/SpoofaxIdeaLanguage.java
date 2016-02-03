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

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;

/**
 * A Spoofax language used in IntelliJ IDEA.
 * <p>
 * There are no implementations of this class because it's instantiated dynamically.
 */
public abstract class SpoofaxIdeaLanguage extends Language {

    private final ILanguage language;

    /**
     * Initializes a new instance of the {@link SpoofaxIdeaLanguage} class.
     *
     * @param language The language.
     */
    protected SpoofaxIdeaLanguage(final ILanguage language) {
        super(language.name());

        this.language = language;
    }

    /**
     * Gets the associated language.
     *
     * @return The associated language.
     */
    public final ILanguage language() {
        return this.language;
    }
}
