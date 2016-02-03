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
import org.metaborg.core.style.IStyle;
import org.metaborg.core.style.Style;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxElementType;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxIdentifierType;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Tracks token types.
 * <p>
 * IntelliJ can't handle too many token types. However, we'll still need a different token type for each different
 * style. This class returns a token type that matches the style, if found; otherwise, creates a new token type and
 * stores it for re-use.
 */
public final class SpoofaxTokenTypeManager {

    private final Map<IStyle, SpoofaxTokenType> map = new HashMap<>();
    private final SpoofaxIdeaLanguage language;
    private final CharacterTokenType characterTokenType;
    private final SpoofaxElementType elementType;
    private final SpoofaxIdentifierType identifierType;

    /**
     * Initializes a new instance of the {@link SpoofaxTokenTypeManager} class.
     *
     * @param language The IDEA language.
     */
    public SpoofaxTokenTypeManager(final SpoofaxIdeaLanguage language) {
        this.language = language;
        this.characterTokenType = new CharacterTokenType(language);
        this.elementType = new SpoofaxElementType(language);
        this.identifierType = new SpoofaxIdentifierType(language);
    }

    /**
     * Returns a special token type used to represent single character tokens.
     *
     * @return The token type.
     */
    public CharacterTokenType getCharacterTokenType() {
        return this.characterTokenType;
    }


    public SpoofaxElementType getElementType() { return this.elementType; }

    public SpoofaxIdentifierType getIdentifierType() { return this.identifierType; }

    /**
     * Returns the token type corresponding to the specified style.
     *
     * @param style The style of the token type. The returned token type will have this style. The style may be null,
     *              in which case the default token style is assumed.
     * @return The token type.
     */
    public SpoofaxTokenType getTokenType(@Nullable IStyle style) {
        style = style != null ? style : getDefaultStyle();

        SpoofaxTokenType token = this.map.getOrDefault(style, null);
        if (token == null) {
            token = new SpoofaxTokenType(this.language, style);
            this.map.put(style, token);
        }
        assert token != null;
        assert token.getStyle().equals(style);

        return token;
    }

    /**
     * Gets the default style when no style is specified.
     *
     * @return The default style.
     */
    public IStyle getDefaultStyle() {
        // TODO: Get this from the IStylerService?
        return new Style(Color.black, Color.white, false, false, false);
    }
}