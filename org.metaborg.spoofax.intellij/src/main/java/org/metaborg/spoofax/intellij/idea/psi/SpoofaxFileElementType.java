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

package org.metaborg.spoofax.intellij.idea.psi;

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
import org.metaborg.intellij.idea.project.IIdeaProjectService;
import org.metaborg.spoofax.intellij.factories.IATermAstElementTypeProviderFactory;
import org.metaborg.spoofax.intellij.idea.languages.ILexerParserManager;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxFile;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxIdeaLanguage;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import javax.annotation.Nullable;

public class SpoofaxFileElementType extends IFileElementType { //IStubFileElementType {

    private final ILexerParserManager lexerParserManager;
    private final IIdeaProjectService projectService;
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
            final IIdeaProjectService projectService,
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

    @Override
    protected ASTNode doParseContents(
            @NotNull final ASTNode chameleon, @NotNull final PsiElement psi) {
        final Project project = psi.getProject();

        final SpoofaxIdeaLanguage language = getSpoofaxLanguage();
        final PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(
                project,
                chameleon,
                null,
                language,
                chameleon.getChars()
        );


        final FileObject resource = getResource(psi, builder);
        final ILanguageImpl languageImpl = getLanguageImpl(resource, psi, this);
        final String input = builder.getOriginalText().toString();
        final ParseResult<IStrategoTerm> parseResult = parseAll(resource, languageImpl, input);

        final AstBuilder astBuilder = new AstBuilder(
                languageImpl,
                this.elementTypeProviderFactory,
                this.tokenTypesManager
        );
        final ASTNode tree = astBuilder.build(parseResult, this, builder);

        final ASTNode rootNode = tree.getFirstChildNode();

        final SpoofaxFile file = (SpoofaxFile)psi;
        file.putUserData(SpoofaxFile.PARSE_RESULT_KEY, parseResult);
        return rootNode;
    }

    public SpoofaxIdeaLanguage getSpoofaxLanguage() {
        return (SpoofaxIdeaLanguage)getLanguage();
    }

    /**
     * Determines the resource being parsed.
     *
     * @param builder The PSI builder.
     * @return The {@link FileObject} of the parsed resource;
     * or <code>null</code> when the file exists only in memory.
     */
    @Nullable
    private FileObject getResource(final PsiElement element, @NotNull final PsiBuilder builder) {
        final PsiFile file = element.getContainingFile();
//        PsiFile file = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY);
        FileObject fileObject = null;
        if (file != null) {
            final VirtualFile virtualFile = file.getVirtualFile();
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
    private ILanguageImpl getLanguageImpl(
            @Nullable final FileObject resource,
            final PsiElement psi,
            @NotNull final IElementType root) {
        final ILanguage language = ((SpoofaxIdeaLanguage)root.getLanguage()).language();
        final IProject project = this.projectService.get(psi);

        final LanguageDialect dialect = this.languageProjectService.getImpl(language, project, resource);
        assert dialect != null;
        final ILanguageImpl languageImpl = dialect.dialectOrBaseLanguage();
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
        final ParseResult<IStrategoTerm> result;
        try {
            result = this.syntaxService.parse(input, resource, languageImpl, this.parserConfiguration);
        } catch (final ParseException e) {
            throw new MetaborgRuntimeException("Unhandled exception", e);
        }
        return result;
    }

}
