package com.virtlink.editorservices.intellij.syntaxcoloring

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lexer.LexerBase
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.tree.IElementType
import com.virtlink.editorservices.NullCancellationToken
import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.ScopeNames
import com.virtlink.editorservices.Span
import com.virtlink.editorservices.intellij.psi.AesiTokenTypeManager
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager
import com.virtlink.editorservices.syntaxcoloring.ISyntaxColoringService
import com.virtlink.editorservices.syntaxcoloring.IToken
import com.virtlink.editorservices.syntaxcoloring.SyntaxColoringInfo
import com.virtlink.editorservices.syntaxcoloring.Token
import java.net.URI

class AesiLexer @Inject constructor(
        @Assisted private val documentUri: URI,
        private val tokenTypeManager: AesiTokenTypeManager,
        private val syntaxColoringService: ISyntaxColoringService,
        private val scopeManager: ScopeManager,
        private val resourceManager: IntellijResourceManager)
    : LexerBase() {

    /**
     * Factory for the language-specific lexer.
     */
    interface IFactory {

        /**
         * Creates the lexer.
         *
         * @param documentUri The document URI.
         */
        fun create(documentUri: URI): AesiLexer
    }

    private val LOG = Logger.getInstance(this.javaClass)

    private var buffer: CharSequence? = null
    private var startOffset: Offset = 0
    private var endOffset: Offset = 0
    private var tokens = emptyList<AesiToken>()
    private var tokenIndex: Int = 0

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        assert(initialState == 0)
        assert(0 <= startOffset && startOffset <= buffer.length)
        assert(0 <= endOffset && endOffset <= buffer.length)

        LOG.debug("Lexing $documentUri...")

        this.buffer = buffer
        this.startOffset = startOffset.toLong()
        this.endOffset = endOffset.toLong()
        this.tokenIndex = 0


        if (buffer.isEmpty()) {
            LOG.debug("Buffer is empty.")
            this.tokens = emptyList()
        } else {
            val coloringInfo = this.syntaxColoringService.getSyntaxColoringInfo(
                    this.documentUri,
                    Span(this.startOffset, this.endOffset),
                    NullCancellationToken) ?: getDefaultTokens(this.documentUri)

            LOG.debug("Colorizer returned ${coloringInfo.tokens.size} tokens")
            this.tokens = tokenize(coloringInfo.tokens)
        }
        LOG.debug("Tokenizer produced ${this.tokens.size} tokens")
    }

    private fun getDefaultTokens(documentUri: URI): SyntaxColoringInfo {
        val content = this.resourceManager.getContent(documentUri) ?: return SyntaxColoringInfo(emptyList())
        return SyntaxColoringInfo(listOf(Token(Span.fromLength(0, content.length.toInt()), ScopeNames("text"))))
    }

    private fun tokenize(tokens: List<IToken>): List<AesiToken> {
        val newTokens = mutableListOf<AesiToken>()
        var offset = 0L

        for (token in tokens) {
            val tokenStart = token.location.startOffset
            val tokenEnd = token.location.endOffset

            // We assume that tokens are non-empty. When we encounter
            // a token with an end at or before its start,
            // it gets ignored.
            if (tokenEnd <= tokenStart) continue

            // We assume the list of tokens is ordered by value.
            // When we encounter a token that's before the current
            // `value`, it gets ignored.
            // We assume that no tokens overlap. When we encounter a
            // token that starts before the previous token ends,
            // it gets ignored.
            if (tokenStart < offset) continue

            // We assume that tokens cover all characters. When we
            // encounter a stretch of characters not covered by a
            // token, we assign it our own dummy token/element.
            if (offset < tokenStart) {
                // Add dummy element.
                offset = addTokenElement(newTokens, null, offset, tokenStart)
            }

            assert(offset == tokenStart)

            // Add element.
            offset = addTokenElement(newTokens, token, offset, tokenEnd)

            // When we've seen tokens up to the end of the highlighted range
            // we bail out.
            if (offset >= this.endOffset)
                break
        }

        // When there is a gap between the last token and the end of the highlighted range
        // we insert our own dummy token/element.
        if (offset < this.endOffset) {
            offset = addTokenElement(newTokens, null, offset, this.endOffset)
        }

        assert(offset >= this.endOffset)

        return newTokens
    }

    private fun addTokenElement(tokenList: MutableList<AesiToken>, token: IToken?, offset: Offset, endOffset: Offset): Offset {
        val tokenType = getTokenType(token)
        tokenList.add(AesiToken(offset, endOffset, tokenType))
        return endOffset
    }

    private fun getTokenType(token: IToken?): IElementType {
        val name = this.scopeManager.getSimplifiedScope(token?.scopes ?: ScopeNames(this.scopeManager.DEFAULT_SCOPE))
        val tokenType = this.tokenTypeManager.getTokenType(name)
        return tokenType
    }

    override fun advance() {
        tokenIndex++
    }

    override fun getTokenStart(): Int {
        assert(0 <= tokenIndex && tokenIndex < tokens.size,
                { "Expected index 0 <= $tokenIndex < ${tokens.size}." })
        return tokens[tokenIndex].startOffset.toInt()
    }

    override fun getTokenEnd(): Int {
        assert(0 <= tokenIndex && tokenIndex < tokens.size,
                { "Expected index 0 <= $tokenIndex < ${tokens.size}." })
        return tokens[tokenIndex].endOffset.toInt()
    }

    override fun getTokenType(): IElementType? {
        return if (0 <= tokenIndex && tokenIndex < tokens.size)
            tokens[tokenIndex].tokenType
        else
            null
    }

    override fun getBufferSequence(): CharSequence = this.buffer!!

    override fun getBufferEnd(): Int = this.endOffset.toInt()

    override fun getState(): Int = 0

    private class AesiToken(
            val startOffset: Offset,
            val endOffset: Offset,
            val tokenType: IElementType)
}