package com.virtlink.editorservices.intellij.syntaxcoloring

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.ui.LayeredIcon
import com.intellij.ui.RowIcon
import com.intellij.util.PlatformIcons
import com.intellij.util.SmartList
import com.intellij.util.ui.EmptyIcon
import com.virtlink.editorservices.ScopeName
import com.virtlink.editorservices.ScopeNames
import java.awt.Color
import java.awt.Font
import java.util.*
import javax.swing.Icon

class ScopeNamesManager: IScopeNamesManager {

    private val EMPTY_KEYS = emptyArray<TextAttributesKey>()

    override fun getTextAttributes(scopes: ScopeNames): Array<TextAttributesKey> {
        // When no scope names are defined, return no text attributes.
        if (scopes == ScopeNames()) return EMPTY_KEYS

        // NOTE: The first scope name produces the first text attribute,
        // whose values are overridden by later scope names.
        return scopes
            .mapNotNull { getTextAttribute(it) }
            .toTypedArray()
    }

    override fun getIcon(scopes: ScopeNames): Icon? {
        val kindIcon = getKindIcon(scopes)
        val visibilityIcon = getVisibilityIcon(scopes)
        val baseIcon = getBaseIcon(kindIcon, scopes)

        if (baseIcon == null && visibilityIcon == null)
            return null

        val resultIcon = RowIcon(2)
        resultIcon.setIcon(baseIcon ?: EmptyIcon.create(PlatformIcons.CLASS_ICON), 0)
        resultIcon.setIcon(visibilityIcon ?: EmptyIcon.create(PlatformIcons.PUBLIC_ICON), 1)
        return resultIcon
    }

    private fun getKindIcon(scopes: ScopeNames): Icon? {
        return scopes.mapNotNull { when {
            "meta.variable" in it -> PlatformIcons.VARIABLE_ICON
            "meta.field" in it -> PlatformIcons.FIELD_ICON
            "meta.property" in it -> PlatformIcons.PROPERTY_ICON
            "meta.function" in it -> PlatformIcons.FUNCTION_ICON
            "meta.method" in it -> if ("meta.abstract" in scopes)
                    PlatformIcons.ABSTRACT_METHOD_ICON
                else
                    PlatformIcons.METHOD_ICON
            "meta.interface" in it -> PlatformIcons.INTERFACE_ICON
            "meta.class" in it -> if ("meta.abstract" in scopes)
                    PlatformIcons.ABSTRACT_CLASS_ICON
                else
                    PlatformIcons.CLASS_ICON
            "meta.exception" in it -> PlatformIcons.EXCEPTION_CLASS_ICON
            "meta.enum" in it -> PlatformIcons.ENUM_ICON
            "meta.annotation" in it -> PlatformIcons.ANNOTATION_TYPE_ICON
            "meta.package" in it -> PlatformIcons.PACKAGE_ICON
            else -> null
        } }.firstOrNull()
    }

    private fun getVisibilityIcon(scopes: ScopeNames): Icon? {
        return scopes.mapNotNull { when {
                "meta.public" in it -> PlatformIcons.PUBLIC_ICON
                "meta.package" in it -> PlatformIcons.PACKAGE_LOCAL_ICON
                "meta.internal" in it -> PlatformIcons.PACKAGE_LOCAL_ICON
                "meta.protected" in it -> PlatformIcons.PROTECTED_ICON
                "meta.private" in it -> PlatformIcons.PRIVATE_ICON
                else -> null
        } }.firstOrNull()
    }

