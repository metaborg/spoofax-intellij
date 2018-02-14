package com.virtlink.editorservices.intellij.psi

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiLexer

/**
 * The element type of an AESI file.
 *
 * @param language The language to which the element type belongs.
 */
open class AesiFileElementType @Inject constructor(
        @Assisted language: Language,
        @Assisted private val tokenTypeManager: AesiTokenTypeManager,
        @Assisted private val elementTypeManager: AesiElementTypeManager,
        private val lexerFactory: AesiLexer.IFactory,
        private val resourceManager: IntellijResourceManager,
        private val astBuilderFactory: AesiAstBuilder.IFactory)
    : IFileElementType(language) {

    /**
     * This overrides the default parsing behavior of IntelliJ.
     *
     * In this method we have access to the name of the file being parsed.
     */
    override fun doParseContents(chameleon: ASTNode, psi: PsiElement): ASTNode {
        val documentUri = this.resourceManager.getUri(psi.containingFile)
        val lexer = this.lexerFactory.create(documentUri, language, tokenTypeManager)
        val builder = PsiBuilderFactory.getInstance().createBuilder(psi.project, chameleon, lexer, language, chameleon.chars)
        val astBuilder = this.astBuilderFactory.create(elementTypeManager)
        val tree = astBuilder.build(this, builder)
        return tree.firstChildNode
    }

}