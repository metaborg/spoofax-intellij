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

package org.metaborg.spoofax.intellij.factories;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.idea.languages.OldSpoofaxTokenType;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;

/**
 * Factory for parsers.
 */
public interface IParserFactory {

    /**
     * Creates a new parser.
     *
     * @param language          The language.
     * @param tokenTypesManager The token type manager.
     * @return The created parser.
     */
    @NotNull
    PsiParser create(@NotNull ILanguage language, @NotNull SpoofaxTokenTypeManager tokenTypesManager);
}
