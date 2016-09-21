/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.parsing.elements;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.idea.languages.MetaborgIdeaLanguage;

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
