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

import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;
import org.metaborg.spoofax.intellij.idea.psi.ATermAstElementTypeProvider;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Factory for ATerm AST element type providers.
 */
public interface IATermAstElementTypeProviderFactory {

    /**
     * Creates a new ATerm AST element type provider.
     *
     * @param language         The language implementation.
     * @param parseResult      The parse result.
     * @param tokenTypeManager The token type manager.
     * @return The created ATerm AST element type providers.
     */
    ATermAstElementTypeProvider create(
            ILanguageImpl language,
            ParseResult<IStrategoTerm> parseResult,
            SpoofaxTokenTypeManager tokenTypeManager);

}
