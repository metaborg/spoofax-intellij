/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.IntRange;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.StringFormatter;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.core.style.*;
import org.metaborg.core.syntax.IParserConfiguration;
import org.metaborg.core.syntax.ISyntaxService;
import org.metaborg.core.syntax.ParseException;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.util.log.ILogger;
import org.slf4j.Logger;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Adapts the Spoofax SGLR parser to allow it to be used as an IntelliJ lexer.
 */
public final class SpoofaxLexer extends LexerBase {

    @NotNull
    private final ILanguageImpl languageImpl;
    @NotNull
    private final IParserConfiguration parserConfiguration;
    @NotNull
    private final ISyntaxService<IStrategoTerm> syntaxService;
    @NotNull
    private final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer;
    @NotNull
    private final IStylerService<IStrategoTerm, IStrategoTerm> styler;
    @NotNull
    private final SpoofaxTokenTypeManager tokenTypesManager;
    @NotNull
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
    private SpoofaxLexer(
            @Assisted final ILanguageImpl languageImpl,
            @Assisted final SpoofaxTokenTypeManager tokenTypesManager,
            final ISyntaxService<IStrategoTerm> syntaxService,
            final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer,
            final IStylerService<IStrategoTerm, IStrategoTerm> styler,
            final IParserConfiguration parserConfiguration,
            final IResourceService resourceService) {
        super();

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
        assert buffer != null;
        assert initialState == 0;
        assert 0 <= startOffset && startOffset <= buffer.length();
        assert 0 <= endOffset && endOffset <= buffer.length();

        this.buffer = buffer;
        this.bufferRange = IntRange.between(startOffset, endOffset);
        this.tokenIndex = 0;
        this.tokens.clear();

        if (buffer.length() == 0)
            return;

        final ParseResult<IStrategoTerm> result = parseAll();
        tokenizeAll(result);
    }

    /**
     * Parses the whole buffer.
     *
     * @return The parse result.
     */
    private ParseResult<IStrategoTerm> parseAll() {
        // Dummy location. Bug in Metaborg Core prevents it being null.
        // TODO: Fix JSGLRI to allow null location.
        final FileObject location = this.resourceService.resolve(
                "file:///home/daniel/eclipse/spoofax1507/workspace/TestProject/trans/test.spoofax");
        final ParseResult<IStrategoTerm> result;
        try {
            result = this.syntaxService.parse(
                    this.buffer.toString(),
                    location,
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
            this.logger.error("Cannot categorize input of {}, parse result is empty", this.languageImpl);

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

        IRegionStyle<IStrategoTerm> currentRegionStyle = styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;

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

            if (tokenRange.isEmpty())
                continue;

            assert offset == tokenRange.start : StringFormatter.format(
                    "The current token (starting @ {}) must start where the previous token left off (@ {}).",
                    tokenStart,
                    offset
            );
            if (tokenRange.overlapsRange(this.bufferRange)) {
                // ASSUME: The styled tokens are ordered by offset.
                // ASSUME: No styled region overlaps another styled region.

                // Iterate until we find a style that ends after the token start.
                while (currentRegionStyle != null && currentRegionStyle.region().endOffset() + 1 <= tokenRange.start)
                    currentRegionStyle = styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;

                // Get the style of the token
                final IStyle tokenStyle = currentRegionStyle != null && currentRegionStyle.region().startOffset() <= tokenRange.start ? currentRegionStyle.style() : null;
                final SpoofaxTokenType styledTokenType = this.tokenTypesManager.getTokenType(tokenStyle);

                final SpoofaxToken spoofaxToken = new SpoofaxToken(styledTokenType, tokenRange);
                this.tokens.add(spoofaxToken);
            }
            offset = tokenRange.end;
        }

        assert offset == this.buffer.length() : StringFormatter.format(
                "The last token ended @ {}, which is before the end of the buffer @ {}.",
                offset,
                this.buffer.length()
        );
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
    @NotNull
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