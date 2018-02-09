package com.virtlink.editorservices.intellij.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

open class AesiPsiElement(node: ASTNode) : ASTWrapperPsiElement(node), PsiElement {
    override fun getReferences(): Array<PsiReference>
        = ReferenceProvidersRegistry.getReferencesFromProviders(this)
}