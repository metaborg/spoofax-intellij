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

package org.metaborg.spoofax.intellij.psi;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.analysis.IAnalysisService;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.*;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.project.IProject;
import org.metaborg.core.syntax.IParserConfiguration;
import org.metaborg.core.syntax.ISyntaxService;
import org.metaborg.core.syntax.ParseException;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.spoofax.intellij.factories.IATermAstElementTypeProviderFactory;
import org.metaborg.spoofax.intellij.idea.languages.ILexerParserManager;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxFile;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxIdeaLanguage;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;
import org.metaborg.spoofax.intellij.project.IIntelliJProjectService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import javax.annotation.Nullable;

public class SpoofaxFileElementType extends IFileElementType { //IStubFileElementType {

    private final ILexerParserManager lexerParserManager;
    private final IIntelliJProjectService projectService;
    private final ILanguageProjectService languageProjectService;
    private final SpoofaxTokenTypeManager tokenTypesManager;
    private final ILanguageIdentifierService identifierService;
    private final IIntelliJResourceService resourceService;
    private final ISyntaxService<IStrategoTerm> syntaxService;
    private final IAnalysisService<IStrategoTerm, IStrategoTerm> analysisService;
    private final IParserConfiguration parserConfiguration;
    private final IContextService contextService;
    private final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester;
    private final IATermAstElementTypeProviderFactory elementTypeProviderFactory;

    @Inject
    public SpoofaxFileElementType(
            @Assisted final Language language,
            @Assisted final SpoofaxTokenTypeManager tokenTypesManager,
            final ILanguageProjectService languageProjectService,
            final ILexerParserManager lexerParserManager,
            final ILanguageIdentifierService identifierService,
            final IIntelliJResourceService resourceService,
            final ISyntaxService<IStrategoTerm> syntaxService,
            final IAnalysisService<IStrategoTerm, IStrategoTerm> analysisService,
            final IParserConfiguration parserConfiguration,
            final IContextService contextService,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester,
            final IIntelliJProjectService projectService,
            final IATermAstElementTypeProviderFactory elementTypeProviderFactory) {

        super(language);
        assert language instanceof SpoofaxIdeaLanguage;
        this.languageProjectService = languageProjectService;
        this.lexerParserManager = lexerParserManager;
        this.tokenTypesManager = tokenTypesManager;
        this.identifierService = identifierService;
        this.resourceService = resourceService;
        this.syntaxService = syntaxService;
        this.analysisService = analysisService;
        this.parserConfiguration = parserConfiguration;
        this.contextService = contextService;
        this.analysisResultRequester = analysisResultRequester;
        this.projectService = projectService;
        this.elementTypeProviderFactory = elementTypeProviderFactory;
    }

    public SpoofaxIdeaLanguage getSpoofaxLanguage() {
        return (SpoofaxIdeaLanguage) getLanguage();
    }

    @Override
    protected ASTNode doParseContents(
            @NotNull final ASTNode chameleon, @NotNull final PsiElement psi) {
        Project project = psi.getProject();

        SpoofaxIdeaLanguage language = getSpoofaxLanguage();
        PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(
                project,
                chameleon,
                null,
                language,
                chameleon.getChars()
        );


        FileObject resource = getResource(psi, builder);
        ILanguageImpl languageImpl = getLanguageImpl(resource, psi, this);
        String input = builder.getOriginalText().toString();
        ParseResult<IStrategoTerm> parseResult = parseAll(resource, languageImpl, input);
//        psi.getContainingFile().
//        AnalysisResult<IStrategoTerm, IStrategoTerm> analysisResult = analyzeAll(
//                parseResult,
//                resource,
//                languageImpl
//        );
//        AnalysisFileResult<IStrategoTerm, IStrategoTerm> analysisFileResult = null;
//        for (AnalysisFileResult<IStrategoTerm, IStrategoTerm> fileResult : analysisResult.fileResults) {
//            if (fileResult.source.equals(resource)) {
//                analysisFileResult = fileResult;
//                break;
//            }
//        }
//        AnalysisFileResult<IStrategoTerm, IStrategoTerm> analysisFileResult = this.analysisResultRequester.get(
//                resource);


        AstBuilder astBuilder = new AstBuilder(languageImpl, this.elementTypeProviderFactory, this.tokenTypesManager);
        ASTNode tree = astBuilder.build(parseResult, this, builder);

        ASTNode rootNode = tree.getFirstChildNode();

        SpoofaxFile file = (SpoofaxFile) psi;
        file.putUserData(SpoofaxFile.PARSE_RESULT_KEY, parseResult);
//        file.putUserData(SpoofaxFile.ANALYSIS_FILE_RESULT_KEY, analysisFileResult);
        return rootNode;
    }

