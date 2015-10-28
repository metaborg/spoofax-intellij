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