package org.metaborg.intellij.idea.syntaxcoloring

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.psi.tree.IFileElementType
import com.virtlink.editorservices.intellij.psi.AesiTokenTypeManager
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiLexer
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiSyntaxHighlighter
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiSyntaxHighlighterFactory
import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType
import org.metaborg.intellij.idea.parsing.MetaborgAesiParserDefinition

class MetaborgAesiSyntaxHighlighterFactory @Inject constructor(
        @Assisted tokenTypeManager: AesiTokenTypeManager,
        resourceManager: IntellijResourceManager,
        lexerFactory: AesiLexer.IFactory,
        highlighterFactory: AesiSyntaxHighlighter.IFactory)
    : AesiSyntaxHighlighterFactory(tokenTypeManager, resourceManager, lexerFactory, highlighterFactory) {

    interface IFactory {
        fun create(tokenTypeManager: AesiTokenTypeManager): MetaborgAesiSyntaxHighlighterFactory
    }
}