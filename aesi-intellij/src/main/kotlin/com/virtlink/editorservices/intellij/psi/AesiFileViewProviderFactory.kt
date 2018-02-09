package com.virtlink.paplj.intellij

import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager

abstract class AesiFileViewProviderFactory : FileViewProviderFactory {
    abstract override fun createFileViewProvider(file: VirtualFile, language: Language?, manager: PsiManager, eventSystemEnabled: Boolean)
            : FileViewProvider
}