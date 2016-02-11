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

package org.metaborg.intellij.idea.languages;

import org.metaborg.intellij.idea.extensions.*;
import org.metaborg.intellij.idea.filetypes.*;
import org.metaborg.intellij.idea.parsing.elements.*;

/**
 * IntelliJ IDEA objects bound to a language.
 *
 * The fields in this class contain language-specific extension points
 * that are registered when the language is activated, and unregistered
 * when the language is deactivated.
 */
/* package private */ final class LanguageBindings {

    private final SpoofaxTokenTypeManager tokenTypeManager;
    private final MetaborgLanguageFileType fileType;
    private final InstanceLanguageExtensionPoint<?> parserDefinitionExtension;
    private final InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension;

    /**
     * Gets the token type manager associated with this language.
     *
     * @return The {@link SpoofaxTokenTypeManager}.
     */
    public SpoofaxTokenTypeManager getTokenTypeManager() {
        return this.tokenTypeManager;
    }

    /**
     * Gets the language file type.
     *
     * @return The language file type.
     */
    public MetaborgLanguageFileType getFileType() {
        return this.fileType;
    }

    /**
     * Gets the parser definition extension point value.
     *
     * @return The extension point value.
     */
    public InstanceLanguageExtensionPoint<?> getParserDefinitionExtension() {
        return this.parserDefinitionExtension;
    }

    /**
     * Gets the syntax highlighter factory extension point value.
     *
     * @return The extension point value.
     */
    public InstanceSyntaxHighlighterFactoryExtensionPoint getSyntaxHighlighterFactoryExtension() {
        return this.syntaxHighlighterFactoryExtension;
    }

    // TODO: Add fields.
//    private final IdeaLanguageAttachment languageObject;
//    private InstanceLanguageExtensionPoint<?> parserDefinitionExtension;
//    private InstanceLanguageExtensionPoint<?> externalAnnotatorExtension;
//    private InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension;
//
//
//    public IdeaLanguageAttachment languageObject() { return this.languageObject; }
//
//    public InstanceLanguageExtensionPoint<?> parserDefinitionExtension() { return this.parserDefinitionExtension; }
//
//    public void setParserDefinitionExtension(final InstanceLanguageExtensionPoint<?> parserDefinitionExtension) { this.parserDefinitionExtension = parserDefinitionExtension; }
//
//    public InstanceLanguageExtensionPoint<?> externalAnnotatorExtension() { return this.externalAnnotatorExtension; }
//
//    public void setExternalAnnotatorExtension(final InstanceLanguageExtensionPoint<?> externalAnnotatorExtension) { this.externalAnnotatorExtension = externalAnnotatorExtension; }
//
//    public InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension() { return this.syntaxHighlighterFactoryExtension; }
//
//    public void setSyntaxHighlighterFactoryExtension(final InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension) { this.syntaxHighlighterFactoryExtension = syntaxHighlighterFactoryExtension; }

    /**
     * Initializes a new instance of the {@link LanguageBindings} class.
     *
     * @param tokenTypeManager The associated token type manager.
     * @param fileType The language file type.
     * @param parserDefinitionExtension The parser definition extension point value.
     * @param syntaxHighlighterFactoryExtension THe syntax highlighter factory extension.
     */
    public LanguageBindings(final SpoofaxTokenTypeManager tokenTypeManager,
                            final MetaborgLanguageFileType fileType,
                            final InstanceLanguageExtensionPoint<?> parserDefinitionExtension,
                            final InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension) {
        this.tokenTypeManager = tokenTypeManager;
        this.fileType = fileType;
        this.parserDefinitionExtension = parserDefinitionExtension;
        this.syntaxHighlighterFactoryExtension = syntaxHighlighterFactoryExtension;
    }

}