    /**
     * Determines the resource being parsed.
     *
     * @param builder The PSI builder.
     * @return The {@FileObject} of the parsed resource;
     * or <code>null</code> when the file exists only in memory.
     */
    @Nullable
    private FileObject getResource(final PsiElement element, @NotNull final PsiBuilder builder) {
        PsiFile file = element.getContainingFile();
//        PsiFile file = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY);
        FileObject fileObject = null;
        if (file != null) {
            VirtualFile virtualFile = file.getVirtualFile();
            if (virtualFile != null) {
                fileObject = this.resourceService.resolve(virtualFile);
            }
        }
        return fileObject;
    }

    /**
     * Determines the language implementation to use to parse this file.
     *
     * @param resource The file object of the file being parsed.
     * @param root     The root element.
     * @return The language implementation to use.
     */
    @NotNull
    private ILanguageImpl getLanguageImpl(@Nullable final FileObject resource, final PsiElement psi, @NotNull final IElementType root) {
        ILanguage language = ((SpoofaxIdeaLanguage) root.getLanguage()).language();
        IProject project = this.projectService.get(psi);

        LanguageDialect dialect = this.languageProjectService.getImpl(language, project, resource);
        assert dialect != null;
        ILanguageImpl languageImpl = dialect.dialectOrBaseLanguage();
//        if (resource != null) {
//            this.languageProjectService.getImpl(project, language, resource);
//            languageImpl = identifierService.identify(resource);
//        }
//        if (languageImpl == null) {
//            SpoofaxIdeaLanguage language = (SpoofaxIdeaLanguage) root.getLanguage();
//
//            // TODO: Pick a language implementation when the file/implementation is not known?
//            throw new UnsupportedOperationException();
//        }
        assert languageImpl != null;
        return languageImpl;
    }

