/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.parsing;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.lexer.*;
import com.intellij.psi.tree.*;
import org.apache.commons.lang3.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.core.resource.*;
import org.metaborg.core.style.*;
import org.metaborg.core.syntax.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;
import org.spoofax.interpreter.terms.*;
import org.spoofax.jsglr.client.imploder.*;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Adapts the Spoofax SGLR parser to allow it to be used as an IntelliJ IDEA highlighting lexer.
 *
 * When {@link #start} is called, this lexer parses the whole input using the SGLR parser.
 * The resulting tokens are stored as {@link SpoofaxToken} objects from which each
 * IntelliJ IDEA token is created when requested.
 *
 * This class is not thread-safe. Since IntelliJ IDEA will try to highlight multiple files
 * simultaneously from different threads, we have to construct a new lexer for each.
 * As we're creating a lexer for each file, we might as well store the file's information.
 */
public final class SpoofaxHighlightingLexer extends LexerBase {

    private final FileObject file;
    @Nullable private final IProject project;
    private final ILanguageImpl languageImpl;
    private final IParserConfiguration parserConfiguration;
    private final ISyntaxService<IStrategoTerm> syntaxService;
    private final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer;
    private final IStylerService<IStrategoTerm, IStrategoTerm> styler;
    private final SpoofaxTokenTypeManager tokenTypesManager;
    private final IResourceService resourceService;
    @InjectLogger
    private ILogger logger;
    // The character buffer.
    private CharSequence buffer;
    // The range of characters in the buffer to lex.
    private IntRange bufferRange;
    // A list of tokens gathered from the lexed characters.
    private final List<SpoofaxToken> tokens = new ArrayList<>();
    // The current index in {@link #tokens}.
    private int tokenIndex;

    @Inject
    private SpoofaxHighlightingLexer(
            @Assisted final FileObject file,
            @Assisted @Nullable final IProject project,
            @Assisted final ILanguageImpl languageImpl,
            @Assisted final SpoofaxTokenTypeManager tokenTypesManager,
            final ISyntaxService<IStrategoTerm> syntaxService,
            final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer,
            final IStylerService<IStrategoTerm, IStrategoTerm> styler,
            final IParserConfiguration parserConfiguration,
            final IResourceService resourceService) {
        super();

        this.file = file;
        this.project = project;
        this.syntaxService = syntaxService;
        this.categorizer = categorizer;
        this.styler = styler;
        this.tokenTypesManager = tokenTypesManager;
        this.languageImpl = languageImpl;
        this.parserConfiguration = parserConfiguration;
        this.resourceService = resourceService;
    }

    /**
     * Initiates a lexing session.
     *
     * @param buffer       The character sequence to lex.
     * @param startOffset  The inclusive start offset.
     * @param endOffset    The exclusive end offset.
     * @param initialState Not used. Must be zero.
     */
    @Override
    public final void start(
            final CharSequence buffer,
            final int startOffset,
            final int endOffset,
            final int initialState) {
        assert initialState == 0;
        assert 0 <= startOffset && startOffset <= buffer.length();
        assert 0 <= endOffset && endOffset <= buffer.length();

        this.buffer = buffer;
        this.bufferRange = IntRange.between(startOffset, endOffset);
        this.tokenIndex = 0;
        this.tokens.clear();

        if (buffer.length() == 0)
            return;

        this.logger.debug("Parsing ({} characters) to get requested range {} from file: {}",
                buffer.length(), this.bufferRange, this.file);

        final ParseResult<IStrategoTerm> result = parseAll();

        this.logger.debug("Tokenizing the parse result of file: {}", this.file);

        tokenizeAll(result);
    }

    /**
     * Parses the whole buffer.
     *
     * @return The parse result.
     */
    private ParseResult<IStrategoTerm> parseAll() {
        final ParseResult<IStrategoTerm> result;
        final String text = this.buffer.toString();

        // TODO: Optimize parsing? Is there a parse cache? I think so.
        try {

            result = this.syntaxService.parse(
                    this.buffer.toString(),
                    this.file,
                    this.languageImpl,
                    this.parserConfiguration
            );
        } catch (final ParseException e) {
            throw new MetaborgRuntimeException("Unhandled exception", e);
        }
        return result;
    }

    /**
     * Uses the Spoofax tokenizer to tokenize the parse result,
     * and adds the tokens to the list of tokens.
     *
     * @param result The parse result to tokenize.
     */
    private void tokenizeAll(final ParseResult<IStrategoTerm> result) {
        if (result.result == null) {
            // A null parse result might occur when the input contains an error,
            // and recovery fails or is disabled.
            this.logger.info("Cannot categorize input of {}, parse result is empty", this.languageImpl);

            // Return a single token covering all input.
            final IntRange tokenRange = IntRange.between(0, this.buffer.length());
            final SpoofaxTokenType styledTokenType = this.tokenTypesManager.getTokenType(this.tokenTypesManager.getDefaultStyle());
            final SpoofaxToken spoofaxToken = new SpoofaxToken(styledTokenType, tokenRange);
            this.tokens.add(spoofaxToken);
            return;
        }

        // This uses the stratego term tokenizer.

        // Found here:
        // https://github.com/metaborg/spoofax/blob/master/org.metaborg.spoofax.core/src/main/java/org/metaborg/spoofax/core/style/CategorizerService.java#L48

        final ImploderAttachment rootImploderAttachment = ImploderAttachment.get(result.result);
        final ITokenizer tokenizer = rootImploderAttachment.getLeftToken().getTokenizer();

        final Iterable<IRegionCategory<IStrategoTerm>> categorizedTokens = this.categorizer.categorize(
                this.languageImpl,
                result
        );
        final Iterable<IRegionStyle<IStrategoTerm>> styledTokens = this.styler.styleParsed(
                this.languageImpl,
                categorizedTokens
        );
        final Iterator<IRegionStyle<IStrategoTerm>> styledTokenIterator = styledTokens.iterator();

        @Nullable IRegionStyle<IStrategoTerm> currentRegionStyle =
                styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;

        final int tokenCount = tokenizer.getTokenCount();
        int offset = 0;
        for (int i = 0; i < tokenCount; ++i) {
            final IToken token = tokenizer.getTokenAt(i);

            // ASSUME: The list of regions is ordered by offset.
            // ASSUME: No region overlaps another region.
            // ASSUME: Every character in the input is covered by a region.
            final int tokenStart = token.getStartOffset();
            final int tokenEnd = token.getEndOffset() + 1;
            final IntRange tokenRange = IntRange.between(tokenStart, tokenEnd);

            if (tokenRange.isEmpty()) {
                // The tokenizer may return empty tokens. Don't know why.
                // Let's ignore those.

                this.logger.info("Token {} is empty. Token ignored.",
                        printToken(tokenRange));

                continue;
            }

            if (tokenRange.start < offset) {
                // Due to a bug in the tokenizer we may see another token covering the same character(s)
                // as the previous token. Let's ignore those tokens for now.
                // From what I've seem it's always the same token as the previous token, but I may be wrong.
                // From that follows that the next token should start where the previous (non-ignored) token
                // ended. If that's not the case, the next assertion will fail.

                this.logger.info("Token {} overlaps previous token {}. Token ignored.",
                        printToken(tokenRange),
                        printToken(getLastTokenRange()));

                continue;
            }

            assert offset == tokenRange.start : this.logger.format(
                    "The current token {} must start where the previous token left off {}.",
                    printToken(tokenRange),
                    printToken(getLastTokenRange())
            );

            if (tokenRange.overlapsRange(this.bufferRange)) {
                // ASSUME: The styled tokens are ordered by offset.
                // ASSUME: No styled region overlaps another styled region.

                // Iterate until we find a style that ends after the token start.
                while (currentRegionStyle != null && currentRegionStyle.region().endOffset() + 1 <= tokenRange.start)
                    currentRegionStyle = styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;

                // Get the style of the token
                @Nullable final IStyle tokenStyle = currentRegionStyle != null
                        && currentRegionStyle.region().startOffset() <= tokenRange.start ? currentRegionStyle.style() : null;
                final SpoofaxTokenType styledTokenType = this.tokenTypesManager.getTokenType(tokenStyle);

                final SpoofaxToken spoofaxToken = new SpoofaxToken(styledTokenType, tokenRange);
                this.tokens.add(spoofaxToken);

                if (tokenStyle != null) {
                    this.logger.trace("Token {} with style: {}",
                            printToken(tokenRange), tokenStyle);
                } else {
                    this.logger.trace("Token {} with default style: {}",
                            printToken(tokenRange), this.tokenTypesManager.getDefaultStyle());
                }
            } else {
                // Token is not in the requested range. No need to style it.

                this.logger.trace("Token {} outside requested range.", printToken(tokenRange));
            }
            offset = tokenRange.end;
        }

        assert offset == this.buffer.length() : this.logger.format(
                "The last token {} ended at {}, which is not at the end of the buffer @ {}.",
                printToken(getLastTokenRange()),
                offset,
                this.buffer.length()
        );
    }

    /**
     * Returnd a string representation of a token, for debugging and logging.
     *
     * @param tokenRange The token range.
     * @return The string representation.
     */
    private String printToken(final IntRange tokenRange) {
        return this.logger.format("\"{}\" @ {}",
                StringEscapeUtils.escapeJava(this.buffer.subSequence(tokenRange.start, tokenRange.end).toString()),
                tokenRange);
    }

    /**
     * Gets the range of the last added token, for debugging and logging.
     *
     * @return The range of the last added token, or an empty range if there is none.
     */
    private IntRange getLastTokenRange() {
        if (this.tokens.isEmpty()) {
            return IntRange.EMPTY;
        } else {
            return this.tokens.get(this.tokens.size() - 1).range();
        }
    }

    /**
     * Gets the current state of the lexer.
     *
     * @return An integer that indicates the current state.
     */
    @Override
    public int getState() {
        // Unused: always zero.
        return 0;
    }

    /**
     * The current token type, or <code>null</code>.
     *
     * @return The current token type, or <code>null</code> when lexing is finished.
     */
    @Nullable
    @Override
    public IElementType getTokenType() {
        if (0 <= this.tokenIndex && this.tokenIndex < this.tokens.size())
            return this.tokens.get(this.tokenIndex).type();
        else
            return null;
    }

    /**
     * Gets the start of the current token.
     *
     * @return The zero-based offset of the start of the current token in the character buffer.
     */
    @Override
    public int getTokenStart() {
        assert 0 <= this.tokenIndex && this.tokenIndex < this.tokens.size();
        return this.tokens.get(this.tokenIndex).range().start;
    }

    /**
     * Gets the end of the current token.
     *
     * @return The zero-based offset of the end of the current token in the character buffer.
     */
    @Override
    public int getTokenEnd() {
        assert 0 <= this.tokenIndex && this.tokenIndex < this.tokens.size();
        return this.tokens.get(this.tokenIndex).range().end;
    }

    /**
     * Advance the lexer to the next token.
     */
    @Override
    public void advance() {
        this.tokenIndex++;
    }

    /**
     * Gets the character buffer.
     *
     * @return The character buffer.
     */
    @Override
    public CharSequence getBufferSequence() {
        return this.buffer;
    }

    /**
     * Gets the end of the relevant range of characters.
     *
     * @return The zero-based offset of the end of the relevant range of characters in the character buffer.
     */
    @Override
    public int getBufferEnd() {
        return this.bufferRange.end;
    }

}