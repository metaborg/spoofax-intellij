package com.virtlink.editorservices.spoofax.syntaxcoloring

import com.virtlink.editorservices.ICancellationToken
import com.virtlink.editorservices.ScopeNames
import com.virtlink.editorservices.Span
import com.virtlink.editorservices.resources.IResourceManager
import com.virtlink.editorservices.syntaxcoloring.*
import com.virtlink.logging.format
import org.metaborg.core.MetaborgRuntimeException
import org.metaborg.core.language.ILanguageImpl
import org.metaborg.core.style.IRegionStyle
import org.metaborg.core.syntax.ParseException
import org.metaborg.spoofax.core.style.ISpoofaxCategorizerService
import org.metaborg.spoofax.core.style.ISpoofaxStylerService
import org.metaborg.spoofax.core.syntax.ISpoofaxSyntaxService
import org.metaborg.spoofax.core.syntax.JSGLRParserConfiguration
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnitService
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit
import org.spoofax.interpreter.terms.IStrategoTerm
import org.spoofax.jsglr.client.imploder.ImploderAttachment
import org.apache.commons.text.StringEscapeUtils
import org.metaborg.core.style.IStyle
import java.net.URI
import org.slf4j.helpers.MessageFormatter
import java.awt.Color
import com.virtlink.logging.logger
import com.google.inject.Inject
import com.virtlink.editorservices.spoofax.LanguageHelper

/**
 * Spoofax Core syntax coloring service implementation.
 */
