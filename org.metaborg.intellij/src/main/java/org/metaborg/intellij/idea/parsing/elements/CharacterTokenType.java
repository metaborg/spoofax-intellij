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

package org.metaborg.intellij.idea.parsing.elements;

import com.intellij.psi.tree.*;
import org.metaborg.intellij.idea.languages.*;

/**
 * Token type for a single character.
 * <p>
 * This is used by the character lexer, see {@link org.metaborg.spoofax.intellij.factories.ICharacterLexerFactory}.
 */
public final class CharacterTokenType extends IElementType {

    /**
     * Initializes a new instance of the {@link CharacterTokenType} class.
     *
     * @param language The associated IDEA language.
     */
    public CharacterTokenType(final MetaborgIdeaLanguage language) {
        super("CHARACTER", language);
    }

}
