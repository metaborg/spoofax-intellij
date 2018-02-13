package com.virtlink.editorservices.intellij.syntaxcoloring

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.virtlink.editorservices.intellij.psi.AesiTokenTypeManager
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager
import java.net.URI

abstract class AesiSyntaxHighlighterFactory @Inject constructor(
        @Assisted private val language: Language,
        @Assisted private val tokenTypeManager: AesiTokenTypeManager,
        private val resourceManager: IntellijResourceManager,
        private val lexerFactory: AesiLexer.IFactory,
        private val highlighterFactory: AesiSyntaxHighlighter.IFactory)
    : SyntaxHighlighterFactory() {

    interface IFactory {
        fun create(language: Language, tokenTypeManager: AesiTokenTypeManager): AesiSyntaxHighlighterFactory
    }

    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        val documentUri = this.resourceManager.getUri(virtualFile!!, project!!)
        val lexer = this.lexerFactory.create(documentUri, language, tokenTypeManager)
        return highlighterFactory.create(lexer)
    }
}