package com.virtlink.editorservices.intellij.psi

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.Language
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.tree.TokenSet
import com.virtlink.editorservices.ScopeNames
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

    val scopedElementTypes = HashMap<ScopeNames, AesiTokenType>()

    val defaultScope: ScopeNames get() = ScopeNames("text")

    val whitespaceTokens get() = TokenSet.create(
            getTokenType(ScopeNames("text.whitespace"))
    )
    val commentTokens get() = TokenSet.create(
            getTokenType(ScopeNames("comment.block")),
            getTokenType(ScopeNames("comment.line")),
            getTokenType(ScopeNames("comment"))
    )
    val stringLiteralTokens get() = TokenSet.create(
            getTokenType(ScopeNames("string.quoted.single")),
            getTokenType(ScopeNames("string.quoted.double")),
            getTokenType(ScopeNames("string.quoted.triple")),
            getTokenType(ScopeNames("string.quoted")),
            getTokenType(ScopeNames("string.unquoted")),
            getTokenType(ScopeNames("string.interpolated")),
            getTokenType(ScopeNames("string.regexp")),
            getTokenType(ScopeNames("string"))
    )

    fun getTokenType(scopes: ScopeNames): AesiTokenType
        = scopedElementTypes.getOrPut(scopes, { AesiTokenType(scopes, language) })

}