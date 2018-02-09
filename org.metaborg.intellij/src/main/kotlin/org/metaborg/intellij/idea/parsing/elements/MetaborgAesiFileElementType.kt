package org.metaborg.intellij.idea.parsing.elements


import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.virtlink.editorservices.intellij.psi.*
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiLexer
import org.metaborg.intellij.idea.languages.MetaborgIdeaLanguage

/**
 * Metaborg source file PSI element type.
 */
class MetaborgAesiFileElementType @Inject
constructor(@Assisted language: MetaborgIdeaLanguage,
            @Assisted tokenTypeManager: AesiTokenTypeManager,
            @Assisted elementTypeManager: AesiElementTypeManager,
            lexerFactory: AesiLexer.IFactory,
            resourceManager: IntellijResourceManager,
            astBuilderFactory: AesiAstBuilder.IFactory)
    : AesiFileElementType(language, tokenTypeManager, elementTypeManager, lexerFactory, resourceManager, astBuilderFactory) {

    /**
     * Factory for file element types.
     */
    interface IFactory {

        /**
         * Creates a new file element type.
         *
         * @param language            The IntelliJ language.
         * @param tokenTypeManager    The token type manager.
         * @param elementTypeManager  The element type manager.
         * @return The created parser definition.
         */
        fun create(language: MetaborgIdeaLanguage, tokenTypeManager: AesiTokenTypeManager, elementTypeManager: AesiElementTypeManager): MetaborgAesiFileElementType

    }

}
