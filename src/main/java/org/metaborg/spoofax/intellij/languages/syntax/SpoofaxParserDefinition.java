package org.metaborg.spoofax.intellij.languages.syntax;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.language.LanguageUtils;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.core.style.ICategorizerService;
import org.metaborg.core.style.IStylerService;
import org.metaborg.core.syntax.ISyntaxService;
import org.metaborg.spoofax.core.syntax.JSGLRParserConfiguration;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.languages.IdeaLanguageManager;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.metaborg.spoofax.intellij.languages.SpoofaxFileType;
import org.spoofax.interpreter.terms.IStrategoTerm;

public final class SpoofaxParserDefinition implements ParserDefinition {

//    @NotNull protected abstract Lexer getLexer();
//    @NotNull protected abstract ISpoofaxParser getParser();
//    @NotNull protected abstract SpoofaxFileType getFileType();
//    @NotNull protected abstract IFileElementType getFileElement();

//    public static final IFileElementType FILE = new IFileElementType(Language.<SpoofaxLanguage>findInstance(
//            SpoofaxLanguage.class));

//    private final ISyntaxService<IStrategoTerm> syntaxService;
//    private final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizer;
//    private final IStylerService<IStrategoTerm, IStrategoTerm> styler;
//    private final SpoofaxTokenTypeManager tokenTypeManager;
//    private final IResourceService resourceService;
//    private final ILanguageDiscoveryService discovery;
//    private final IStylerService<IStrategoTerm, IStrategoTerm> styler;

    @NotNull private Lexer lexer;
    @NotNull private ISpoofaxParser parser;
    @NotNull private SpoofaxFileType fileType;
    @NotNull private IFileElementType fileElement;

    public SpoofaxParserDefinition(@NotNull final SpoofaxFileType fileType, @NotNull final Lexer lexer, @NotNull final ISpoofaxParser parser) {
        this.fileType = fileType;
        this.lexer = lexer;
        this.parser = parser;
        this.fileElement = new IFileElementType(fileType.getLanguage());
    }

//    /**
//     * This instance is created by IntelliJ's plugin system.
//     * Do not call this method manually.
//     */
//    public SpoofaxParserDefinition() {
//        IdeaPlugin.injector().injectMembers(this);
//    }
//
//    @Inject
//    @SuppressWarnings("unused")
//    private final void inject(@NotNull final SpoofaxFileType fileType, @NotNull final Lexer lexer, @NotNull final ISpoofaxParser parser) {
//        this.fileType = fileType;
//        this.lexer = lexer;
//        this.parser = parser;
//        this.fileElement = new IFileElementType(fileType.getLanguage());
//    }

    @NotNull
    @Override
    public Lexer createLexer(Project project) {

        return this.lexer;
//
//        FileObject location = this.resourceService.resolve("file:///home/daniel/repos/sdf-master/org.strategoxt.imp.editors.template/");
//        //FileObject location = resourceService.resolve("file:///home/daniel/eclipse/spoofax1507/workspace");
//        //ILanguageDiscoveryService discovery = SpoofaxPlugin.getInstance(ILanguageDiscoveryService.class);
//        ILanguageImpl languageImpl = null;
//        try {
//            languageImpl = LanguageUtils.toImpls(this.discovery.discover(location)).iterator().next();
//        } catch (Exception e) { }
//        JSGLRParserConfiguration configuration = new JSGLRParserConfiguration(
//            /* implode    */ true,
//            /* recovery   */ true,
//            /* completion */ false,
//            /* timeout    */ 30000);
//
//        assert languageImpl != null;
//
//        return new SpoofaxLexer(syntaxService, categorizer, styler, tokenTypeManager, languageImpl, configuration);
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