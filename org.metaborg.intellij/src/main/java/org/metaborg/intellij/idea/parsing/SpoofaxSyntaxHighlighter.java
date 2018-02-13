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

package org.metaborg.intellij.idea.parsing;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.metaborg.core.style.*;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenType;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Highlighter for Spoofax languages.
 */
@Deprecated
public final class SpoofaxSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
    private final Map<IStyle, TextAttributesKey[]> styleMap = new HashMap<>();
    private final Lexer lexer;

    /**
     * Initializes a new instance of the {@link SpoofaxSyntaxHighlighter} class.
     *
     * @param lexer The lexer to use for highlighting.
     */
    public SpoofaxSyntaxHighlighter(final Lexer lexer) {
        super();
        this.lexer = lexer;
    }

    /**
     * Gets the highlighting lexer.
     *
     * @return The highlighting lexer.
     */
    @Override
    public Lexer getHighlightingLexer() {
        return this.lexer;
    }

    /**
     * Gets the highlights for the specified token type.
     *
     * @param tokenType The token type.
     * @return The text attributes for the token type.
     */
    @Override
    public TextAttributesKey[] getTokenHighlights(final IElementType tokenType) {
        if (!(tokenType instanceof SpoofaxTokenType))
            return EMPTY_KEYS;

        // FIXME: Use fixed categorized styles, so we don't have to use createTextAttributesKey.
        return getTextAttributesKeyForStyle(((SpoofaxTokenType)tokenType).getStyle());
    }

    /**
     * Gets the text attributes for the specified style.
     *
     * @param style The style.
     * @return The text attributes.
     */
    private TextAttributesKey[] getTextAttributesKeyForStyle(final IStyle style) {
        @Nullable TextAttributesKey[] attributes = this.styleMap.getOrDefault(style, null);
        if (attributes == null) {
            final String name = "STYLE_" + style.hashCode();
            @SuppressWarnings("deprecation") final TextAttributesKey attribute = createTextAttributesKey(
                    name,
                    new TextAttributes(
                            style.color(),
                            style.backgroundColor(),
                            null,
                            (style.underscore() ? EffectType.LINE_UNDERSCORE : null),
                            (style.bold() ? Font.BOLD : Font.PLAIN)
                                    + (style.italic() ? Font.ITALIC : Font.PLAIN)
                    )
            );
            attributes = new TextAttributesKey[]{attribute};

            this.styleMap.put(style, attributes);
        }
        return attributes;
    }
}
