package com.virtlink.editorservices.intellij.syntaxcoloring

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.virtlink.editorservices.intellij.psi.AesiTokenType
import java.net.URI

class AesiSyntaxHighlighter @Inject constructor(
        @Assisted val lexer: Lexer,
        private val scopeManager: ScopeManager)
    : SyntaxHighlighterBase() {

    /** Factory */
    interface IFactory {
        fun create(lexer: Lexer): AesiSyntaxHighlighter
    }

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        if (tokenType !is AesiTokenType)
            return this.scopeManager.EMPTY_KEYS

        return this.scopeManager.getTokenHighlights(tokenType.scope)
    }

    override fun getHighlightingLexer(): Lexer {
        return this.lexer
    }
}