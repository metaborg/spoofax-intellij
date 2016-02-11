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

package org.metaborg.intellij.idea.parsing;

import com.google.inject.*;
import com.intellij.lexer.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.parsing.elements.*;
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
     *
     * @param resourceService
     * @param identifierService
     * @param highlightingLexerFactory
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
