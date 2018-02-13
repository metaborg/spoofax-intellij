package org.metaborg.intellij.idea.parsing

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.ParserDefinition
import com.intellij.psi.tree.IFileElementType
import com.virtlink.editorservices.intellij.psi.AesiTokenTypeManager
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiParserDefinition
import org.metaborg.intellij.idea.files.SpoofaxFileType
import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType

class MetaborgAesiParserDefinition @Inject constructor (
        @Assisted fileType: SpoofaxFileType,
        @Assisted fileElementType: IFileElementType,
        @Assisted tokenTypeManager: AesiTokenTypeManager)
    : AesiParserDefinition(fileType, fileElementType, tokenTypeManager) {

    interface IFactory {
        fun create(fileType: SpoofaxFileType,
                   fileElementType: IFileElementType,
                   tokenTypeManager: AesiTokenTypeManager)
                : MetaborgAesiParserDefinition
    }

}