package com.virtlink.editorservices.intellij.syntaxcoloring

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.virtlink.editorservices.intellij.psi.AesiTokenType

class AesiSyntaxHighlighter @Inject constructor(
        @Assisted val lexer: Lexer,
        private val scopeNamesManager: IScopeNamesManager,
        private val tokenScopeManager: TokenScopeManager)
    : SyntaxHighlighterBase() {

    /** Factory */
    interface IFactory {
        fun create(lexer: Lexer): AesiSyntaxHighlighter
    }

    /**
     * Returns the text attributes for the specified token type.
     *
     * Note that the order of the returned attributes matters,
     * as they are merged from first to last, where the attributes
     * of later attributes supersede those of earlier attributes.
     */
    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        if (tokenType !is AesiTokenType)
            return this.tokenScopeManager.EMPTY_KEYS

//        return this.scopeNamesManager.getTextAttributes(tokenType.scope)
        return this.tokenScopeManager.getTokenHighlights(tokenType.scope)
    }

    override fun getHighlightingLexer(): Lexer {
        return this.lexer
    }
}