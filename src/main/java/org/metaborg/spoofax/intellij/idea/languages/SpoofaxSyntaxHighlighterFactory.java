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

package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

/**
 * Factory for the {@link SpoofaxSyntaxHighlighter} class.
 */
public final class SpoofaxSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    @NotNull
    private final IIntelliJResourceService resourceService;
    @NotNull
    private final ILanguageIdentifierService identifierService;
    @NotNull
    private final ILexerParserManager lexerParserManager;

    @Inject
    /* package private */ SpoofaxSyntaxHighlighterFactory(
            @NotNull final IIntelliJResourceService resourceService,
            @NotNull final ILanguageIdentifierService identifierService,
            @NotNull final ILexerParserManager lexerParserManager) {
        this.resourceService = resourceService;
        this.identifierService = identifierService;
        this.lexerParserManager = lexerParserManager;
    }

    /**
     * Gets the syntax highlighter for the specified project and file.
     *
     * @param project     The project.
     * @param virtualFile The file.
     * @return The syntax highlighter.
     */
    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(
            @NotNull final Project project,
            @NotNull final VirtualFile virtualFile) {
        FileObject file = this.resourceService.resolve(virtualFile);
        ILanguageImpl implementation = this.identifierService.identify(file);
        Lexer lexer = this.lexerParserManager.getHighlightingLexer(implementation);

        return new SpoofaxSyntaxHighlighter(lexer);
    }
}
