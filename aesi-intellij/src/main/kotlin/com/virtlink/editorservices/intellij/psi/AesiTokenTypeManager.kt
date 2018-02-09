package com.virtlink.editorservices.intellij.psi

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.Language
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.tree.TokenSet
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiLexer
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiSyntaxHighlighter
import java.net.URI

/**
 * Tracks token types.
 *
 * IntelliJ can't handle too many token types. However, we'll still need a different token type for
 * each different style. This class returns a token type that matches the style, if found; otherwise,
 * creates a new token type and stores it for re-use.

 * The token type manager is specific to a single language.
 */
class AesiTokenTypeManager
@Inject constructor(@Assisted private val language: Language) {

    /**
     * Factory.
     */
    interface IFactory {

        /**
         * Creates the token type manager.
         *
         * @param language The language.
         */
        fun create(language: Language): AesiTokenTypeManager
    }

    val scopedElementTypes = HashMap<String, AesiTokenType>()

    val defaultScope: String get() = "text"

    val whitespaceTokens get() = TokenSet.create(
            getTokenType("text.whitespace")
    )
    val commentTokens get() = TokenSet.create(
            getTokenType("comment.block"),
            getTokenType("comment.line"),
            getTokenType("comment")
    )
    val stringLiteralTokens get() = TokenSet.create(
            getTokenType("string.quoted.single"),
            getTokenType("string.quoted.double"),
            getTokenType("string.quoted.triple"),
            getTokenType("string.quoted"),
            getTokenType("string.unquoted"),
            getTokenType("string.interpolated"),
            getTokenType("string.regexp"),
            getTokenType("string")
    )

    fun getTokenType(scope: String?): AesiTokenType
        = scopedElementTypes.getOrPut(scope ?: defaultScope, { AesiTokenType(scope ?: defaultScope, language) })

}