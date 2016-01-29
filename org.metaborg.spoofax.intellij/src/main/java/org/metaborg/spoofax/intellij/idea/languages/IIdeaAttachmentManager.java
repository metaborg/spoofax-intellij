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
import org.metaborg.core.language.ILanguageImpl;

/**
 * Creates and caches {@link IdeaLanguageAttachment}
 * and {@link IdeaLanguageImplAttachment} objects.
 */
public interface IIdeaAttachmentManager {

    /**
     * Gets the {@link IdeaLanguageAttachment} for a particular language.
     * <p>
     * If no {@link IdeaLanguageAttachment} exists yet for the language,
     * a new  {@link IdeaLanguageAttachment} is created and cached.
     *
     * @param language The language.
     * @return The corresponding {@link IdeaLanguageAttachment}.
     */
    IdeaLanguageAttachment get(ILanguage language);

    /**
     * Gets the {@link IdeaLanguageImplAttachment} for a particular language implementation.
     * <p>
     * If no {@link IdeaLanguageImplAttachment} exists yet for the language implementation,
     * a new  {@link IdeaLanguageImplAttachment} is created and cached.
     *
     * @param implementation The language implementation.
     * @return The corresponding {@link IdeaLanguageImplAttachment}.
     */
    IdeaLanguageImplAttachment get(ILanguageImpl implementation);
}
