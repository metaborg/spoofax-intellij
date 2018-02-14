package org.metaborg.intellij.idea.files

import com.google.common.base.Preconditions
import com.google.common.collect.Iterables
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher
import com.intellij.openapi.fileTypes.FileNameMatcher
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.virtlink.editorservices.intellij.files.IFileTypeExt
import org.jetbrains.annotations.NonNls
import org.metaborg.intellij.idea.filetypes.IMetaborgFileType


/**
 * Consumer for file types implementing the [IMetaborgFileType] interface.
 *
 * @param consumer The file type consumer to wrap.
 */
@Deprecated("")
class SpoofaxLanguageArtifactFileTypeConsumer(private val consumer: FileTypeConsumer) {

    init {
        Preconditions.checkNotNull(consumer)
    }

    /**
     * Consumes the file type with its default extensions.
     *
     * @param fileType The file type.
     */
    fun consume(fileType: IFileTypeExt) {
        Preconditions.checkNotNull(fileType)

        consume(fileType, *Iterables.toArray(fileType.extensions, String::class.java))
    }

    /**
     * Consumes the file type with only the specified extensions.
     *
     * @param fileType   The file type.
     * @param extensions The file extensions, without a leading '.'.
     */
    fun consume(fileType: FileType, @NonNls vararg extensions: String) {
        Preconditions.checkNotNull(fileType)
//        Preconditions.checkNotNull<Array<String>>(extensions)

        val matchers = arrayOfNulls<FileNameMatcher>(extensions.size)
        for (i in extensions.indices) {
            matchers[i] = ExtensionFileNameMatcher(extensions[i])
        }
        // TODO
//        consume(fileType, *matchers)
    }

    /**
     * Consumes the file type with only the specified file name matchers.
     *
     * @param fileType The file type.
     * @param matchers The file name matchers.
     */
    fun consume(fileType: FileType, vararg matchers: FileNameMatcher) {
        Preconditions.checkNotNull(fileType)
//        Preconditions.checkNotNull<Array<FileNameMatcher>>(matchers)

        this.consumer.consume(fileType, *matchers)
    }

}