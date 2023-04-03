/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.metaborg.intellij.idea.parsing;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.source.ISourceRegion;
import org.metaborg.core.style.IRegionCategory;
import org.metaborg.core.style.IRegionStyle;
import org.metaborg.core.style.IStyle;
import org.metaborg.core.syntax.ParseException;
import org.metaborg.intellij.IntRange;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenType;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenTypeManager;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.spoofax.core.style.ISpoofaxCategorizerService;
import org.metaborg.spoofax.core.style.ISpoofaxStylerService;
import org.metaborg.spoofax.core.syntax.ISpoofaxSyntaxService;
import org.metaborg.spoofax.core.syntax.JSGLRParserConfiguration;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnitService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxUnitService;
import org.metaborg.util.log.ILogger;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;

/**
 * Adapts the Spoofax SGLR parser to allow it to be used as an IntelliJ IDEA highlighting lexer.
 *
 * When {@link #start} is called, this lexer parses the whole input using the SGLR parser. The resulting tokens are
 * stored as {@link SpoofaxToken} objects from which each IntelliJ IDEA token is created when requested.
 *
 * This class is not thread-safe. Since IntelliJ IDEA will try to highlight multiple files simultaneously from different
 * threads, we have to construct a new lexer for each. As we're creating a lexer for each file, we might as well store
 * the file's information.
 */
public final class SpoofaxHighlightingLexer extends LexerBase {
    // Whether to show debug info from the lexer.
    private static final boolean DEBUG_INFO = false;

    private final @Nullable
    FileObject file;
    private final @Nullable IProject project;
    private final ILanguageImpl languageImpl;
    private final JSGLRParserConfiguration parserConfiguration;
    private final SpoofaxTokenTypeManager tokenTypesManager;
    private final ISpoofaxInputUnitService inputUnitService;
    private final ISpoofaxSyntaxService syntaxService;
    private final ISpoofaxCategorizerService categorizer;
    private final ISpoofaxStylerService styler;
    private final ISpoofaxUnitService unitService;
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
    private SpoofaxHighlightingLexer(@Assisted @Nullable FileObject file, @Assisted @Nullable IProject project,
                                     @Assisted ILanguageImpl languageImpl, @Assisted SpoofaxTokenTypeManager tokenTypesManager,
                                     ISpoofaxInputUnitService inputUnitService, ISpoofaxSyntaxService syntaxService,
                                     ISpoofaxCategorizerService categorizer, ISpoofaxStylerService styler,
                                     JSGLRParserConfiguration parserConfiguration, ISpoofaxUnitService unitService) {
        this.file = file;
        this.project = project;
        this.languageImpl = languageImpl;
        this.parserConfiguration = parserConfiguration;
        this.tokenTypesManager = tokenTypesManager;
        this.inputUnitService = inputUnitService;
        this.syntaxService = syntaxService;
        this.categorizer = categorizer;
        this.styler = styler;
        this.unitService = unitService;
    }


    /**
     * Initiates a lexing session.
     *
     * @param inputBuffer
     *            The character sequence to lex.
     * @param startOffset
     *            The inclusive start offset.
     * @param endOffset
     *            The exclusive end offset.
     * @param initialState
     *            Not used. Must be zero.
     */
    @Override public final void start(final CharSequence inputBuffer, final int startOffset, final int endOffset,
        final int initialState) {
        assert initialState == 0;
        assert 0 <= startOffset && startOffset <= inputBuffer.length();
        assert 0 <= endOffset && endOffset <= inputBuffer.length();

        this.buffer = inputBuffer;
        this.bufferRange = IntRange.between(startOffset, endOffset);
        this.tokenIndex = 0;
        this.tokens.clear();

        if(inputBuffer.length() == 0)
            return;

        this.logger.debug("Parsing ({} characters) to get requested range {} from file: {}", inputBuffer.length(),
                this.bufferRange, this.file);

        final ISpoofaxParseUnit result = parseAll();

        this.logger.debug("Tokenizing the parse result of file: {}", this.file);

        tokenizeAll(result);
    }

    /**
     * Parses the whole buffer.
     *
     * @return The parse result.
     */
    private ISpoofaxParseUnit parseAll() {
        // TODO: Optimize parsing? Is there a parse cache? I think so.
        final ISpoofaxInputUnit input =
            inputUnitService.inputUnit(file, buffer.toString(), languageImpl, null, parserConfiguration);
        try {
            return syntaxService.parse(input);
        } catch(final AssertionError e) {
            logger.error("Assertion failed in parser", e);
            return this.unitService.emptyParseUnit(input);
        } catch(final ParseException e) {
            throw new MetaborgRuntimeException("Unhandled exception", e);
        }
    }

