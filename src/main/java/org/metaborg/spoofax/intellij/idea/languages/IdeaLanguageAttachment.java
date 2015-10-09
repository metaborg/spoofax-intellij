package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;

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
    public final SpoofaxParserDefinition parserDefinition;
    @NotNull
    public final CharacterLexer characterLexer;
    @NotNull
    public final OldSpoofaxParser parser;
    @NotNull
    public final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory;

    public IdeaLanguageAttachment(@NotNull final SpoofaxIdeaLanguage ideaLanguage,
                                  @NotNull final SpoofaxFileType fileType,
                                  @NotNull final SpoofaxTokenTypeManager tokenTypeManager,
                                  @NotNull final OldSpoofaxTokenType dummyAstTokenType,
                                  @NotNull final SpoofaxParserDefinition parserDefinition,
                                  @NotNull final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory,
                                  @NotNull final CharacterLexer characterLexer,
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