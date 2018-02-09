package com.virtlink.editorservices.intellij.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.psi.PsiElement

class AesiNamedElementType(language: Language, debugName: String? = null)
    : AesiElementType(language, debugName ?: "NAMED") {

    override fun createElement(node: ASTNode): PsiElement = AesiPsiNamedElement(node)

}