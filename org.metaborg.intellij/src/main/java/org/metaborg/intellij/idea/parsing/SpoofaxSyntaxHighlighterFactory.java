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
import com.intellij.lexer.Lexer;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.intellij.idea.languages.ILanguageBindingManager;
import org.metaborg.intellij.idea.languages.MetaborgIdeaLanguage;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenTypeManager;
import org.metaborg.intellij.resources.IIntelliJResourceService;

import javax.annotation.Nullable;

/**
 * Factory for the {@link SpoofaxSyntaxHighlighter} class.
 */
public final class SpoofaxSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    private final IIntelliJResourceService resourceService;
    private final ILanguageIdentifierService identifierService;
    private final IHighlightingLexerFactory highlightingLexerFactory;
    private final IIdeaLanguageManager languageManager;
    private final ILanguageBindingManager bindingManager;

    /**
     * Initializes a new instance of the {@link SpoofaxSyntaxHighlighterFactory} class.
     */
    @Inject
    public SpoofaxSyntaxHighlighterFactory(
            final IIntelliJResourceService resourceService,
            final ILanguageIdentifierService identifierService,
            final IHighlightingLexerFactory highlightingLexerFactory,
            final IIdeaLanguageManager languageManager,
            final ILanguageBindingManager bindingManager) {
        super();
        this.resourceService = resourceService;
        this.identifierService = identifierService;
        this.highlightingLexerFactory = highlightingLexerFactory;
        this.languageManager = languageManager;
        this.bindingManager = bindingManager;
    }

    /**
     * Gets the syntax highlighter for the specified project and file.
     *
     * @param project     The project.
     * @param virtualFile The file.
     * @return The syntax highlighter.
     */
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(
            final Project project,
            final VirtualFile virtualFile) {

        @Nullable ILanguageImpl implementation = null;
        @Nullable final FileObject file = this.resourceService.resolve(virtualFile);

        if (file != null) {
            implementation = this.identifierService.identify(file);
        }
        else if (virtualFile instanceof LightVirtualFile) {
            final com.intellij.lang.Language ideaLanguage = ((LightVirtualFile)virtualFile).getLanguage();
            if (ideaLanguage instanceof MetaborgIdeaLanguage) {
                final ILanguage language = this.languageManager.getLanguage((MetaborgIdeaLanguage)ideaLanguage);
                implementation = language.activeImpl();
            }
        }

        if (implementation == null){
            // FIXME: What to do? Can I return null to get the default highlighting?
            return null;
        }

        @Nullable final IProject metaborgProject = null;  // FIXME: Get IProject from Project
        final SpoofaxTokenTypeManager tokenTypesManager =
                this.bindingManager.getTokenTypeManager(implementation.belongsTo());
        final Lexer lexer = this.highlightingLexerFactory
                .create(file, metaborgProject, implementation, tokenTypesManager);

        return new SpoofaxSyntaxHighlighter(lexer);
    }
}
