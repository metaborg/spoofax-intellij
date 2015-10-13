package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.style.IStyle;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Highlighter for Spoofax languages.
 */
public final class SpoofaxSyntaxHighlighter extends SyntaxHighlighterBase {

    @NotNull
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    private final Map<IStyle, TextAttributesKey[]> styleMap = new HashMap<>();
    @NotNull
    private final Lexer lexer;

    /**
     * Initializes a new instance of the {@link SpoofaxSyntaxHighlighter} class.
     *
     * @param lexer The lexer to use for highlighting.
     */
    public SpoofaxSyntaxHighlighter(@NotNull final Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Gets the highlighting lexer.
     *
     * @return The highlighting lexer.
     */
    @NotNull
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
    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(@NotNull final IElementType tokenType) {
        if (!(tokenType instanceof SpoofaxTokenType))
            return EMPTY_KEYS;

        // TODO: Use fixed categorized styles, so we don't have to use createTextAttributesKey.
        return getTextAttributesKeyForStyle(((SpoofaxTokenType) tokenType).getStyle());
    }

    /**
     * Gets the text attributes for the specified style.
     *
     * @param style The style.
     * @return The text attributes.
     */
    @NotNull
    private TextAttributesKey[] getTextAttributesKeyForStyle(@NotNull final IStyle style) {
        TextAttributesKey[] attributes = this.styleMap.getOrDefault(style, null);
        if (attributes == null) {
            String name = "STYLE_" + style.hashCode();
            TextAttributesKey attribute = createTextAttributesKey(name, new TextAttributes(
                    style.color(),
                    style.backgroundColor(),
                    null,
                    (style.underscore() ? EffectType.LINE_UNDERSCORE : null),
                    (style.bold() ? Font.BOLD : Font.PLAIN)
                            + (style.italic() ? Font.ITALIC : Font.PLAIN)));
            attributes = new TextAttributesKey[]{attribute};

            this.styleMap.put(style, attributes);
        }
        return attributes;
    }
}
