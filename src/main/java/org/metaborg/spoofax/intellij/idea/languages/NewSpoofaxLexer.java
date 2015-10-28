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

//
///**
// * Adapts the Spoofax SGLR parser to allow it to be used as an IntelliJ lexer.
// */
//public final class NewSpoofaxLexer extends LexerBase {
//    @NotNull
//    private final ILanguageImpl languageImpl;
//    @NotNull
//    private final IParserConfiguration parserConfiguration;
//    @NotNull
//    private final ISyntaxService<IStrategoTerm> syntaxService;
//    @NotNull
//    private final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer;
//    @NotNull
//    private final IStylerService<IStrategoTerm, IStrategoTerm> styler;
//    @NotNull
//    private final SpoofaxTokenTypeManager tokenTypesManager;
//    @NotNull
//    private final IResourceService resourceService;
//    @InjectLogger
//    private Logger logger;
//    // The character buffer.
//    private CharSequence buffer;
//    // The range of characters in the buffer to lex.
//    private Range<Integer> bufferRange;
//
//    private ITokenizer tokenizer;
//    private Iterator<IRegionStyle<IStrategoTerm>> styledTokenIterator;
//
//    @Inject
//    private NewSpoofaxLexer(
//            @Assisted @NotNull final ILanguageImpl languageImpl,
//            @Assisted @NotNull final SpoofaxTokenTypeManager tokenTypesManager,
//            @NotNull final ISyntaxService<IStrategoTerm> syntaxService,
//            @NotNull final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer,
//            @NotNull final IStylerService<IStrategoTerm, IStrategoTerm> styler,
//            @NotNull final IParserConfiguration parserConfiguration,
//            @NotNull final IResourceService resourceService) {
//
//        this.syntaxService = syntaxService;
//        this.categorizer = categorizer;
//        this.styler = styler;
//        this.tokenTypesManager = tokenTypesManager;
//        this.languageImpl = languageImpl;
//        this.parserConfiguration = parserConfiguration;
//        this.resourceService = resourceService;
//    }
//
//    /**
//     * Initiates a lexing session.
//     *
//     * @param buffer       The character sequence to lex.
//     * @param startOffset  The inclusive start offset.
//     * @param endOffset    The exclusive end offset.
//     * @param initialState Not used. Must be zero.
//     */
//    @Override
//    public final void start(@NotNull final CharSequence buffer,
//                            final int startOffset,
//                            final int endOffset,
//                            final int initialState) {
//        assert initialState == 0;
//        assert 0 <= startOffset && startOffset <= buffer.length();
//        assert 0 <= endOffset && endOffset <= buffer.length();
//
//        this.buffer = buffer;
//        this.bufferRange = toRange(startOffset, endOffset);
//        ParseResult<IStrategoTerm> parseResult = parseAll();
//
//    }
//
//    /**
//     * Gets the current state of the lexer.
//     *
//     * @return An integer that indicates the current state.
//     */
//    @Override
//    public int getState() {
//        // Unused: always zero.
//        return 0;
//    }
//
//    /**
//     * The current token type, or <code>null</code>.
//     *
//     * @return The current token type;
//     * or <code>null</code> when lexing is finished.
//     */
//    @Nullable
//    @Override
//    public IElementType getTokenType() {
//        // TODO
//        return null;
//    }
//
//    /**
//     * Gets the inclusive start of the current token.
//     *
//     * @return The zero-based offset of the start of the current token in the character buffer.
//     */
//    @Override
//    public int getTokenStart() {
//        // TODO
//        return 0;
//    }
//
//    /**
//     * Gets the exclusive end of the current token.
//     *
//     * @return The zero-based offset of the end of the current token in the character buffer.
//     */
//    @Override
//    public int getTokenEnd() {
//        // TODO
//        return 0;
//    }
//
//    /**
//     * Advance the lexer to the next token.
//     */
//    @Override
//    public void advance() {
//        // TODO
//        for (int i = 0; i < tokenCount; ++i) {
//            final IToken token = tokenizer.getTokenAt(i);
//
//            if (token.getEndOffset() < token.getStartOffset())
//                // This happens when the token is empty.
//                continue;
//
//            if (token.getStartOffset() < offset)
//                // FIXME: The tokenizer sometimes returned the same token with the same start and end _twice_?
//                continue;
//
//            // ASSUME: The list of regions is ordered by offset.
//            // ASSUME: No region overlaps another region.
//            // ASSUME: Every character in the input is covered by a region.
//            int tokenStart = token.getStartOffset();
//            int tokenEnd = token.getEndOffset() + 1;
//            IntRange tokenRange = new IntRange(tokenStart, tokenEnd);
//            if (tokenRange.overlapsRange(range)) {
////            if (rangeStart <= tokenStart && tokenEnd <= rangeEnd) {
//
//                // ASSUME: The styled tokens are ordered by offset.
//                // ASSUME: No styled region overlaps another styled region.
//
//                // Iterate until we find a style that ends after the token start.
//                while (currentRegionStyle != null && currentRegionStyle.region().endOffset() + 1 <= tokenStart)
//                    currentRegionStyle = styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;
//
//                // Get the style of the token
//                IStyle tokenStyle = currentRegionStyle != null && currentRegionStyle.region().startOffset() <= tokenStart ? currentRegionStyle.style() : null;
//                SpoofaxTokenType styledTokenType = this.tokenTypesManager.getTokenType(tokenStyle);
//
//                SpoofaxToken spoofaxToken = new SpoofaxToken(styledTokenType, tokenStart, tokenEnd);
//                spoofaxTokens.add(spoofaxToken);
//                assert rangeStart <= tokenStart;
//                assert tokenEnd <= rangeEnd;
//            }
//            assert offset == tokenStart;
//            offset = tokenEnd;
//        }
//    }
//
//    /**
//     * Gets the character buffer.
//     *
//     * @return The character buffer.
//     */
//    @NotNull
//    @Override
//    public CharSequence getBufferSequence() {
//        return this.buffer;
//    }
//
//    /**
//     * Gets the exclusive end of the relevant range of characters.
//     *
//     * @return The zero-based offset of the end of the relevant range of characters in the character buffer.
//     */
//    @Override
//    public int getBufferEnd() {
//        return getRangeEnd(this.bufferRange);
//    }
//
//    /**
//     * Creates an integer range.
//     *
//     * @param start The inclusive start.
//     * @param end The exclusive end.
//     * @return The range.
//     */
//    @NotNull
//    private Range<Integer> toRange(int start, int end) {
//        return Range.between(start, end - 1);
//    }
//
//    /**
//     * Gets the inclusive start of the range.
//     *
//     * @param range The range.
//     * @return The inclusive start.
//     */
//    private int getRangeStart(@NotNull Range<Integer> range) {
//        return range.getMinimum();
//    }
//
//    /**
//     * Gets the exclusive end of the range.
//     *
//     * @param range The range.
//     * @return The exclusive end.
//     */
//    private int getRangeEnd(@NotNull Range<Integer> range) {
//        assert range.getMaximum() < Integer.MAX_VALUE;
//        return range.getMaximum() + 1;
//    }
//
//    /**
//     * Parses the whole buffer.
//     *
//     * @return The parse result.
//     */
//    private ParseResult<IStrategoTerm> parseAll() {
//        // Dummy location. Bug in Metaborg Core prevents it being null.
//        // TODO: Fix JSGLRI to allow null location.
//        FileObject location = this.resourceService.resolve(
//                "file:///home/daniel/eclipse/spoofax1507/workspace/TestProject/trans/test.spoofax");
//        ParseResult<IStrategoTerm> result;
//        try {
//            result = this.syntaxService.parse(buffer.toString(), location, this.languageImpl, this.parserConfiguration);
//        } catch (ParseException e) {
//            throw new MetaborgRuntimeException("Unhandled exception", e);
//        }
//        return result;
//    }
//
//    private void setupLexer() {
//        if (result.result == null) {
//            // A null parse result might occur when the input contains an error,
//            // and recovery fails or is disabled.
//            logger.error("Cannot categorize input of {}, parse result is empty", this.languageImpl);
//            return new ArrayList<SpoofaxToken>();
//        }
//
//        // This uses the stratego term tokenizer.
//
//        // Found here:
//        // https://github.com/metaborg/spoofax/blob/master/org.metaborg.spoofax.core/src/main/java/org/metaborg/spoofax/core/style/CategorizerService.java#L48
//
//        ImploderAttachment rootImploderAttachment = ImploderAttachment.get(result.result);
//        this.tokenizer = rootImploderAttachment.getLeftToken().getTokenizer();
////        List<SpoofaxToken> spoofaxTokens = new ArrayList<>();
//
//        Iterable<IRegionCategory<IStrategoTerm>> categorizedTokens = this.categorizer.categorize(this.languageImpl,
//                                                                                                 result);
//        Iterable<IRegionStyle<IStrategoTerm>> styledTokens = this.styler.styleParsed(this.languageImpl,
//                                                                                     categorizedTokens);
//        this.styledTokenIterator = styledTokens.iterator();
//
////        IRegionStyle<IStrategoTerm> currentRegionStyle = styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;
//
////        final int tokenCount = tokenizer.getTokenCount();
////        int offset = 0;
////        for (int i = 0; i < tokenCount; ++i) {
////            final IToken token = tokenizer.getTokenAt(i);
////
////            if (token.getEndOffset() < token.getStartOffset())
////                // This happens when the token is empty.
////                continue;
////
////            if (token.getStartOffset() < offset)
////                // FIXME: The tokenizer sometimes returned the same token with the same start and end _twice_?
////                continue;
////
////            // ASSUME: The list of regions is ordered by offset.
////            // ASSUME: No region overlaps another region.
////            // ASSUME: Every character in the input is covered by a region.
////            int tokenStart = token.getStartOffset();
////            int tokenEnd = token.getEndOffset() + 1;
////            IntRange tokenRange = new IntRange(tokenStart, tokenEnd);
////            if (tokenRange.overlapsRange(range)) {
//////            if (rangeStart <= tokenStart && tokenEnd <= rangeEnd) {
////
////                // ASSUME: The styled tokens are ordered by offset.
////                // ASSUME: No styled region overlaps another styled region.
////
////                // Iterate until we find a style that ends after the token start.
////                while (currentRegionStyle != null && currentRegionStyle.region().endOffset() + 1 <= tokenStart)
////                    currentRegionStyle = styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;
////
////                // Get the style of the token
////                IStyle tokenStyle = currentRegionStyle != null && currentRegionStyle.region().startOffset() <= tokenStart ? currentRegionStyle.style() : null;
////                SpoofaxTokenType styledTokenType = this.tokenTypesManager.getTokenType(tokenStyle);
////
////                SpoofaxToken spoofaxToken = new SpoofaxToken(styledTokenType, tokenStart, tokenEnd);
////                spoofaxTokens.add(spoofaxToken);
////                assert rangeStart <= tokenStart;
////                assert tokenEnd <= rangeEnd;
////            }
////            assert offset == tokenStart;
////            offset = tokenEnd;
////        }
//
//        assert offset == length;
//
//
//        return spoofaxTokens;
//    }
//}
