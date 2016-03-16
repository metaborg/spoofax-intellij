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

import com.google.inject.*;
import com.intellij.lexer.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.languages.*;
import org.metaborg.intellij.resources.*;

import javax.annotation.*;

/**
 * Factory for the {@link SpoofaxSyntaxHighlighter} class.
 */
public final class SpoofaxSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    private final IIntelliJResourceService resourceService;
    private final ILanguageIdentifierService identifierService;
    private final IHighlightingLexerFactory highlightingLexerFactory;
    private final ILanguageManager languageManager;
    private final ILanguageBindingManager bindingManager;

    /**
     * Initializes a new instance of the {@link SpoofaxSyntaxHighlighterFactory} class.
     */
    @Inject
    public SpoofaxSyntaxHighlighterFactory(
            final IIntelliJResourceService resourceService,
            final ILanguageIdentifierService identifierService,
            final IHighlightingLexerFactory highlightingLexerFactory,
            final ILanguageManager languageManager,
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
        final FileObject file = this.resourceService.resolve(virtualFile);
        @Nullable final ILanguageImpl implementation = this.identifierService.identify(file);

        if (implementation == null) {
            // FIXME: What to do? Can I return null to get the default highlighting?
            return null;
        }

        final IProject metaborgProject = null;  // FIXME: Get IProject from Project
        final SpoofaxTokenTypeManager tokenTypesManager =
                this.bindingManager.getTokenTypeManager(implementation.belongsTo());
        final Lexer lexer = this.highlightingLexerFactory
                .create(file, metaborgProject, implementation, tokenTypesManager);

        return new SpoofaxSyntaxHighlighter(lexer);
    }
}
