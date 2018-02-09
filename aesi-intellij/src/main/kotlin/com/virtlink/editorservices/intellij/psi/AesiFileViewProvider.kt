package com.virtlink.paplj.intellij

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.SingleRootFileViewProvider

abstract class AesiFileViewProvider(manager: PsiManager, virtualFile: VirtualFile, eventSystemEnabled: Boolean, language: Language)
    : SingleRootFileViewProvider(manager, virtualFile, eventSystemEnabled, language) {

    override fun supportsIncrementalReparse(rootLanguage: Language): Boolean {
        return false
    }

    abstract override fun createFile(project: Project, file: VirtualFile, fileType: FileType): PsiFile?
}