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
import org.metaborg.core.language.ILanguage;
import org.metaborg.spoofax.intellij.factories.ICharacterLexerFactory;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxAnnotator;
import org.metaborg.spoofax.intellij.idea.vfs.SpoofaxFileType;

/**
 * Stores the IntelliJ IDEA objects that are associated with a particular {@link ILanguage}.
 */
public final class IdeaLanguageAttachment {

    private final SpoofaxIdeaLanguage ideaLanguage;
    private final SpoofaxFileType fileType;
    private final SpoofaxTokenTypeManager tokenTypeManager;
    private final ParserDefinition parserDefinition;
    private final ICharacterLexerFactory characterLexerFactory;
    private final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory;
    private final SpoofaxAnnotator spoofaxAnnotator;

    public SpoofaxIdeaLanguage ideaLanguage() { return this.ideaLanguage; }

    public SpoofaxFileType fileType() { return this.fileType; }

    public SpoofaxTokenTypeManager tokenTypeManager() { return this.tokenTypeManager; }

    public ParserDefinition parserDefinition() { return this.parserDefinition; }

    public ICharacterLexerFactory characterLexerFactory() { return this.characterLexerFactory; }

    public SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory() { return this.syntaxHighlighterFactory; }

    public SpoofaxAnnotator spoofaxAnnotator() { return this.spoofaxAnnotator; }

    /**
     * Initializes a new instance of the {@link IdeaLanguageAttachment} class.
     *
     * @param ideaLanguage             The IDEA language.
     * @param fileType                 The file type.
     * @param tokenTypeManager         The token type manager.
     * @param parserDefinition         The parser definition.
     * @param syntaxHighlighterFactory The syntax highlighter factory.
     * @param characterLexerFactory    The character lexer factory.
     */
    /* package private */ IdeaLanguageAttachment(
            final SpoofaxIdeaLanguage ideaLanguage,
            final SpoofaxFileType fileType,
            final SpoofaxTokenTypeManager tokenTypeManager,
            final ParserDefinition parserDefinition,
            final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory,
            final ICharacterLexerFactory characterLexerFactory,
            final SpoofaxAnnotator spoofaxAnnotator) {
        this.ideaLanguage = ideaLanguage;
        this.fileType = fileType;
        this.tokenTypeManager = tokenTypeManager;
        this.parserDefinition = parserDefinition;
        this.syntaxHighlighterFactory = syntaxHighlighterFactory;
        this.characterLexerFactory = characterLexerFactory;
        this.spoofaxAnnotator = spoofaxAnnotator;
    }
}