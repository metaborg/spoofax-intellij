/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.parsing;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType;
import org.metaborg.intellij.idea.languages.ILanguageBindingManager;
import org.metaborg.intellij.idea.parsing.elements.IMetaborgPsiElementFactory;
import org.metaborg.intellij.idea.parsing.elements.MetaborgAesiFileElementType;
import org.metaborg.intellij.idea.parsing.elements.MetaborgFile;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenTypeManager;

/**
 * A Spoofax parser definition.
 */
@Deprecated
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
        throw new UnsupportedOperationException("See MetaborgAesiFileElementType class.");
//        final SpoofaxTokenTypeManager tokenTypeManager
//                = this.bindingManager.getTokenTypeManager(this.fileType.getMetaborgLanguage());
//        return this.characterLexerFactory.create(tokenTypeManager);
    }

    /**
     * Creates a parser for the specified project.
     *
     * @param project The project.
     * @return The parser.
     */
    @Override
    public PsiParser createParser(final Project project) {
        throw new UnsupportedOperationException("See MetaborgAesiFileElementType class.");
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