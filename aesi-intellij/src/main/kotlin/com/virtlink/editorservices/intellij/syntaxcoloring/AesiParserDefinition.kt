package com.virtlink.editorservices.intellij.syntaxcoloring

import com.google.inject.Inject
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.virtlink.editorservices.intellij.psi.*

/**
 * Defines the implementation of a parser for a custom language.
 *
 * We have overridden the code that would use the lexer and parser
 * returned by this class in [AesiFileElementType.doParseContents].
 */
open class AesiParserDefinition @Inject constructor(
        private val fileType: LanguageFileType,
        private val fileElementType: IFileElementType,
        private val tokenTypeManager: AesiTokenTypeManager)
    : ParserDefinition {

    override fun getFileNodeType(): IFileElementType = this.fileElementType

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements
            = ParserDefinition.SpaceRequirements.MAY

    override fun getStringLiteralElements(): TokenSet = tokenTypeManager.stringLiteralTokens

    override fun getWhitespaceTokens(): TokenSet = tokenTypeManager.whitespaceTokens

    override fun getCommentTokens(): TokenSet = tokenTypeManager.commentTokens

    override fun createLexer(project: Project?): Lexer
        = throw UnsupportedOperationException("See AesiFileElementType.doParseContents().")

    override fun createParser(project: Project): PsiParser
        = throw UnsupportedOperationException("See AesiFileElementType.doParseContents().")

    override fun createFile(viewProvider: FileViewProvider): PsiFile = AesiFile2(viewProvider, this.fileType)

    override fun createElement(node: ASTNode): PsiElement {
        val elementType = node.elementType
        if (elementType !is AesiElementType)
            throw UnsupportedOperationException("Unexpected element type: $elementType")

        return elementType.createElement(node)
    }
}