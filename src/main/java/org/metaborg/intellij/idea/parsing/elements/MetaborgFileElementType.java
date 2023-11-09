/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.metaborg.intellij.idea.parsing.elements;


import com.google.inject.assistedinject.Assisted;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.syntax.ParseException;
import org.metaborg.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.intellij.idea.languages.ILanguageProjectService;
import org.metaborg.intellij.idea.languages.LanguageDialect;
import org.metaborg.intellij.idea.languages.MetaborgIdeaLanguage;
import org.metaborg.intellij.idea.projects.IIdeaProjectService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.core.processing.parse.ISpoofaxParseResultProcessor;
import org.metaborg.spoofax.core.syntax.ISpoofaxSyntaxService;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnitService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.util.log.ILogger;

import jakarta.annotation.Nullable;

/**
 * Metaborg source file PSI element type.
 */
public final class MetaborgFileElementType extends IFileElementType { // IStubFileElementType {
    private final IIdeaLanguageManager languageManager;
    private final IIdeaProjectService projectService;
    private final ILanguageProjectService languageProjectService;
    private final SpoofaxTokenTypeManager tokenTypesManager;
    private final IIntelliJResourceService resourceService;
    private final ISpoofaxParseResultProcessor parseResultProcessor;
    private final ISpoofaxInputUnitService unitService;
    private final ISpoofaxSyntaxService syntaxService;
    private final IATermAstElementTypeProviderFactory elementTypeProviderFactory;
    @InjectLogger
    private ILogger logger;


    @jakarta.inject.Inject @javax.inject.Inject
    public MetaborgFileElementType(@Assisted final Language language,
                                   @Assisted final SpoofaxTokenTypeManager tokenTypesManager, final IIdeaLanguageManager languageManager,
                                   final ILanguageProjectService languageProjectService, final IIntelliJResourceService resourceService,
                                   final ISpoofaxParseResultProcessor parseResultProcessor, ISpoofaxInputUnitService unitService,
                                   final ISpoofaxSyntaxService syntaxService, final IIdeaProjectService projectService,
                                   final IATermAstElementTypeProviderFactory elementTypeProviderFactory) {
        super(language);
        assert language instanceof MetaborgIdeaLanguage;

        this.languageManager = languageManager;
        this.languageProjectService = languageProjectService;
        this.tokenTypesManager = tokenTypesManager;
        this.resourceService = resourceService;
        this.parseResultProcessor = parseResultProcessor;
        this.unitService = unitService;
        this.syntaxService = syntaxService;
        this.projectService = projectService;
        this.elementTypeProviderFactory = elementTypeProviderFactory;
    }


    @Override protected ASTNode doParseContents(final ASTNode chameleon, final PsiElement psi) {
        final Project project = psi.getProject();

        final MetaborgIdeaLanguage language = getMetaborgIdeaLanguage();
        final PsiBuilder builder =
            PsiBuilderFactory.getInstance().createBuilder(project, chameleon, null, language, chameleon.getChars());


        @Nullable final FileObject resource = getResource(psi, builder);
        final ILanguageImpl languageImpl = getLanguageImpl(resource, psi, this);

        final String text = builder.getOriginalText().toString();
        final ISpoofaxInputUnit input = unitService.inputUnit(resource, text, languageImpl, null);
        final ISpoofaxParseUnit parseResult = parseAll(input);

        final AstBuilder astBuilder =
            new AstBuilder(languageImpl, this.elementTypeProviderFactory, this.tokenTypesManager);
        final ASTNode tree = astBuilder.build(parseResult, this, builder);

        final ASTNode rootNode = tree.getFirstChildNode();

        final MetaborgFile file = (MetaborgFile) psi;
        file.putUserData(MetaborgFile.PARSE_RESULT_KEY, parseResult);

        return rootNode;
    }

    /**
     * Gets the Metaborg IDEA language object.
     *
     * @return The language object.
     */
    public MetaborgIdeaLanguage getMetaborgIdeaLanguage() {
        return (MetaborgIdeaLanguage) getLanguage();
    }

    /**
     * Determines the resource being parsed.
     *
     * @param element
     *            The PSI element.
     * @param builder
     *            The PSI builder.
     * @return The {@link FileObject} of the parsed resource; or <code>null</code> when the file exists only in-memory.
     */
    @Nullable private FileObject getResource(final PsiElement element, final PsiBuilder builder) {
        @Nullable final PsiFile file = element.getContainingFile();
        assert file != null : "Only non-file PSI elements (e.g. directories and packages) may have no PsiFile.";

        return this.resourceService.resolve(file);
    }

    /**
     * Determines the language implementation to use to parse this file.
     *
     * @param resource
     *            The file object of the file being parsed.
     * @param root
     *            The root element.
     * @return The language implementation to use.
     */
    private ILanguageImpl getLanguageImpl(@Nullable final FileObject resource,
                                          final PsiElement psi,
                                          final IElementType root) {
        final ILanguage language = this.languageManager.getLanguage((MetaborgIdeaLanguage) root.getLanguage());
        @Nullable final IProject project;
        if (resource != null)
            project = this.projectService.get(resource);
        else
            project = this.projectService.get(psi);

        @Nullable final LanguageDialect dialect = this.languageProjectService.getImpl(language, project, resource);
        if(dialect == null) {
            throw LoggerUtils2.exception(this.logger, IllegalStateException.class,
                "Could not determine the language dialect of the resource: {}", resource);
        }

        return dialect.dialectOrBaseLanguage();
    }

    /**
     * Parses the whole buffer.
     *
     * @return The parse result.
     */
    private ISpoofaxParseUnit parseAll(final ISpoofaxInputUnit input) {
        final ISpoofaxParseUnit parseResult;
        final FileObject resource = input.source();
        try {
            if(!input.detached()) {
                this.parseResultProcessor.invalidate(resource);
            }

            parseResult = this.syntaxService.parse(input);

            if(!input.detached()) {
                this.parseResultProcessor.update(resource, parseResult);
            }
        } catch(final ParseException e) {
            if(!input.detached()) {
                this.parseResultProcessor.error(resource, e);
            }
            throw new MetaborgRuntimeException("Unhandled exception", e);
        }
        return parseResult;
    }

}
