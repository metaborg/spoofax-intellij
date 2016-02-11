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
import org.jetbrains.annotations.*;
import org.metaborg.intellij.idea.languages.*;

/**
 * A Spoofax element type.
 */
public class SpoofaxElementType extends IElementType {

    /**
     * Initializes a new instance of the {@link SpoofaxElementType} class.
     *
     * @param language The language.
     */
    public SpoofaxElementType(
            @Nullable final MetaborgIdeaLanguage language) {
        this(language, "SPOOFAX_ELEMENT_TYPE");
    }

    /**
     * Initializes a new instance of the {@link SpoofaxElementType} class.
     *
     * @param language  The language.
     * @param debugName The debug name.
     */
    protected SpoofaxElementType(
            @Nullable final MetaborgIdeaLanguage language,
            @NonNls final String debugName) {
        super(debugName, language);
    }

    /**
     * Gets the {@link MetaborgIdeaLanguage} of this element type.
     *
     * @return The {@link MetaborgIdeaLanguage}.
     */
    @Nullable
    public MetaborgIdeaLanguage getMetaborgIdeaLanguage() { return (MetaborgIdeaLanguage)getLanguage(); }

}
