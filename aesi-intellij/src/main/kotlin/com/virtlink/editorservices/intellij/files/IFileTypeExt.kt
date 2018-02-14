package com.virtlink.editorservices.intellij.files

import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx
import com.virtlink.editorservices.intellij.languages.AesiLanguage

/**
 * An extended IntelliJ file type.
 */
interface IFileTypeExt: FileType {

    companion object {
        /**
         * Registers a file type with IntelliJ.
         *
         * @param fileType The file type to register.
         */
        fun register(fileType: IFileTypeExt) {
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
        fun unregister(fileType: IFileTypeExt) {
            val fileTypeManager = FileTypeManagerEx.getInstanceEx()
            fileTypeManager.unregisterFileType(fileType)
        }
    }

    /**
     * Gets a list of file extensions supported by this file type.
     *
     * Each file extension is without the leading dot.
     */
    val extensions: Set<String>

}