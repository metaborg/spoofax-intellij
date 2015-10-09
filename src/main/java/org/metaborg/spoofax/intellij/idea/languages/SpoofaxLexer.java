package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.core.style.*;
import org.metaborg.core.syntax.IParserConfiguration;
import org.metaborg.core.syntax.ISyntaxService;
import org.metaborg.core.syntax.ParseException;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
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

    @InjectLogger
    private Logger logger;

    @NotNull private final ILanguageImpl languageImpl;
    @NotNull private final IParserConfiguration parserConfiguration;
    @NotNull private final ISyntaxService<IStrategoTerm> syntaxService;
    @NotNull private final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer;
    @NotNull private final IStylerService<IStrategoTerm, IStrategoTerm> styler;
    @NotNull private final SpoofaxTokenTypeManager tokenTypesManager;
    @NotNull private final IResourceService resourceService;

    private CharSequence buffer;
    private int startOffset;
    private int endOffset;
    private List<SpoofaxToken> tokens = new ArrayList<SpoofaxToken>();
    private int tokenIndex;

    @Inject
    public SpoofaxLexer(
            @Assisted @NotNull final ILanguageImpl languageImpl,
            @Assisted @NotNull final SpoofaxTokenTypeManager tokenTypesManager,
            @NotNull final ISyntaxService<IStrategoTerm> syntaxService,
            @NotNull final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer,
            @NotNull final IStylerService<IStrategoTerm, IStrategoTerm> styler,
            @NotNull final IParserConfiguration parserConfiguration,
            @NotNull final IResourceService resourceService){

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
     * @param buffer The character sequence to lex.
     * @param startOffset The inclusive start offset.
     * @param endOffset The exclusive end offset.
     * @param initialState Not used. Must be zero.
     */
    @Override
    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        assert buffer != null;
        assert initialState == 0;
        assert 0 <= startOffset && startOffset <= buffer.length();
        assert 0 <= endOffset && endOffset <= buffer.length();

        this.buffer = buffer;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.tokens = parse_to_tokens(buffer, startOffset, endOffset);
        this.tokenIndex = 0;
    }

    /**
     * Returns a list of tokens between the specified offsets.
     * @param buffer The character sequence to parse.
     * @param startOffset The inclusive start offset.
     * @param endOffset The exclusive end offset.
     * @return The resulting list of tokens.
     */
    private List<SpoofaxToken> parse_to_tokens(CharSequence buffer, int startOffset, int endOffset) {
        assert buffer != null;
        assert 0 <= startOffset && startOffset <= buffer.length();
        assert 0 <= endOffset && endOffset <= buffer.length();

        if (buffer.length() == 0)
            return new ArrayList<>();

        // Dummy location. Bug in Metaborg Core prevents it being null.
        FileObject location = this.resourceService.resolve("file:///home/daniel/eclipse/spoofax1507/workspace/TestProject/trans/test.spoofax");
        ParseResult<IStrategoTerm> result = null;
        try {
            result = this.syntaxService.parse(buffer.toString(), location, this.languageImpl, this.parserConfiguration);
        } catch (ParseException e) {
            // TODO: Handle the exception.
            e.printStackTrace();
        }


        List<SpoofaxToken> spoofaxTokens = TokenizeWithTokenizer(result, startOffset, endOffset, buffer.length());
        return spoofaxTokens;
    }

    private List<SpoofaxToken> TokenizeWithTokenizer(ParseResult<IStrategoTerm> result, int rangeStart, int rangeEnd, int length)
    {
        if(result.result == null) {
            // A null parse result might occur when the input contains an error,
            // and recovery fails or is disabled.
            logger.error("Cannot categorize input of {}, parse result is empty", this.languageImpl);
            return new ArrayList<SpoofaxToken>();
        }

        // This uses the stratego term tokenizer.

        // Found here:
        // https://github.com/metaborg/spoofax/blob/master/org.metaborg.spoofax.core/src/main/java/org/metaborg/spoofax/core/style/CategorizerService.java#L48

        final ImploderAttachment rootImploderAttachment = ImploderAttachment.get(result.result);
        final ITokenizer tokenizer = rootImploderAttachment.getLeftToken().getTokenizer();
        List<SpoofaxToken> spoofaxTokens = new ArrayList<SpoofaxToken>();

        Iterable<IRegionCategory<IStrategoTerm>> categorizedTokens = this.categorizer.categorize(this.languageImpl, result);
        Iterable<IRegionStyle<IStrategoTerm>> styledTokens = this.styler.styleParsed(this.languageImpl, categorizedTokens);
        Iterator<IRegionStyle<IStrategoTerm>> styledTokenIterator = styledTokens.iterator();

        IRegionStyle<IStrategoTerm> currentRegionStyle = styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;

        final int tokenCount = tokenizer.getTokenCount();
        int offset = 0;
        for(int i = 0; i < tokenCount; ++i) {
            final IToken token = tokenizer.getTokenAt(i);

            if (token.getEndOffset() < token.getStartOffset())
                // This happens when the token is empty.
                continue;

            if (token.getStartOffset() < offset)
                // FIXME: The tokenizer sometimes returned the same token with the same start and end _twice_?
                continue;

            // ASSUME: The list of regions is ordered by offset.
            // ASSUME: No region overlaps another region.
            // ASSUME: Every character in the input is covered by a region.
            int tokenStart = token.getStartOffset();
            int tokenEnd = token.getEndOffset() + 1;
            if (rangeStart <= tokenStart && tokenEnd <= rangeEnd) {

                // ASSUME: The styled tokens are ordered by offset.
                // ASSUME: No styled region overlaps another styled region.

                // Iterate until we find a style that ends after the token start.
                while (currentRegionStyle != null && currentRegionStyle.region().endOffset() + 1 <= tokenStart)
                    currentRegionStyle = styledTokenIterator.hasNext() ? styledTokenIterator.next() : null;

                // Get the style of the token
                IStyle tokenStyle = currentRegionStyle != null && currentRegionStyle.region().startOffset() <= tokenStart ? currentRegionStyle.style() : null;
                SpoofaxTokenType styledTokenType = this.tokenTypesManager.getTokenType(tokenStyle);

                SpoofaxToken spoofaxToken = new SpoofaxToken(styledTokenType, tokenStart, tokenEnd);
                spoofaxTokens.add(spoofaxToken);
                assert rangeStart <= tokenStart;
                assert tokenEnd <= rangeEnd;
            }
            assert offset == tokenStart;
            offset = tokenEnd;
        }

        assert offset == length;



        return spoofaxTokens;
    }


