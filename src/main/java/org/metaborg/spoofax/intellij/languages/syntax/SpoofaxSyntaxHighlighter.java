package org.metaborg.spoofax.intellij.languages.syntax;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.style.IStyle;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SpoofaxSyntaxHighlighter extends SyntaxHighlighterBase {

    private final Map<IStyle, TextAttributesKey[]> styleMap = new HashMap<>();

    //public static final TextAttributesKey SEPARATOR = createTextAttributesKey("SIMPLE_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    //public static final TextAttributesKey KEY = createTextAttributesKey("SIMPLE_KEY", new TextAttributes(Color.orange, null, null, null, Font.BOLD));
    //public static final TextAttributesKey VALUE = createTextAttributesKey("SIMPLE_VALUE", new TextAttributes(Color.blue, null, null, null, Font.PLAIN));
    //public static final TextAttributesKey KEY = createTextAttributesKey("SIMPLE_KEY", DefaultLanguageHighlighterColors.KEYWORD);
    //public static final TextAttributesKey VALUE = createTextAttributesKey("SIMPLE_VALUE", DefaultLanguageHighlighterColors.STRING);
    //public static final TextAttributesKey COMMENT = createTextAttributesKey("SIMPLE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

    //static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("SIMPLE_BAD_CHARACTER", new TextAttributes(Color.RED, null, null, null, Font.BOLD));

    //private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    //private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
    //private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[]{KEY};
    //private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[]{VALUE};
    //private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    private final SpoofaxParserDefinition parserDefinition;
    private final Project project;

    public SpoofaxSyntaxHighlighter(SpoofaxParserDefinition parserDefinition, Project project)
    {
        this.parserDefinition = parserDefinition;
        this.project = project;
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return parserDefinition.createLexer(this.project);
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
