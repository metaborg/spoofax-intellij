package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.style.IStyle;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SpoofaxSyntaxHighlighter extends SyntaxHighlighterBase {

    private final Map<IStyle, TextAttributesKey[]> styleMap = new HashMap<>();

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

//    private final SpoofaxParserDefinition parserDefinition;
    private final Lexer lexer;

    public SpoofaxSyntaxHighlighter(Lexer lexer)
    {
//        this.parserDefinition = parserDefinition;
        this.lexer = lexer;
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
//        // TODO: Project!
//        final ILanguageImpl implementation = this.languageIdentifierService.identify(this.fileType.getSpoofaxLanguage(),
//                                                                                     null);
//        return this.lexerParserManager.getHighlightingLexer(implementation);
//
//        return parserDefinition.createLexer(this.project);
        return this.lexer;
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (!(tokenType instanceof SpoofaxTokenType))
            return EMPTY_KEYS;

        return getTextAttributesKeyForStyle(((SpoofaxTokenType)tokenType).getStyle());
    }

    private TextAttributesKey[] getTextAttributesKeyForStyle(IStyle style)
    {
        TextAttributesKey[] attributes = this.styleMap.getOrDefault(style, null);
        if (attributes == null)
        {
            String name = "STYLE_" + style.hashCode();
            TextAttributesKey attribute = createTextAttributesKey(name, new TextAttributes(
                    style.color(),
                    style.backgroundColor(),
                    null,
                    (style.underscore() ? EffectType.LINE_UNDERSCORE : null),
                    (style.bold() ? Font.BOLD : Font.PLAIN)
                            + (style.italic() ? Font.ITALIC : Font.PLAIN)));
            attributes = new TextAttributesKey[] { attribute };

            this.styleMap.put(style, attributes);
        }
        return attributes;
    }
}
