package com.virtlink.editorservices.intellij.syntaxcoloring

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.Language
import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import com.virtlink.editorservices.NullCancellationToken
import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.ScopeNames
import com.virtlink.editorservices.Span
import com.virtlink.editorservices.intellij.psi.AesiTokenTypeManager
import com.virtlink.editorservices.syntaxcoloring.ISyntaxColoringService
import com.virtlink.editorservices.spoofax.syntaxcoloring.ISpoofaxSyntaxColoringService
import com.virtlink.editorservices.syntaxcoloring.IToken
import com.virtlink.editorservices.syntaxcoloring.SyntaxColoringInfo
import com.virtlink.editorservices.syntaxcoloring.Token
import com.virtlink.logging.logger
import java.net.URI

/**
 * IntelliJ lexer used to produce tokens for AST construction and highlighting.
 */
class AesiLexer @Inject constructor(
        @Assisted private val documentUri: URI,
        @Assisted private val language: Language,
        @Assisted private val tokenTypeManager: AesiTokenTypeManager,
        private val syntaxColoringService: ISpoofaxSyntaxColoringService)
    : LexerBase() {

    interface IFactory {
        fun create(documentUri: URI, language: Language, tokenTypeManager: AesiTokenTypeManager): AesiLexer
    }

    @Suppress("PrivatePropertyName")
    private val LOG by logger()

    private var buffer: CharSequence? = null
    private var startOffset: Offset = 0
    private var endOffset: Offset = 0
    private var tokens = emptyList<IntelliJToken>()
    private var tokenIndex: Int = 0

    /**
     * Called by IntelliJ at the start of a lexing session.
     *
     * We call the [ISyntaxColoringService], and store the tokens it returns.
     * IntelliJ then calls the [advance] method to iterate the tokens,
     * and extracts their information using the [getTokenType], [getTokenStart],
     * and [getTokenEnd] methods.
     *
     * We ignore the initial state parameter, which is used in incremental parsing.
     */
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        assert(initialState == 0)
        assert(0 <= startOffset && startOffset <= buffer.length)
        assert(0 <= endOffset && endOffset <= buffer.length)

        LOG.debug("Lexing $documentUri...")

        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.tokenIndex = 0

        if (buffer.isEmpty()) {
            LOG.debug("Buffer is empty.")
            this.tokens = emptyList()
        } else {
            val coloringInfo = this.syntaxColoringService.getSyntaxColoringInfo(
                    this.documentUri,
                    this.language.toString(),
                    Span(this.startOffset, this.endOffset),
                    NullCancellationToken) ?: getDefaultColoringInfo(buffer)

            LOG.debug("Colorizer returned ${coloringInfo.tokens.size} tokens")
            this.tokens = toIntelliJTokens(coloringInfo.tokens)
        }
        LOG.debug("Tokenizer produced ${this.tokens.size} tokens")
    }

    /**
     * Returns the default coloring info when the syntax coloring service
     * does not return anything.
     */
    private fun getDefaultColoringInfo(buffer: CharSequence): SyntaxColoringInfo {
        return SyntaxColoringInfo(listOf(Token(Span.fromLength(0, buffer.length), ScopeNames("text"))))
    }

    /**
     * Converts the list of colorizer service tokens into tokens
     * that IntelliJ can understand.
     *
     * Invalid tokens, that overlap, are empty, or that are in the
     * wrong order, are discarded.
     */
    private fun toIntelliJTokens(tokens: List<IToken>): List<IntelliJToken> {
        val newTokens = mutableListOf<IntelliJToken>()
        var offset = 0

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

    /**
     * Constructs and adds an IntelliJ token to the list.
     *
     * @param tokenList The list of IntelliJ tokens to add the new token to.
     * @param token The AESI token that must be converted to an IntelliJ token; or null.
     * @param startOffset The start offset of the token.
     * @param endOffset The end offset of the token.
     */
    private fun addTokenElement(tokenList: MutableList<IntelliJToken>, token: IToken?, startOffset: Offset, endOffset: Offset): Offset {
        val tokenType = getTokenElementType(token)
        tokenList.add(IntelliJToken(startOffset, endOffset, tokenType))
        return endOffset
    }

    /**
     * Determines the token element type, given the token's scope names.
     *
     * @param token The token
     * @return The element type.
     */
    private fun getTokenElementType(token: IToken?): IElementType {
        return this.tokenTypeManager.getTokenType(token?.scopes ?: ScopeNames())
    }

    override fun advance() {
        tokenIndex++
    }

    override fun getTokenStart(): Int {
        assert(0 <= tokenIndex && tokenIndex < tokens.size,
                { "Expected index 0 <= $tokenIndex < ${tokens.size}." })
        return tokens[tokenIndex].startOffset
    }

    override fun getTokenEnd(): Int {
        assert(0 <= tokenIndex && tokenIndex < tokens.size,
                { "Expected index 0 <= $tokenIndex < ${tokens.size}." })
        return tokens[tokenIndex].endOffset
    }

    override fun getTokenType(): IElementType? {
        return if (0 <= tokenIndex && tokenIndex < tokens.size)
            tokens[tokenIndex].tokenType
        else
            null
    }

    override fun getBufferSequence(): CharSequence = this.buffer!!

    override fun getBufferEnd(): Int = this.endOffset

    override fun getState(): Int = 0

    /**
     * Represents an IntelliJ token.
     */
    private data class IntelliJToken(
            val startOffset: Offset,
            val endOffset: Offset,
            val tokenType: IElementType)
}