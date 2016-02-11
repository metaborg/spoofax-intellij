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

import com.google.common.cache.*;
import org.metaborg.core.style.*;
import org.metaborg.intellij.idea.languages.*;

import javax.annotation.*;
import java.awt.*;

/**
 * Tracks token types.
 * <p>
 * IntelliJ can't handle too many token types. However, we'll still need a different token type for
 * each different style. This class returns a token type that matches the style, if found; otherwise,
 * creates a new token type and stores it for re-use.
 *
 * The token type manager is specific to a single language.
 */
public final class SpoofaxTokenTypeManager {

    private final LoadingCache<IStyle, SpoofaxTokenType> tokenCache;
    private final MetaborgIdeaLanguage language;
    private final CharacterTokenType characterTokenType;
    private final SpoofaxElementType elementType;
    private final MetaborgIdentifierElementType identifierType;

    /**
     * Initializes a new instance of the {@link SpoofaxTokenTypeManager} class.
     *
     * @param language The IDEA language.
     */
    public SpoofaxTokenTypeManager(final MetaborgIdeaLanguage language) {
        this.language = language;
        this.characterTokenType = new CharacterTokenType(language);
        this.elementType = new SpoofaxElementType(language);
        this.identifierType = new MetaborgIdentifierElementType(language);

        this.tokenCache = CacheBuilder.newBuilder().weakValues()
            .build(new CacheLoader<IStyle, SpoofaxTokenType>() {
                @Override
                public SpoofaxTokenType load(final IStyle style)
                        throws Exception {
                    return new SpoofaxTokenType(language, style);
                }
            });
    }

    /**
     * Returns a special token type used to represent single character tokens.
     *
     * @return The token type.
     */
    public CharacterTokenType getCharacterTokenType() {
        return this.characterTokenType;
    }

    // TODO: Documentation
    public SpoofaxElementType getElementType() { return this.elementType; }

    // TODO: Documentation
    public MetaborgIdentifierElementType getIdentifierType() { return this.identifierType; }

    /**
     * Returns the token type corresponding to the specified style.
     *
     * This method is thread-safe.
     *
     * @param style The style of the token type. The returned token type will have this style.
     *              The style may be <code>null</code>, in which case the default token style is assumed.
     * @return The token type.
     */
    public SpoofaxTokenType getTokenType(@Nullable IStyle style) {
        style = style != null ? style : getDefaultStyle();

        return this.tokenCache.getUnchecked(style);
    }

    /**
     * Gets the default style when no style is specified.
     *
     * @return The default style.
     */
    public IStyle getDefaultStyle() {
        // FIXME: Get this from the IStylerService?
        return new Style(Color.black, Color.white, false, false, false);
    }
}