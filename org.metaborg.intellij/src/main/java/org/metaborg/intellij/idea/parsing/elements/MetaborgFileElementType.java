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

package org.metaborg.intellij.idea.parsing.elements;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.lang.*;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.*;
import com.intellij.psi.tree.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.*;
import org.metaborg.core.language.*;
import org.metaborg.core.processing.parse.*;
import org.metaborg.core.project.*;
import org.metaborg.core.syntax.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;
import org.spoofax.interpreter.terms.*;

import javax.annotation.*;

/**
 * Metaborg source file PSI element type.
 */
public final class MetaborgFileElementType extends IFileElementType { //IStubFileElementType {

    private final IIdeaLanguageManager languageManager;
    private final IIdeaProjectService projectService;
    private final ILanguageProjectService languageProjectService;
    private final SpoofaxTokenTypeManager tokenTypesManager;
    private final IIntelliJResourceService resourceService;
    private final IParseResultProcessor<IStrategoTerm> parseResultProcessor;
    private final ISyntaxService<IStrategoTerm> syntaxService;
    private final IParserConfiguration parserConfiguration;
    private final IATermAstElementTypeProviderFactory elementTypeProviderFactory;
    @InjectLogger
    private ILogger logger;

    @Inject
    public MetaborgFileElementType(
            @Assisted final Language language,
            @Assisted final SpoofaxTokenTypeManager tokenTypesManager,
            final IIdeaLanguageManager languageManager,
            final ILanguageProjectService languageProjectService,
            final IIntelliJResourceService resourceService,
            final IParseResultProcessor<IStrategoTerm> parseResultProcessor,
            final ISyntaxService<IStrategoTerm> syntaxService,
            final IParserConfiguration parserConfiguration,
            final IIdeaProjectService projectService,
            final IATermAstElementTypeProviderFactory elementTypeProviderFactory) {

        super(language);
        assert language instanceof MetaborgIdeaLanguage;

        this.languageManager = languageManager;
        this.languageProjectService = languageProjectService;
        this.tokenTypesManager = tokenTypesManager;
        this.resourceService = resourceService;
        this.parseResultProcessor = parseResultProcessor;
        this.syntaxService = syntaxService;
        this.parserConfiguration = parserConfiguration;
        this.projectService = projectService;
        this.elementTypeProviderFactory = elementTypeProviderFactory;
    }

    @Override
    protected ASTNode doParseContents(
            final ASTNode chameleon, final PsiElement psi) {
        final Project project = psi.getProject();

        final MetaborgIdeaLanguage language = getMetaborgIdeaLanguage();
        final PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(
                project,
                chameleon,
                null,
                language,
                chameleon.getChars()
        );


        @Nullable final FileObject resource = getResource(psi, builder);
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

        final MetaborgFile file = (MetaborgFile)psi;
        file.putUserData(MetaborgFile.PARSE_RESULT_KEY, parseResult);

        return rootNode;
    }

    /**
     * Gets the Metaborg IDEA language object.
     *
     * @return The language object.
     */
    public MetaborgIdeaLanguage getMetaborgIdeaLanguage() {
        return (MetaborgIdeaLanguage)getLanguage();
    }

    /**
     * Determines the resource being parsed.
     *
     * @param element The PSI element.
     * @param builder The PSI builder.
     * @return The {@link FileObject} of the parsed resource;
     * or <code>null</code> when the file exists only in-memory.
     */
    @Nullable
    private FileObject getResource(final PsiElement element, final PsiBuilder builder) {
        @Nullable final PsiFile file = element.getContainingFile();
        assert file != null : "Only non-file PSI elements (e.g. directories and packages) may have no PsiFile.";

        @Nullable final VirtualFile virtualFile = file.getOriginalFile().getVirtualFile();
        if (virtualFile == null) {
            // Only in-memory (non-physical) files have no associated virtual file.
            return null;
        }

        return this.resourceService.resolve(virtualFile);
    }

    /**
     * Determines the language implementation to use to parse this file.
     *
     * @param resource The file object of the file being parsed.
     * @param root     The root element.
     * @return The language implementation to use.
     */
    private ILanguageImpl getLanguageImpl(
            @Nullable final FileObject resource,
            final PsiElement psi,
            final IElementType root) {
        final ILanguage language =  this.languageManager.getLanguage((MetaborgIdeaLanguage)root.getLanguage());
        @Nullable final IProject project = this.projectService.get(psi);

        @Nullable final LanguageDialect dialect = this.languageProjectService.getImpl(language, project, resource);
        if (dialect == null) {
            throw LoggerUtils.exception(this.logger, IllegalStateException.class,
                    "Could not determine the language dialect of the resource: {}", resource);
        }

        return dialect.dialectOrBaseLanguage();
    }

    /**
     * Parses the whole buffer.
     *
     * @return The parse result.
     */
    private ParseResult<IStrategoTerm> parseAll(
            @Nullable final FileObject resource,
            final ILanguageImpl languageImpl,
            final String text) {
        final ParseResult<IStrategoTerm> parseResult;
        try {
            if (resource != null) {
                this.parseResultProcessor.invalidate(resource);
            }

            // FIXME: Syntax service must allow null resource.
            parseResult = this.syntaxService.parse(text, resource, languageImpl, this.parserConfiguration);

            if (resource != null) {
                this.parseResultProcessor.update(resource, parseResult);
            }
        } catch (final ParseException e) {
            if (resource != null) {
                this.parseResultProcessor.error(resource, e);
            }
            throw new MetaborgRuntimeException("Unhandled exception", e);
        }
        return parseResult;
    }

}
