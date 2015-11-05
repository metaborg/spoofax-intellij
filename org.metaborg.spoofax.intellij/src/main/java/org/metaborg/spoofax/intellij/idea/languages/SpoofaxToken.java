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

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.IntRange;

/**
 * Represents a Spoofax token. This class contains enough information
 * to reconstruct the Spoofax AST.
 */
public final class SpoofaxToken {

    @NotNull
    public final SpoofaxTokenType type;
    @NotNull
    public final IntRange range;

    /**
     * Initializes a new instance of the {@link SpoofaxToken} class.
     *
     * @param type  The token type.
     * @param range The range of the token.
     */
    public SpoofaxToken(@NotNull final SpoofaxTokenType type, @NotNull final IntRange range) {
        this.type = type;
        this.range = range;
    }

    @Override
    public String toString() {
        return type.toString() + " [" + range.start + ", " + range.end + ")";
    }

}