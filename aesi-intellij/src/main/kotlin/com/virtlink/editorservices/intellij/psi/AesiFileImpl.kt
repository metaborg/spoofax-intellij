package com.virtlink.paplj.intellij

import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.tree.IElementType

abstract class AesiFileImpl(elementType: IElementType, provider: FileViewProvider)
    : PsiFileImpl(elementType, elementType, provider), AesiFile {

    abstract override fun getFileType(): FileType

    override fun accept(visitor: PsiElementVisitor) {
        visitor.visitFile(this)
    }

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this)
    }
}
//
//abstract class AesiFileImpl(elementType: IElementType, provider: FileViewProvider)
//    : PsiFileImpl(elementType, elementType, provider), AesiFile {
//
//    abstract override fun getFileType(): FileType
//
//    override fun accept(visitor: PsiElementVisitor) {
//        visitor.visitFile(this)
//    }
//
//    override fun getReferences(): Array<PsiReference> {
//        return ReferenceProvidersRegistry.getReferencesFromProviders(this)
//    }
//}