    private fun getBaseIcon(kindIcon: Icon?, scopes: ScopeNames): Icon? {
        if (kindIcon == null) return null

        val iconLayers = SmartList<Icon>()

        scopes.forEach { when {
                "meta.external" in it -> iconLayers.add(PlatformIcons.LOCKED_ICON)
                "meta.excluded" in it -> iconLayers.add(PlatformIcons.EXCLUDED_FROM_COMPILE_ICON)
                "meta.static" in it -> iconLayers.add(AllIcons.Nodes.StaticMark)
        } }

        return if (!iconLayers.isEmpty()) {
            val layeredIcon = LayeredIcon(1 + iconLayers.size)
            layeredIcon.setIcon(kindIcon, 0)
            for (i in 0 until iconLayers.size) {
                layeredIcon.setIcon(iconLayers[i], i + 1)
            }
            layeredIcon
        } else {
            kindIcon
        }
    }

    override fun createLookupElement(label: String, scopes: ScopeNames): LookupElementBuilder {
        val element = LookupElementBuilder.create(label)
        scopes.forEach { applyStyleToLookupElement(element, it) }
        return element
    }

    private fun applyStyleToLookupElement(element: LookupElementBuilder, scope: ScopeName) {
        if (scope.name.startsWith(".")) {
            // Special scope in the form ".FC:#ffcc00.BC
            val scopeTags = scope.name.substring(1).split('.')
            for (scopeTag in scopeTags) {
                val (key, value) = parseScopeTag(scopeTag)
                when (key) {
                    "FC" -> { val color = parseColor(value); if (color != null) element.withItemTextForeground(color) }
                    "B" -> element.withBoldness(true)
                    "U" -> element.withItemTextUnderlined(true)
                    "S" -> element.withStrikeoutness(true)
                    // Other tags are ignored.
                }
            }
        } else when {
            // Put more specific scope names (longer prefixes)
            // before more general scope names (shorter prefixes).
            "meta.not-inherited" in scope -> element.withBoldness(true)
            "invalid.deprecated" in scope -> element.withStrikeoutness(true)
            "invalid" in scope -> element.withItemTextForeground(Color.RED)
            "markup.underline" in scope -> element.withItemTextUnderlined(true)
            "markup.bold" in scope -> element.withBoldness(true)
            // Other scopes are ignored.
        }
    }

    private fun getTextAttribute(scope: ScopeName): TextAttributesKey? {
        if (scope.name.startsWith(".")) {
            // Special scope in the form ".FC:#ffcc00.BC
            var attributes = TextAttributes()
            val scopeTags = scope.name.substring(1).split('.')
            for (scopeTag in scopeTags) {
                val (key, value) = parseScopeTag(scopeTag)
                attributes = TextAttributes.merge(attributes, when (key) {
                    "FC" -> createTextAttribute(foregroundColor = parseColor(value))
                    "BC" -> createTextAttribute(backgroundColor = parseColor(value))
                    "B" -> createTextAttribute(bold = true)
                    "I" -> createTextAttribute(italic = true)
                    "U" -> createTextAttribute(underline = true)
                    "S" -> createTextAttribute(strikeout = true)
                    else -> attributes      // Other tags are ignored.
                })
            }
            return attributes.toKey(createScopeName(scope.name))
        } else {
            // Pre-defined scope
            return this.styleScopes
                    .filter { (prefix, _) -> prefix in scope }
                    .map { (_, style) -> style }
                    .firstOrNull()
        }
    }

    /**
     * Creates a new [TextAttributes] object with the specified style.
     *
     * @param foregroundColor The foreground color of the text; or null.
     * @param backgroundColor The background color of the text; or null.
     * @param bold Whether to make the text bold.
     * @param italic Whether to make the text italic.
     * @param underline Whether to underline the text.
     * @param strikeout Whether to strike out the text.
     * @return The created [TextAttributes] object.
     */
    private fun createTextAttribute(
            foregroundColor: Color? = null,
            backgroundColor: Color? = null,
            bold: Boolean = false,
            italic: Boolean = false,
            underline: Boolean = false,
            strikeout: Boolean = false)
            : TextAttributes{
        val attributes = TextAttributes()
        if (foregroundColor != null) attributes.foregroundColor = foregroundColor
        if (backgroundColor != null) attributes.backgroundColor = backgroundColor
        if (bold) attributes.fontType = attributes.fontType or Font.BOLD
        if (italic) attributes.fontType = attributes.fontType or Font.ITALIC
        if (underline) attributes.effectType = EffectType.LINE_UNDERSCORE
        if (strikeout) attributes.effectType = EffectType.STRIKEOUT
        return attributes
    }

