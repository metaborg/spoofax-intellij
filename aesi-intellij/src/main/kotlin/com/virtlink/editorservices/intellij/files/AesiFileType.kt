package com.virtlink.editorservices.intellij.files

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx
import com.virtlink.editorservices.intellij.languages.AesiLanguage

/**
 * A file type of an AESI language.
 */
abstract class AesiFileType(
        language: AesiLanguage)
    : LanguageFileType(language) {

    companion object {
        /**
         * Registers a file type with IntelliJ.
         *
         * @param fileType The file type to register.
         */
        fun register(fileType: AesiFileType) {
            val fileTypeManager = FileTypeManagerEx.getInstanceEx()
            fileTypeManager.registerFileType(fileType)
            fileType.extensions
                    .map { ExtensionFileNameMatcher(it) }
                    .forEach { fileTypeManager.associate(fileType, it) }
        }

        /**
         * Unregisters a file type from IntelliJ.
         *
         * @param fileType The file type to unregister.
         */
        fun unregister(fileType: AesiFileType) {
            val fileTypeManager = FileTypeManagerEx.getInstanceEx()
            fileTypeManager.unregisterFileType(fileType)
        }
    }

    /**
     * Gets a list of file extensions supported by this file type.
     *
     * Each file extension is without the leading dot.
     */
    abstract val extensions: Set<String>

}