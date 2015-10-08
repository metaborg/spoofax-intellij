package org.metaborg.spoofax.intellij.idea.languages;

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
import org.jetbrains.annotations.NotNull;

/**
 * A Spoofax parser definition.
 */
public final class SpoofaxParserDefinition implements ParserDefinition {

    @NotNull private Lexer lexer;
    @NotNull private ISpoofaxParser parser;
    @NotNull private SpoofaxFileType fileType;
    @NotNull private IFileElementType fileElement;
    @NotNull private ILexerParserManager lexerParserManager;
    //@NotNull private IProjectLanguageIdentifierService languageIdentifierService;

    public SpoofaxParserDefinition(@NotNull final SpoofaxFileType fileType, @NotNull final Lexer lexer, @NotNull final ISpoofaxParser parser/*, @NotNull final ILexerParserManager lexerParserManager*/) {
        this.fileType = fileType;
        this.lexer = lexer;
        this.parser = parser;
        this.fileElement = new IFileElementType(fileType.getLanguage());
        this.lexerParserManager = lexerParserManager;
    }

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return this.lexer;
        //return this.lexerParserManager.getLexer(fileType.getSpoofaxLanguage());
    }

    @Override
    public PsiParser createParser(Project project) {
        return this.parser;
    }

    @Override
    public IFileElementType getFileNodeType() {
        return this.fileElement;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return this.parser.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SpoofaxFile(viewProvider, this.fileType);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}