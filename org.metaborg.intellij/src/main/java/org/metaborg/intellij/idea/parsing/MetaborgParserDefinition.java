/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.idea.parsing;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.lang.*;
import com.intellij.lexer.*;
import com.intellij.openapi.project.*;
import com.intellij.psi.*;
import com.intellij.psi.tree.*;
import org.metaborg.intellij.idea.filetypes.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.parsing.elements.*;

/**
 * A Spoofax parser definition.
 */
public final class MetaborgParserDefinition implements ParserDefinition {

    private final MetaborgLanguageFileType fileType;
    private final IFileElementType fileElement;
    private final IMetaborgPsiElementFactory psiElementFactory;
    private final ICharacterLexerFactory characterLexerFactory;
    private final ILanguageBindingManager bindingManager;

    @Inject
    /* package private */ MetaborgParserDefinition(
            @Assisted final MetaborgLanguageFileType fileType,
            @Assisted final IFileElementType fileElementType,
            final IMetaborgPsiElementFactory psiElementFactory,
            final ICharacterLexerFactory characterLexerFactory,
            final ILanguageBindingManager bindingManager) {
        this.fileType = fileType;
        this.fileElement = fileElementType;
        this.psiElementFactory = psiElementFactory;
        this.characterLexerFactory = characterLexerFactory;
        this.bindingManager = bindingManager;
    }

    /**
     * Creates a lexer for the specified project.
     *
     * @param project The project.
     * @return The lexer.
     */
    @Override
    public Lexer createLexer(final Project project) {
        final SpoofaxTokenTypeManager tokenTypeManager
                = this.bindingManager.getTokenTypeManager(this.fileType.getMetaborgLanguage());
        return this.characterLexerFactory.create(tokenTypeManager);
    }

    /**
     * Creates a parser for the specified project.
     *
     * @param project The project.
     * @return The parser.
     */
    @Override
    public PsiParser createParser(final Project project) {
        throw new UnsupportedOperationException("See SpoofaxFileElementType class.");
    }

    /**
     * Gets the type of file node.
     *
     * @return The file node type.
     */
    @Override
    public IFileElementType getFileNodeType() {
        return this.fileElement;
    }

    /**
     * Gets a set of whitespace tokens.
     *
     * @return A set of whitespace tokens.
     */
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.EMPTY;
    }

    /**
     * Gets a set of comment tokens.
     *
     * @return A set of comment tokens.
     */
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    /**
     * Gets a set of string literal tokens.
     *
     * @return A set of string literal tokens.
     */
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    /**
     * Creates a PSI element for the specified AST node.
     *
     * @param node The AST node.
     * @return The PSI element.
     */
    @Override
    public PsiElement createElement(final ASTNode node) {
        return this.psiElementFactory.create(node);
    }

    /**
     * Creates a file element for the specified file view provider.
     *
     * @param viewProvider The file view provider.
     * @return The PSI file element.
     */
    @Override
    public PsiFile createFile(final FileViewProvider viewProvider) {
        return new MetaborgFile(viewProvider, this.fileType);
    }

    /**
     * Gets whether space may exist between two nodes.
     *
     * @param left  The left AST node.
     * @param right The right AST node.
     * @return A member of the {@link SpaceRequirements} enum.
     */
    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(
            final ASTNode left,
            final ASTNode right) {
        return SpaceRequirements.MAY;
    }
}