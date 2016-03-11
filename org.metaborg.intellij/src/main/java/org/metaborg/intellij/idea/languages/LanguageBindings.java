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
    private final InstanceLanguageExtensionPoint<?> externalAnnotatorExtension;

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

    /**
     * Gets the external annotator extension point value.
     *
     * @return The extension point value.
     */
    public InstanceLanguageExtensionPoint<?> getExternalAnnotatorExtension() {
        return this.externalAnnotatorExtension;
    }

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
                            final InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension,
                            final InstanceLanguageExtensionPoint<?> externalAnnotatorExtension) {
        this.tokenTypeManager = tokenTypeManager;
        this.fileType = fileType;
        this.parserDefinitionExtension = parserDefinitionExtension;
        this.syntaxHighlighterFactoryExtension = syntaxHighlighterFactoryExtension;
        this.externalAnnotatorExtension = externalAnnotatorExtension;
    }

}