class SpoofaxSyntaxColoringService @Inject constructor(
//        private val resourceService: IResourceService,
        private val resourceManager: IResourceManager,
        private val languageHelper: LanguageHelper,
//        private val languageService: ILanguageService,
//        private val languageIdentifierService: LanguageIdentifierService,
//        private val languageImpl: ILanguageImpl,
        private val parserConfiguration: JSGLRParserConfiguration,
        private val unitService: ISpoofaxInputUnitService,
        private val syntaxService: ISpoofaxSyntaxService,
        private val categorizer: ISpoofaxCategorizerService,
        private val styler: ISpoofaxStylerService
): ISpoofaxSyntaxColoringService {

    companion object {
        /**  Whether to show debug info from the lexer. */
        @Suppress("PrivatePropertyName")
        private const val DEBUG_INFO = false
    }

    @Suppress("PrivatePropertyName")
    private val LOG by logger()

    override fun configure(configuration: ISyntaxColoringConfiguration) {
        // Nothing to do.
    }

    override fun getSyntaxColoringInfo(document: URI, language: String?, span: Span, cancellationToken: ICancellationToken): ISyntaxColoringInfo? {

        val languageImpl = this.languageHelper.determineLanguageOf(document, language) ?: return null

        val content = this.resourceManager.getContent(document) ?: return null

        val text = content.text

        assert(0 <= span.startOffset && span.startOffset <= text.length)
        assert(0 <= span.endOffset && span.endOffset <= text.length)

        if (text.isEmpty()) return null

        LOG.debug("Parsing ({} characters) to get requested span {} from file: {}",
                text.length, span, document)

        val result = parseAll(text, languageImpl)

        LOG.debug("Tokenizing the parse result of document: {}", document)

        return SyntaxColoringInfo(tokenizeAll(text, languageImpl, span, result))
    }

    /**
     * Parses the whole buffer.
     *
     * @param text The content of the file being parsed.
     * @param languageImpl The language of the content.
     * @return The parse result.
     */
    private fun parseAll(text: String, languageImpl: ILanguageImpl): ISpoofaxParseUnit {
        // TODO: Optimize parsing? Is there a parse cache? I think so.
        val inputUnit = unitService.inputUnit(null, text, languageImpl, null, parserConfiguration)
        try {
            return syntaxService.parse(inputUnit)
        } catch (e: ParseException) {
            throw MetaborgRuntimeException("Unhandled exception", e)
        }

    }

    /**
     * Uses the Spoofax tokenizer to tokenize the parse result, and adds the tokens to the list of tokens.
     *
     * @param text The input text that was parsed.
     * @param languageImpl The language of the content.
     * @param parseResult The parse result to tokenize.
     * @return The resulting list of tokens.
     */
    private fun tokenizeAll(text: String, languageImpl: ILanguageImpl, inputSpan: Span, parseResult: ISpoofaxParseUnit): List<IToken> {
        val tokens = mutableListOf<IToken>()

        if (!parseResult.valid()) {
            // An invalid parse result might occur when the input contains an error,
            // and recovery fails or is disabled.
            LOG.info("Cannot categorize input of {}, parse result is empty", languageImpl)

            // Return a single token covering all input.
            val tokenSpan = Span(0, text.length)
            val spoofaxToken = SpoofaxToken(tokenSpan, ScopeNames())
            tokens.add(spoofaxToken)
            return tokens
        }

        // This uses the Stratego term tokenizer.

        // Found here:
        // https://github.com/metaborg/spoofax/blob/master/org.metaborg.spoofax.core/src/main/java/org/metaborg/spoofax/core/style/CategorizerService.java#L48

        val rootImploderAttachment = ImploderAttachment.get(parseResult.ast()!!)
        val tokenizer = rootImploderAttachment.leftToken.tokenizer

        val categorizedTokens = categorizer.categorize(languageImpl, parseResult)
        val styledTokens = styler.styleParsed(languageImpl, categorizedTokens)
        val styledTokenIterator = styledTokens.iterator()

        var currentRegionStyle: IRegionStyle<IStrategoTerm>? = if (styledTokenIterator.hasNext()) styledTokenIterator.next() else null

        val tokenCount = tokenizer.tokenCount
        var offset = 0
        for (i in 0 until tokenCount) {
            val token = tokenizer.getTokenAt(i)

            // ASSUME: The list of regions is ordered by offset.
            // ASSUME: No region overlaps another region.
            // ASSUME: Every character in the input is covered by a region.
            val tokenStart = token.startOffset
            val tokenEnd = token.endOffset + 1
            val tokenSpan = Span(tokenStart, tokenEnd)

            if (tokenSpan.isEmpty) {
                // The tokenizer may return empty tokens. Don't know why.
                // Let's ignore those.

                if (DEBUG_INFO) {
                    LOG.info("Token {} is empty. Token ignored.", printToken(text, tokenSpan))
                }

                continue
            }

            if (tokenSpan.startOffset < offset) {
                // Due to a bug in the tokenizer we may see another token covering the same character(s)
                // as the previous token. Let's ignore those tokens for now.
                // From what I've seem it's always the same token as the previous token, but I may be wrong.
                // From that follows that the next token should start where the previous (non-ignored) token
                // ended. If that's not the case, the next assertion will fail.

                if (DEBUG_INFO) {
                    LOG.info("Token {} overlaps previous token {}. Token ignored.",
                            printToken(text, tokenSpan), printToken(text, getLastTokenRange(tokens)))
                }

                continue
            }

            assert(offset == tokenSpan.startOffset) {
                MessageFormatter.format("The current token {} must start where the previous token left off {}.",
                        printToken(text, tokenSpan),
                        printToken(text, getLastTokenRange(tokens)))
            }

            if (tokenSpan.overlaps(inputSpan)) {
                // ASSUME: The styled tokens are ordered by offset.
                // ASSUME: No styled region overlaps another styled region.

                // Iterate until we find a style that ends after the token start.
                while (currentRegionStyle != null && currentRegionStyle.region().endOffset() + 1 <= tokenSpan.startOffset)
                    currentRegionStyle = if (styledTokenIterator.hasNext()) styledTokenIterator.next() else null

                // Get the style of the token
                val tokenStyle = if (currentRegionStyle != null && currentRegionStyle.region().startOffset() <= tokenSpan.startOffset)
                    currentRegionStyle.style()
                else
                    null
                val scopeName = if (tokenStyle != null) ScopeNames(getScopeName(tokenStyle)) else ScopeNames()

                val spoofaxToken = SpoofaxToken(tokenSpan, scopeName)
                tokens.add(spoofaxToken)

                if (DEBUG_INFO) {
                    if (tokenStyle != null) {
                        LOG.trace("Token {} with style: {}", printToken(text, tokenSpan), tokenStyle)
                    } else {
                        LOG.trace("Token {} with default style.", printToken(text, tokenSpan))
                    }
                }
            } else {
                // Token is not in the requested range. No need to style it.

                if (DEBUG_INFO) {
                    LOG.trace("Token {} outside requested range.", printToken(text, tokenSpan))
                }
            }
            offset = tokenSpan.endOffset
        }

        assert(offset == text.length) {
            LOG.format(
                    "The last token {} ended at {}, which is not at the end of the buffer @ {}.",
                    printToken(text, getLastTokenRange(tokens)), offset, text.length)
        }

        return tokens
    }

    /**
     * Gets the range of the last added token, for debugging and logging.
     *
     * @param tokens The list of tokens built so far.
     * @return The span of the last added token, or an empty span if there is none.
     */
    private fun getLastTokenRange(tokens: List<IToken>): Span {
        return if (tokens.isEmpty()) {
            Span.empty
        } else {
            tokens[tokens.size - 1].location
        }
    }

    /**
     * Return a string representation of a token, for debugging and logging.
     *
     * @param text The input text.
     * @param tokenSpan The token span.
     * @return The string representation.
     */
    private fun printToken(text: String, tokenSpan: Span): String {
        return LOG.format("\"{}\" @ {}",
                StringEscapeUtils.escapeJava(text.subSequence(tokenSpan.startOffset, tokenSpan.endOffset).toString()), tokenSpan)
    }

    /**
     * Creates the custom scope name for the exact style.
     *
     * @param style The style.
     * @return The custom scope name.
     */
    private fun getScopeName(style: IStyle): String {
        val sb = StringBuilder()

        val foregroundColor = style.color()
        if (foregroundColor != null) {
            sb.append(".FC:")
            sb.append(formatColor(foregroundColor))
        }

        val backgroundColor = style.backgroundColor()
        if (backgroundColor != null) {
            sb.append(".BC:")
            sb.append(formatColor(backgroundColor))
        }

        if (style.bold()) sb.append(".B")
        if (style.italic()) sb.append(".I")
        if (style.underscore()) sb.append(".U")
        if (style.strikeout()) sb.append(".S")

        return sb.toString()
    }

    /**
     * Formats a color as a scope tag value (e.g. `#ffcc00`).
     *
     * @param color The color to format.
     * @return The resulting string.
     */
    private fun formatColor(color: Color): String {
        return String.format("#%02x%02x%02x", color.red, color.green, color.blue)
    }
}