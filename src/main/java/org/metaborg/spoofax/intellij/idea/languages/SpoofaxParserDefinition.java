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
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.IProjectLanguageIdentifierService;

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
    @NotNull
    private final IProjectLanguageIdentifierService languageIdentifierService;

    @Inject
    private SpoofaxParserDefinition(@Assisted @NotNull final SpoofaxFileType fileType,
                                    @NotNull final ILexerParserManager lexerParserManager,
                                    @NotNull final IProjectLanguageIdentifierService languageIdentifierService) {
        this.fileType = fileType;
        this.fileElement = new IFileElementType(fileType.getLanguage());
        this.languageIdentifierService = languageIdentifierService;
        this.lexerParserManager = lexerParserManager;
    }

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        // TODO: Project!
        final ILanguageImpl implementation = this.languageIdentifierService.identify(this.fileType.getSpoofaxLanguage(),
                                                                                     null);
        return this.lexerParserManager.getLexer(implementation);
    }

    @Override
    public PsiParser createParser(Project project) {
        // TODO: Project!
        final ILanguageImpl implementation = this.languageIdentifierService.identify(this.fileType.getSpoofaxLanguage(),
                                                                                     null);
        return this.lexerParserManager.getParser(implementation);
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
        IElementType type = node.getElementType();
        if (type instanceof OldSpoofaxTokenType) {
            return new SpoofaxPsiElement(node);
        }
        throw new AssertionError("Unknown element type: " + type);
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