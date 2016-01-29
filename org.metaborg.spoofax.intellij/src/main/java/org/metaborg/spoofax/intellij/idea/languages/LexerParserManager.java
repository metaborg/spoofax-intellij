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
import com.google.inject.Singleton;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;

/**
 * {@inheritDoc}
 */
@Singleton
public final class LexerParserManager implements ILexerParserManager {

    @NotNull
    private final IIdeaAttachmentManager attachmentManager;

    @Inject
    /* package private */ LexerParserManager(final IIdeaAttachmentManager attachmentManager) {
        this.attachmentManager = attachmentManager;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Lexer getHighlightingLexer(final ILanguageImpl implementation) {
        return this.attachmentManager.get(implementation).lexer();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Lexer createCharacterLexer(final ILanguage language) {
        final IdeaLanguageAttachment attachment = this.attachmentManager.get(language);
        return attachment.characterLexerFactory().create(attachment.tokenTypeManager());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public PsiParser createParser(final ILanguage language) {
        throw new UnsupportedOperationException("Deprecated");
    }
}