    /**
     * Uses the Spoofax tokenizer to tokenize the parse result, and adds the tokens to the list of tokens.
     *
     * @param result
     *            The parse result to tokenize.
     */
    private void tokenizeAll(ISpoofaxParseUnit result) {
        if(!result.valid()) {
            // An invalid parse result might occur when the input contains an error,
            // and recovery fails or is disabled.
            logger.info("Cannot categorize input of {}, parse result is empty", languageImpl);

            // Return a single token covering all input.
            final IntRange tokenRange = IntRange.between(0, buffer.length());
            final SpoofaxTokenType styledTokenType =
                tokenTypesManager.getTokenType(tokenTypesManager.getDefaultStyle());
            final SpoofaxToken spoofaxToken = new SpoofaxToken(styledTokenType, tokenRange);
            tokens.add(spoofaxToken);
            return;
        }

        // This uses the stratego term tokenizer.

        // Found here:
        // https://github.com/metaborg/spoofax/blob/master/org.metaborg.spoofax.core/src/main/java/org/metaborg/spoofax/core/style/CategorizerService.java#L48

        final Iterable<IRegionCategory<IStrategoTerm>> categorizedTokens = categorizer.categorize(languageImpl, result);
        final Iterable<IRegionStyle<IStrategoTerm>> styledTokens = styler.styleParsed(languageImpl, categorizedTokens);

        for (IRegionStyle<IStrategoTerm> currentRegionStyle : styledTokens) {

            // ASSUME: The list of regions is ordered by offset.
            // ASSUME: No region overlaps another region.
            // ASSUME: Every character in the input is covered by a region.
            ISourceRegion currentRegion = currentRegionStyle.region();
            final IntRange tokenRange = IntRange.between(currentRegion.startOffset(), currentRegion.endOffset() + 1);

            if(tokenRange.overlapsRange(bufferRange)) {
                // ASSUME: The styled tokens are ordered by offset.
                // ASSUME: No styled region overlaps another styled region.

                // Get the style of the token
                @Nullable final IStyle tokenStyle =
                    currentRegion.startOffset() <= tokenRange.start ? currentRegionStyle.style() : null;
                final SpoofaxTokenType styledTokenType = tokenTypesManager.getTokenType(tokenStyle);

                final SpoofaxToken spoofaxToken = new SpoofaxToken(styledTokenType, tokenRange);
                tokens.add(spoofaxToken);

                if(DEBUG_INFO) {
                    if(tokenStyle != null) {
                        logger.trace("Token {} with style: {}", printToken(tokenRange), tokenStyle);
                    } else {
                        logger.trace("Token {} with default style: {}", printToken(tokenRange),
                            tokenTypesManager.getDefaultStyle());
                    }
                }
            } else {
                // Token is not in the requested range. No need to style it.

                if(DEBUG_INFO) {
                    logger.trace("Token {} outside requested range.", printToken(tokenRange));
                }
            }
        }
    }

    /**
     * Return a string representation of a token, for debugging and logging.
     *
     * @param tokenRange
     *            The token range.
     * @return The string representation.
     */
    private String printToken(final IntRange tokenRange) {
        return logger.format("\"{}\" @ {}",
            StringEscapeUtils.escapeJava(buffer.subSequence(tokenRange.start, tokenRange.end).toString()), tokenRange);
    }

    /**
     * Gets the range of the last added token, for debugging and logging.
     *
     * @return The range of the last added token, or an empty range if there is none.
     */
    private IntRange getLastTokenRange() {
        if(tokens.isEmpty()) {
            return IntRange.EMPTY;
        } else {
            return tokens.get(tokens.size() - 1).range();
        }
    }

    /**
     * Gets the current state of the lexer.
     *
     * @return An integer that indicates the current state.
     */
    @Override public int getState() {
        // Unused: always zero.
        return 0;
    }

    /**
     * The current token type, or <code>null</code>.
     *
     * @return The current token type, or <code>null</code> when lexing is finished.
     */
    @Nullable @Override public IElementType getTokenType() {
        if(0 <= tokenIndex && tokenIndex < tokens.size())
            return tokens.get(tokenIndex).type();
        else
            return null;
    }

    /**
     * Gets the start of the current token.
     *
     * @return The zero-based offset of the start of the current token in the character buffer.
     */
    @Override public int getTokenStart() {
        assert 0 <= tokenIndex && tokenIndex < tokens.size();
        return tokens.get(tokenIndex).range().start;
    }

    /**
     * Gets the end of the current token.
     *
     * @return The zero-based offset of the end of the current token in the character buffer.
     */
    @Override public int getTokenEnd() {
        assert 0 <= tokenIndex && tokenIndex < tokens.size();
        return tokens.get(tokenIndex).range().end;
    }

    /**
     * Advance the lexer to the next token.
     */
    @Override public void advance() {
        tokenIndex++;
    }

    /**
     * Gets the character buffer.
     *
     * @return The character buffer.
     */
    @Override public CharSequence getBufferSequence() {
        return buffer;
    }

    /**
     * Gets the end of the relevant range of characters.
     *
     * @return The zero-based offset of the end of the relevant range of characters in the character buffer.
     */
    @Override public int getBufferEnd() {
        return bufferRange.end;
    }
}
