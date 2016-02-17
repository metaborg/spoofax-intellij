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
import org.metaborg.core.project.*;
import org.metaborg.core.syntax.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.languages.*;
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
            final String input) {
        final ParseResult<IStrategoTerm> result;
        try {
            // FIXME: Syntax service must allow null resource.
            result = this.syntaxService.parse(input, resource, languageImpl, this.parserConfiguration);
        } catch (final ParseException e) {
            throw new MetaborgRuntimeException("Unhandled exception", e);
        }
        return result;
    }

}