//    private List<SpoofaxToken> TokenizeWithCategorizer(ParseResult<IStrategoTerm> result)
//    {
//        // This uses the categorizer to split the input into tokens.
//        // However, when the input contains an error, the categorizer doesn't
//        // include all tokens (with recovery) or no tokens at all (without recovery).
//
//        List<SpoofaxToken> spoofaxTokens = new ArrayList<SpoofaxToken>();
//        Iterable<IRegionCategory<IStrategoTerm>> tokens = this.categorizer.categorize(this.languageImpl, result);
//        for (IRegionCategory<IStrategoTerm> token : tokens)
//        {
//            // ASSUME: The list of regions is ordered by offset.
//            // ASSUME: No region overlaps another region.
//            // ASSUME: Every character in the input is covered by a region. (false!)
//            if (startOffset <= token.region().endOffset() && token.region().startOffset() <= endOffset) {
//                SpoofaxToken spoofaxToken = new SpoofaxToken(SpoofaxTypes.ID_TOKEN, token.region().startOffset(), token.region().endOffset() + 1);
//                spoofaxTokens.add(spoofaxToken);
//            }
//        }
//        return spoofaxTokens;
//    }

    /**
     * The current token type, or null.
     * @return The current token type, or null when lexing is finished.
     */
    @Nullable
    @Override
    public IElementType getTokenType() {
        if (0 <= tokenIndex && tokenIndex < tokens.size())
            return tokens.get(tokenIndex).type;
        else
            return null;
    }

    @Override
    public int getTokenStart() {
        assert 0 <= tokenIndex && tokenIndex < tokens.size();
        return tokens.get(tokenIndex).start;
    }

    @Override
    public int getTokenEnd() {
        assert 0 <= tokenIndex && tokenIndex < tokens.size();
        return tokens.get(tokenIndex).end;
    }

    @Override
    public void advance() {
        tokenIndex++;
    }

    @Override
    public int getState() {
        // Unused: always zero.
        return 0;
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return this.buffer;
    }

    @Override
    public int getBufferEnd() {
        return this.endOffset;
    }
}