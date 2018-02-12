package com.virtlink.editorservices.intellij.syntaxcoloring

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.virtlink.editorservices.ScopeNames

/**
 * Manages the token scope names.
 *
 * A single scope name, which is an identifier of the form `a.b.c.d`, determines the coloring and style
 * of tokens.  For example, `entity.name.function.java` is the scope name for the name of a function in Java.
 * However, the scope manager could not possibly know all the possible scope names for all the possible languages.
 * Therefore, scope names get more specific with each additional identifier, i.e. `a` is the most general scope and
 * `a.b.c.d.e.f` is a very specific scope.  The scope manager will try to find a coloring and style for the most
 * specific scope name it knows, such as `a.b.c`, which is a prefix of the actual scope name.  For example, the scope
 * manager may know how to style `entity.name` in general, and apply that whenever it sees `entity.name.function.java`.
 *
 * As a special case, exact coloring and styling can be specified in a scope name.  They start with a dot (`.`),
 * followed by a sequence of tags, each separated by an underscore.  Each tag specifies a property of the exact coloring
 * and styling.  The following tags are defined:
 *
 *     FC#RRGGBB       (foreground color #RRGGBB)
 *     BC#RRGGBB       (background color #RRGGBB)
 *     B               (bold)
 *     I               (italic)
 *     U               (underline)
 *     S               (strikethrough)
 *
 * For example, to color text red, bold and italic:
 *
 *     .FC#FF0000_B_I
 *
 */
class TokenScopeManager {

    val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    val DEFAULT_SCOPE = "text"
    private val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("AESI_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    private val styleScopes = listOf(
            // Put more specific scopes (longer prefixes) before more general scopes (shorter prefixes).
            // See https://manual.macromates.com/en/language_grammars for details about these scopes.
            createScopeStyle("text.whitespace", HighlighterColors.TEXT),     // TODO: Whitespace
            createScopeStyle("text", HighlighterColors.TEXT),
            createScopeStyle("source", HighlighterColors.TEXT),
            // Comments
            createScopeStyle("comment.line", DefaultLanguageHighlighterColors.LINE_COMMENT),
            createScopeStyle("comment.block", DefaultLanguageHighlighterColors.BLOCK_COMMENT),
            createScopeStyle("comment.block.documentation", DefaultLanguageHighlighterColors.DOC_COMMENT),
            createScopeStyle("comment", DefaultLanguageHighlighterColors.BLOCK_COMMENT),
            // Constants
            createScopeStyle("constant.numeric", DefaultLanguageHighlighterColors.NUMBER),
            createScopeStyle("constant.character.escape", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE),
            createScopeStyle("constant.character", DefaultLanguageHighlighterColors.STRING),
            createScopeStyle("constant", DefaultLanguageHighlighterColors.CONSTANT),
            // Entities
            createScopeStyle("entity.name.function", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION),
            createScopeStyle("entity.name.type", DefaultLanguageHighlighterColors.CLASS_NAME),
            createScopeStyle("entity.name", DefaultLanguageHighlighterColors.CLASS_NAME),
            createScopeStyle("entity.other.inherited-class", DefaultLanguageHighlighterColors.CLASS_NAME),
            createScopeStyle("entity.other.attribute-name", DefaultLanguageHighlighterColors.CLASS_NAME),
            createScopeStyle("entity.other", DefaultLanguageHighlighterColors.CLASS_NAME),
            // Invalid
            createScopeStyle("invalid.illegal", BAD_CHARACTER),
            createScopeStyle("invalid.deprecated", HighlighterColors.TEXT),    // TODO: Strikethrough text
            createScopeStyle("invalid", HighlighterColors.TEXT),    // TODO: Red text
            // Keywords
            createScopeStyle("keyword.operator", DefaultLanguageHighlighterColors.OPERATION_SIGN),
            createScopeStyle("keyword.control", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("keyword", DefaultLanguageHighlighterColors.KEYWORD),
            // Markup
            createScopeStyle("markup.underline.link", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("markup.underline", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("markup.bold", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("markup.italic", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("markup.list", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("markup.quote", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("markup.raw", DefaultLanguageHighlighterColors.KEYWORD),
            // Meta
            createScopeStyle("meta.braces", DefaultLanguageHighlighterColors.BRACES),
            createScopeStyle("meta.parens", DefaultLanguageHighlighterColors.PARENTHESES),
            createScopeStyle("meta.brackets", DefaultLanguageHighlighterColors.BRACKETS),
            createScopeStyle("meta.generic", DefaultLanguageHighlighterColors.BRACKETS),
            // Punctuation
            createScopeStyle("punctuation.separator", DefaultLanguageHighlighterColors.COMMA),
            createScopeStyle("punctuation.terminator", DefaultLanguageHighlighterColors.SEMICOLON),
            createScopeStyle("punctuation.accessor", DefaultLanguageHighlighterColors.DOT),
            // Storage
            createScopeStyle("storage.type", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("storage.modifier", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("storage", DefaultLanguageHighlighterColors.KEYWORD),
            // Strings
            createScopeStyle("string.quoted.single", DefaultLanguageHighlighterColors.STRING),
            createScopeStyle("string.quoted.double", DefaultLanguageHighlighterColors.STRING),
            createScopeStyle("string.quoted.triple", DefaultLanguageHighlighterColors.STRING),
            createScopeStyle("string.quoted", DefaultLanguageHighlighterColors.STRING),
            createScopeStyle("string.unquoted", DefaultLanguageHighlighterColors.STRING),
            createScopeStyle("string.interpolated", DefaultLanguageHighlighterColors.STRING),
            createScopeStyle("string.regexp", DefaultLanguageHighlighterColors.STRING),
            createScopeStyle("string", DefaultLanguageHighlighterColors.STRING),
            // Support
            createScopeStyle("support.function", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL),
            createScopeStyle("support.class", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL),
            createScopeStyle("support.type", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL),
            createScopeStyle("support.constant", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL),
            createScopeStyle("support.variable", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL),
            createScopeStyle("support", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL),
            // Variables
            createScopeStyle("variable.parameter", DefaultLanguageHighlighterColors.PARAMETER),
            createScopeStyle("variable.language", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("variable", DefaultLanguageHighlighterColors.IDENTIFIER)
    )

    fun getSimplifiedScope(scopes: ScopeNames): String {
        return this.styleScopes
                .map { (prefix, _) -> prefix }
                .firstOrNull { it in scopes }
                ?: DEFAULT_SCOPE
    }

    fun getTokenHighlights(scope: String): Array<TextAttributesKey> {
        return this.styleScopes
                .filter { (prefix, _) -> scope.startsWith(prefix, true) }
                .map { (_, style) -> style }
                .firstOrNull() ?: EMPTY_KEYS
    }

    private fun createScopeStyle(prefix: String, style: TextAttributesKey): Pair<String, Array<TextAttributesKey>> {
        return Pair(prefix, arrayOf(TextAttributesKey.createTextAttributesKey(createScopeName(prefix), style)))
    }

    private fun createScopeName(prefix: String): String {
        return "AESI_" + prefix.toUpperCase().replace('.', '_')
    }
}