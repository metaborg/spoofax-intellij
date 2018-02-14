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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.metaborg.core.style.*;
import org.metaborg.intellij.idea.languages.SpoofaxIdeaLanguage;

import javax.annotation.Nullable;
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
    private final SpoofaxIdeaLanguage language;
    private final CharacterTokenType characterTokenType;
    private final SpoofaxElementType elementType;
    private final MetaborgIdentifierElementType identifierType;

    /**
     * Initializes a new instance of the {@link SpoofaxTokenTypeManager} class.
     *
     * @param language The IDEA language.
     */
    public SpoofaxTokenTypeManager(final SpoofaxIdeaLanguage language) {
        this.language = language;
        this.characterTokenType = new CharacterTokenType(language);
        this.elementType = new SpoofaxElementType(language);
        this.identifierType = new MetaborgIdentifierElementType(language);

        this.tokenCache = CacheBuilder.newBuilder().weakValues()
            .build(new CacheLoader<IStyle, SpoofaxTokenType>() {
                @Override
                public SpoofaxTokenType load(final IStyle style) {
                    return new SpoofaxTokenType(language, style);
                }
            });
    }

    /**
     * Returns a special token type used to represent single character tokens.
     *
     * @return The token type.
     */
    @Deprecated
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
    @Deprecated
    public SpoofaxTokenType getTokenType(@Nullable IStyle style) {
        style = style != null ? style : getDefaultStyle();

        return this.tokenCache.getUnchecked(style);
    }

    /**
     * Gets the default style when no style is specified.
     *
     * @return The default style.
     */
    @Deprecated
    public IStyle getDefaultStyle() {
        // FIXME: Get this from the IStylerService?
        return new Style(Color.black, Color.white, false, false, false, false);
    }
}