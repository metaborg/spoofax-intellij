package com.virtlink.editorservices.intellij.psi

import com.google.inject.Inject
import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiLexer

class AesiFileElementType @Inject constructor(
        language: Language,
        private val lexerFactory: AesiLexer.IFactory,
        private val resourceManager: IntellijResourceManager,
        private val astBuilderFactory: IAstBuilderFactory)
    : IFileElementType(language) {

    override fun doParseContents(chameleon: ASTNode, psi: PsiElement): ASTNode {
        val documentUri = this.resourceManager.getUri(psi.containingFile)
        val lexer = this.lexerFactory.create(documentUri)
        val builder = PsiBuilderFactory.getInstance().createBuilder(psi.project, chameleon, lexer, language, chameleon.chars)
        val astBuilder = this.astBuilderFactory.create()
        val tree = astBuilder.build(this, builder)
        return tree.firstChildNode
    }

}