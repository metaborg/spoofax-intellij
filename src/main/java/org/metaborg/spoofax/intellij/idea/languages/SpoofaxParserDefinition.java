package org.metaborg.spoofax.intellij.idea.languages;

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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * A Spoofax parser definition.
 */
public final class SpoofaxParserDefinition implements ParserDefinition {

    @NotNull
    private final SpoofaxFileType fileType;
    @NotNull
    private final IFileElementType fileElement;
    @NotNull
    private final ILexerParserManager lexerParserManager;

    @Inject
    private SpoofaxParserDefinition(@Assisted @NotNull final SpoofaxFileType fileType,
                                    @NotNull final ILexerParserManager lexerParserManager) {
        this.fileType = fileType;
        this.fileElement = new IFileElementType(fileType.getLanguage());
        this.lexerParserManager = lexerParserManager;
    }

    /**
     * Creates a lexer for the specified project.
     *
     * @param project The project.
     * @return The lexer.
     */
    @NotNull
    @Override
    public Lexer createLexer(@NotNull final Project project) {
        return this.lexerParserManager.getCharacterLexer(this.fileType.getSpoofaxLanguage());
    }

    /**
     * Creates a parser for the specified project.
     *
     * @param project The project.
     * @return The parser.
     */
    @Override
    public PsiParser createParser(@NotNull final Project project) {
        return this.lexerParserManager.getParser(this.fileType.getSpoofaxLanguage());
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
    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.EMPTY;
    }

    /**
     * Gets a set of comment tokens.
     *
     * @return A set of comment tokens.
     */
    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    /**
     * Gets a set of string literal tokens.
     *
     * @return A set of string literal tokens.
     */
    @NotNull
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
    @NotNull
    @Override
    public PsiElement createElement(@NotNull final ASTNode node) {
        IElementType type = node.getElementType();
        if (type instanceof OldSpoofaxTokenType) {
            return new SpoofaxPsiElement(node);
        }
        throw new AssertionError("Unknown element type: " + type);
    }

    /**
     * Creates a file element for the specified file view provider.
     *
     * @param viewProvider The file view provider.
     * @return The PSI file element.
     */
    @Override
    @NotNull
    public PsiFile createFile(@NotNull final FileViewProvider viewProvider) {
        return new SpoofaxFile(viewProvider, this.fileType);
    }

    /**
     * Gets whether space may exist between two nodes.
     *
     * @param left  The left AST node.
     * @param right The right AST node.
     * @return A member of the {@link SpaceRequirements} enum.
     */
    @Override
    @NotNull
    public SpaceRequirements spaceExistanceTypeBetweenTokens(@NotNull final ASTNode left,
                                                             @NotNull final ASTNode right) {
        return SpaceRequirements.MAY;
    }
}