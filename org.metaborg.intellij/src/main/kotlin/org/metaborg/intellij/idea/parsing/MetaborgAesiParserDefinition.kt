package org.metaborg.intellij.idea.parsing

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.ParserDefinition
import com.intellij.psi.tree.IFileElementType
import com.virtlink.editorservices.intellij.psi.AesiTokenTypeManager
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiParserDefinition
import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType

class MetaborgAesiParserDefinition @Inject constructor (
        @Assisted fileType: MetaborgLanguageFileType,
        @Assisted fileElementType: IFileElementType,
        @Assisted tokenTypeManager: AesiTokenTypeManager)
    : AesiParserDefinition(fileType, fileElementType, tokenTypeManager) {

    /**
     * Factory for parser definitions.
     */
    interface IFactory {

        /**
         * Creates a new parser definition for the specified file type.
         *
         * @param fileType        The file type.
         * @param fileElementType The file element type.
         * @return The created parser definition.
         */
        fun create(fileType: MetaborgLanguageFileType, fileElementType: IFileElementType, tokenTypeManager: AesiTokenTypeManager): MetaborgAesiParserDefinition

    }

}