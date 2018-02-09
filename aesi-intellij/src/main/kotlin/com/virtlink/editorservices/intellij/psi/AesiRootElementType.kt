package com.virtlink.editorservices.intellij.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.psi.PsiElement

class AesiRootElementType(language: Language, debugName: String? = null)
    : AesiElementType(language, debugName ?: "ROOT") {

    override fun createElement(node: ASTNode): PsiElement = AesiPsiRootElement(node)
}