    /**
     * Parses the whole buffer.
     *
     * @return The parse result.
     */
    private ParseResult<IStrategoTerm> parseAll(
            final FileObject resource,
            final ILanguageImpl languageImpl,
            final String input) {
        ParseResult<IStrategoTerm> result;
        try {
            result = this.syntaxService.parse(input, resource, languageImpl, this.parserConfiguration);
        } catch (ParseException e) {
            throw new MetaborgRuntimeException("Unhandled exception", e);
        }
        return result;
    }

//    private AnalysisResult<IStrategoTerm, IStrategoTerm> analyzeAll(
//            ParseResult<IStrategoTerm> parseResult,
//            FileObject resource,
//            ILanguageImpl languageImpl) {
//        try {
//            IContext context = this.contextService.get(resource, languageImpl);
//            return this.analysisService.analyze(Iterables2.singleton(parseResult), context);
//        } catch (ContextException | AnalysisException e) {
//            throw new RuntimeException("Unhandled exception: ", e);
//        }
//    }

//    /**
//     * Builds a PSI AST.
//     */
//    private static class SpoofaxAstBuilder {
//
//        @NotNull
//        private final SpoofaxTokenTypeManager tokenTypesManager;
//        @Nullable
//        private final ParseResult<IStrategoTerm> result;
//        @NotNull
//        private final PsiBuilder builder;
//        @NotNull
//        private final IElementType root;
//        @NotNull
//        private final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizerService;
//
//        /**
//         * Initializes a new instance of the {@link SpoofaxAstBuilder} class.
//         *
//         * @param result            The parse result.
//         * @param root              The root element type.
//         * @param builder           The PSI builder.
//         * @param tokenTypesManager The token types manager.
//         */
//        public SpoofaxAstBuilder(
//                @Nullable final ParseResult<IStrategoTerm> result,
//                @NotNull final IElementType root,
//                @NotNull final PsiBuilder builder,
//                @NotNull final SpoofaxTokenTypeManager tokenTypesManager,
//                @NotNull final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizerService) {
//            this.result = result;
//            this.root = root;
//            this.builder = builder;
//            this.tokenTypesManager = tokenTypesManager;
//            this.categorizerService = categorizerService;
//        }
//
//        /**
//         * Builds the PSI AST from the parse result.
//         *
//         * @return The PSI AST tree root.
//         */
//        @NotNull
//        public ASTNode build() {
//
//            PsiBuilder.Marker m = builder.mark();
//            if (this.result != null && this.result.result != null) {
//                buildTermIterative(this.result.result);
//            } else {
//                // We have no parse result. Therefore,
//                // parse a single element for all tokens.
//                PsiBuilder.Marker m2 = builder.mark();
//                while (!builder.eof()) {
//                    builder.advanceLexer();
//                }
//                m2.done(this.tokenTypesManager.getDummySpoofaxTokenType());
//            }
//            m.done(this.root);
//
//            ASTNode astNode = builder.getTreeBuilt();
////            astNode.putUserData(SpoofaxFile.PARSE_RESULT_KEY, this.result);
//            return astNode;
//        }
//
//        /**
//         * Builds the AST for the specified term, iteratively.
//         *
//         * @param root The root term.
//         */
//        private void buildTermIterative(@NotNull final IStrategoTerm root) {
//            Stack<TermTask> tasks = new Stack<>();
//            tasks.push(new TermTask(root));
//
//            while (!tasks.empty()) {
//                TermTask task = tasks.pop();
//                IStrategoTerm term = task.term;
//                PsiBuilder.Marker marker = task.marker;
//                if (marker == null) {
//                    // Start
//                    marker = buildTermStart(term);
//                    tasks.push(new TermTask(term, marker));
//
//                    IStrategoTerm[] subterms = term.getAllSubterms();
//                    for (int i = subterms.length - 1; i >= 0; i--) {
//                        tasks.push(new TermTask(subterms[i]));
//                    }
//                } else {
//                    // End
//                    buildTermEnd(term, marker);
//                }
//            }
//        }
//
//        /**
//         * Builds the AST for the specified term, recursively.
//         *
//         * @param term The term.
//         */
//        private void buildTermRecursive(@NotNull final IStrategoTerm term) {
//            PsiBuilder.Marker marker = buildTermStart(term);
//
//            IStrategoTerm[] subterms = term.getAllSubterms();
//            for (int i = 0; i < subterms.length; i++) {
//                // Recurse
//                buildTermRecursive(subterms[i]);
//            }
//
//            buildTermEnd(term, marker);
//        }
//
//        /**
//         * Builds the start of a term.
//         *
//         * @param term The term.
//         * @return The resulting marker.
//         */
//        @NotNull
//        private PsiBuilder.Marker buildTermStart(@NotNull final IStrategoTerm term) {
//            moveToStart(term);
//            PsiBuilder.Marker marker = builder.mark();
//            return marker;
//        }
//
//        /**
//         * Builds the end of a term.
//         *
//         * @param term   The term.
//         * @param marker The marker.
//         */
//        private void buildTermEnd(@NotNull final IStrategoTerm term, @NotNull final PsiBuilder.Marker marker) {
//            // TODO: Pick an element type!
//            IElementType elementType = this.tokenTypesManager.getDummySpoofaxTokenType();
//
//            moveToEnd(term);
//            marker.done(elementType);
//        }
//
//        /**
//         * Moves to the start of a term.
//         *
//         * @param term The term.
//         */
//        private void moveToStart(@NotNull final IStrategoTerm term) {
//            final ImploderAttachment imploderAttachment = ImploderAttachment.get(term);
//            int start = imploderAttachment.getLeftToken().getStartOffset();
//            moveTo(start);
//        }
//
//        /**
//         * Moves to the end of a term.
//         *
//         * @param term The term.
//         */
//        private void moveToEnd(@NotNull final IStrategoTerm term) {
//            final ImploderAttachment imploderAttachment = ImploderAttachment.get(term);
//            int end = imploderAttachment.getRightToken().getEndOffset() + 1;
//            moveTo(end);
//        }
//
//        /**
//         * Move the builder to the specified offset.
//         *
//         * @param offset The target offset.
//         */
//        private void moveTo(int offset) {
//            // We assume the builder to be _before_ the target offset.
//            while (this.builder.getCurrentOffset() < offset) {
//                this.builder.advanceLexer();
//            }
//            // We assume the lexer to have a 1 character step increments,
//            // so we can't overshoot our target.
//            assert this.builder.getCurrentOffset() == offset;
//        }
//
//        private static class TermTask {
//            public final IStrategoTerm term;
//            @Nullable
//            public final PsiBuilder.Marker marker;
//
//            public TermTask(IStrategoTerm term, PsiBuilder.Marker marker) {
//                this.term = term;
//                this.marker = marker;
//            }
//
//            public TermTask(IStrategoTerm term) {
//                this(term, null);
//            }
//        }
//    }
}
