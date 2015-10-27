package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.spoofax.intellij.idea.vfs.SpoofaxFileType;

/**
 * Stores the IntelliJ IDEA objects that are associated with a particular {@link ILanguage}.
 */
public final class IdeaLanguageAttachment {

    @NotNull
    public final SpoofaxIdeaLanguage ideaLanguage;
    @NotNull
    public final SpoofaxFileType fileType;
    @NotNull
    public final SpoofaxTokenTypeManager tokenTypeManager;
    @NotNull
    public final OldSpoofaxTokenType dummyAstTokenType;
    @NotNull
    public final ParserDefinition parserDefinition;
    @NotNull
    public final Lexer characterLexer;
    @NotNull
    public final OldSpoofaxParser parser;
    @NotNull
    public final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory;

    /**
     * Initializes a new instance of the {@link IdeaLanguageAttachment} class.
     *
     * @param ideaLanguage             The IDEA language.
     * @param fileType                 The file type.
     * @param tokenTypeManager         The token type manager.
     * @param dummyAstTokenType        The dummy AST token to use.
     * @param parserDefinition         The parser definition.
     * @param syntaxHighlighterFactory The syntax highlighter factory.
     * @param characterLexer           The character lexer.
     * @param parser                   The parser.
     */
    /* package private */ IdeaLanguageAttachment(@NotNull final SpoofaxIdeaLanguage ideaLanguage,
                                                 @NotNull final SpoofaxFileType fileType,
                                                 @NotNull final SpoofaxTokenTypeManager tokenTypeManager,
                                                 @NotNull final OldSpoofaxTokenType dummyAstTokenType,
                                                 @NotNull final ParserDefinition parserDefinition,
                                                 @NotNull final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory,
                                                 @NotNull final Lexer characterLexer,
                                                 @NotNull final OldSpoofaxParser parser) {
        this.ideaLanguage = ideaLanguage;
        this.fileType = fileType;
        this.tokenTypeManager = tokenTypeManager;
        this.dummyAstTokenType = dummyAstTokenType;
        this.parserDefinition = parserDefinition;
        this.syntaxHighlighterFactory = syntaxHighlighterFactory;
        this.characterLexer = characterLexer;
        this.parser = parser;
    }
}