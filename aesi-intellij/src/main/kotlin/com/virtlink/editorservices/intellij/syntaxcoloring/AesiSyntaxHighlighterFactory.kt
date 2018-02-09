package com.virtlink.editorservices.intellij.syntaxcoloring

import com.google.inject.Inject
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager

abstract class AesiSyntaxHighlighterFactory @Inject constructor(
        private val resourceManager: IntellijResourceManager,
        private val lexerFactory: AesiLexer.IFactory,
        private val highlighterFactory: AesiSyntaxHighlighter.IFactory)
    : SyntaxHighlighterFactory() {

    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        val documentUri = this.resourceManager.getUri(virtualFile!!, project!!)
        val lexer = this.lexerFactory.create(documentUri)
        return highlighterFactory.create(lexer)
    }
}