    /**
     * Creates a [TextAttributesKey] for the specified [TextAttributes].
     *
     * @param name The key name.
     * @return The [TextAttributesKey].
     */
    private fun TextAttributes.toKey(name: String): TextAttributesKey {
        // We have to use this deprecated method to get custom colors in our text.
        // IntelliJ instead wants us to use the color scheme colors.
        @Suppress("DEPRECATION")
        return TextAttributesKey.createTextAttributesKey(name, this)
    }

    /**
     * Parses a scope tag of the form `name:value` to their constituent components.
     *
     * @param scopeTag The scope tag.
     * @return The pair of name and value, or just the name.
     */
    private fun parseScopeTag(scopeTag: String): Pair<String, String?> {
        val separator = scopeTag.indexOf(':')
        return if (separator > 0) {
            Pair(scopeTag.substring(0, separator).trim().toUpperCase(Locale.ROOT), scopeTag.substring(separator + 1).trim())
        } else {
            Pair(scopeTag.trim().toUpperCase(Locale.ROOT), null)
        }
    }

    /**
     * Parses a color of the form #RRGGBB to a [Color] object.
     *
     * @param colorString The color string.
     * @return The actual color; or null when parsing failed.
     */
    private fun parseColor(colorString: String?): Color? {
        if (colorString == null || !colorString.startsWith('#') || colorString.length != 7)
            return null
        val r = tryParseHex(colorString.substring(1, 3)) ?: return null
        val g = tryParseHex(colorString.substring(3, 5)) ?: return null
        val b = tryParseHex(colorString.substring(5, 7)) ?: return null
        return Color(r, g, b)
    }

    /**
     * Tries to parse a hexadecimal value.
     *
     * @param s The string with the hexadecimal value.
     * @return The integer value; or null when parsing failed.
     */
    private fun tryParseHex(s: String): Int? {
        return try {
            Integer.parseInt(s, 16)
        } catch (_: NumberFormatException) {
            null
        }
    }

    private val badCharacter = TextAttributesKey.createTextAttributesKey("AESI_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

    private val styleScopes = listOf(
            // Put more specific scope names (longer prefixes) before more general scope names (shorter prefixes).
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
            createScopeStyle("invalid.illegal", badCharacter),
            createScopeStyle("invalid.deprecated", createTextAttribute(strikeout = true)),
            createScopeStyle("invalid", createTextAttribute(foregroundColor = Color.RED)),
            // Keywords
            createScopeStyle("keyword.operator", DefaultLanguageHighlighterColors.OPERATION_SIGN),
            createScopeStyle("keyword.control", DefaultLanguageHighlighterColors.KEYWORD),
            createScopeStyle("keyword", DefaultLanguageHighlighterColors.KEYWORD),
            // Markup
            createScopeStyle("markup.underline.link", createTextAttribute(underline = true)),
            createScopeStyle("markup.underline", createTextAttribute(underline = true)),
            createScopeStyle("markup.bold", createTextAttribute(bold = true)),
            createScopeStyle("markup.italic", createTextAttribute(italic = true)),
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


    private fun createScopeStyle(prefix: String, style: TextAttributesKey): Pair<String, TextAttributesKey> {
        return Pair(prefix, TextAttributesKey.createTextAttributesKey(createScopeName(prefix), style))
    }

    private fun createScopeStyle(prefix: String, style: TextAttributes): Pair<String, TextAttributesKey> {
        return Pair(prefix, TextAttributesKey.createTempTextAttributesKey(createScopeName(prefix), style))
    }

    private fun createScopeName(scopeName: String): String {
        return "AESI_" + scopeName
                .toUpperCase()
                .replace('.', '_')
    }
}