package com.virtlink.editorservices.intellij.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.util.IncorrectOperationException

class AesiPsiNamedElement(node: ASTNode) : AesiPsiElement(node), PsiNamedElement {

    override fun getName(): String? {
        return this.node.text
    }

    override fun setName(name: String): PsiElement {
        throw IncorrectOperationException()
    }
}