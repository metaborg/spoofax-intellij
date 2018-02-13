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
import org.metaborg.core.style.*;
import org.metaborg.intellij.idea.languages.SpoofaxIdeaLanguage;

/**
 * The type of a Spoofax token.
 */
public final class SpoofaxTokenType extends IElementType {

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
    public final IStyle getStyle() { return this.style; }

    @Override
    public String toString() {
        return SpoofaxTokenType.class.getName() + "." + super.toString() + "." + this.style.toString();
    }

}
