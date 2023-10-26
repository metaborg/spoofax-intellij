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


import com.intellij.lexer.Lexer;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighterFactory;
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
import org.metaborg.intellij.idea.NotificationUtils;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenTypeManager;
import org.metaborg.intellij.resources.IIntelliJResourceService;

import jakarta.annotation.Nullable;

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
    @jakarta.inject.Inject @javax.inject.Inject
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
            try {
                implementation = this.identifierService.identify(file);
            } catch (IllegalStateException ex) {
                // Multiple possible languages identified
                // (e.g. when different languages have the same extension,
                // such as TypeScript and TS both having .ts extension)
                // TODO: Better error message,
                // but to do that the exception needs to have its own class (derived from IllegalStateException)
                // that contains a list of languages.
                Notification notification = NotificationUtils.METABORG_NOTIFICATIONS.createNotification(ex.getMessage(), NotificationType.ERROR);
                NotificationUtils.INSTANCE.notify(project, notification);
                implementation = null;

            }
        }
        else if (virtualFile instanceof LightVirtualFile) {
            final com.intellij.lang.Language ideaLanguage = ((LightVirtualFile)virtualFile).getLanguage();
            if (ideaLanguage instanceof MetaborgIdeaLanguage) {
                final ILanguage language = this.languageManager.getLanguage((MetaborgIdeaLanguage)ideaLanguage);
                implementation = language.activeImpl();
            }
        }

        if (implementation == null){
            // We don't know the language, so
            // let's return the plain syntax highlighter instead.
            return new PlainSyntaxHighlighter();
        }

        @Nullable final IProject metaborgProject = null;  // FIXME: Get IProject from Project
        final SpoofaxTokenTypeManager tokenTypesManager =
                this.bindingManager.getTokenTypeManager(implementation.belongsTo());
        final Lexer lexer = this.highlightingLexerFactory
                .create(file, metaborgProject, implementation, tokenTypesManager);

        return new SpoofaxSyntaxHighlighter(lexer);
    }
}
