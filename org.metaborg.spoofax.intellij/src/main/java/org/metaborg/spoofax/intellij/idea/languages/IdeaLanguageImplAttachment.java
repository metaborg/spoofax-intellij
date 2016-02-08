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

import com.intellij.lexer.Lexer;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.factories.IHighlightingLexerFactory;

/**
 * Stores the IntelliJ IDEA objects that are associated with a particular {@link ILanguageImpl}.
 */
public final class IdeaLanguageImplAttachment {

//    private final Lexer lexer;
    private final IHighlightingLexerFactory lexerFactory;
    private final SpoofaxTokenTypeManager tokenTypeManager;
    private final DefaultActionGroup buildActionGroup;

//    public Lexer lexer() { return this.lexer;}

    public SpoofaxTokenTypeManager tokenTypeManager() { return this.tokenTypeManager; }

    public IHighlightingLexerFactory lexerFactory() { return this.lexerFactory; }

    public DefaultActionGroup buildActionGroup() { return this.buildActionGroup; }

    /**
     * Creates a new instance of the {@link IdeaLanguageImplAttachment} class.
     *
     * @param lexerFactory     The lexer factory.
     * @param tokenTypeManager The token type manager.
     * @param buildActionGroup The build action group.
     */
    /* package private */ IdeaLanguageImplAttachment(
            final IHighlightingLexerFactory lexerFactory,
            final SpoofaxTokenTypeManager tokenTypeManager,
            final DefaultActionGroup buildActionGroup) {
        this.lexerFactory = lexerFactory;
        this.tokenTypeManager = tokenTypeManager;
        this.buildActionGroup = buildActionGroup;
    }
}