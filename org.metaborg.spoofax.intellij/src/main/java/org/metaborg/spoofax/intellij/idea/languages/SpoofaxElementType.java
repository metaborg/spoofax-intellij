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

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.style.IStyle;
import org.metaborg.core.syntax.ParseResult;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * The type of a Spoofax element.
 */
public final class SpoofaxElementType extends IElementType {

    /**
     * Initializes a new instance of the {@link SpoofaxElementType} class.
     *
     * @param language The associated IDEA language.
     */
    public SpoofaxElementType(@NotNull final SpoofaxIdeaLanguage language) {
        super("SPOOFAX_ELEMENT_TYPE", language);
    }

    @Override
    public String toString() {
        return SpoofaxElementType.class.getName() + "." + super.toString();
    }

}
