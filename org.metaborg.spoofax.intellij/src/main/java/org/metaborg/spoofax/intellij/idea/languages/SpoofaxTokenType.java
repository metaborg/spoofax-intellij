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

/**
 * The type of a Spoofax token.
 */
public final class SpoofaxTokenType extends IElementType {

    @NotNull
    private final IStyle style;

    /**
     * Initializes a new instance of the {@link SpoofaxTokenType} class.
     *
     * @param language The associated IDEA language.
     * @param style    The token style.
     */
    public SpoofaxTokenType(final SpoofaxIdeaLanguage language, final IStyle style) {
        super(style.toString(), language);
        this.style = style;
    }

    /**
     * Gets the style associated with this token type.
     *
     * @return The token style.
     */
    @NotNull
    public final IStyle getStyle() { return this.style; }

    @Override
    public String toString() {
        return SpoofaxTokenType.class.getName() + "." + super.toString() + "." + this.style.toString();
    }

}
