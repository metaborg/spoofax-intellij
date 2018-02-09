package com.virtlink.editorservices.intellij.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.FileViewProvider

class AesiFile2(viewProvider: FileViewProvider, private val fileType: LanguageFileType)
    : PsiFileBase(viewProvider, fileType.language) {

    override fun getFileType(): LanguageFileType = this.fileType
}