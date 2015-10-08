package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.languages.SpoofaxFileType;
import org.metaborg.spoofax.intellij.languages.SpoofaxIdeaLanguage;
import org.metaborg.spoofax.intellij.languages.syntax.*;

/**
 * Stores the IntelliJ IDEA objects that are associated with a particular language.
 */
public final class IdeaLanguageObject {

    @NotNull
    public final SpoofaxIdeaLanguage ideaLanguage;
    @NotNull
    public final SpoofaxFileType fileType;
    @NotNull
    public final SpoofaxTokenTypeManager tokenTypeManager;
    @NotNull
    public final OldSpoofaxTokenType dummyAstTokenType;
    @NotNull
    public final SpoofaxParserDefinition parserDefinition;
    @NotNull
    public final SpoofaxLexer lexer;
    @NotNull
    public final OldSpoofaxParser parser;
    @NotNull
    public final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory;

    public IdeaLanguageObject(@NotNull final SpoofaxIdeaLanguage ideaLanguage,
                              @NotNull final SpoofaxFileType fileType,
                              @NotNull final SpoofaxTokenTypeManager tokenTypeManager,
                              @NotNull final OldSpoofaxTokenType dummyAstTokenType,
                              @NotNull final SpoofaxParserDefinition parserDefinition,
                              @NotNull final SpoofaxLexer lexer,
                              @NotNull final OldSpoofaxParser parser,
                              @NotNull final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory) {
        this.ideaLanguage = ideaLanguage;
        this.fileType = fileType;
        this.tokenTypeManager = tokenTypeManager;
        this.dummyAstTokenType = dummyAstTokenType;
        this.parserDefinition = parserDefinition;
        this.lexer = lexer;
        this.parser = parser;
        this.syntaxHighlighterFactory = syntaxHighlighterFactory;
    }
}