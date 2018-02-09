package com.virtlink.editorservices.resources

import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.Position
import java.io.LineNumberReader

/**
 * Represents the content of a document.
 *
 * This object is immutable.
 */
interface IContent {

    /**
     * Gets the size of the document, in characters.
     */
    val length: Long

    /**
     * The last-modification stamp.
     */
    val lastModificationStamp: Long

    /**
     * Gets the number of lines in the document.
     *
     * This is greater than or equal to 1. If the
     * document ends with a newline, the following
     * (empty) line is treated as a line as well.
     */
    val lineCount: Int

    /**
     * Gets the text in the document.
     */
    val text: String

    /**
     * Returns a new document content object with the specified changes applied.
     *
     * @param changes The changes to apply, in terms of this content.
     * @param newStamp The new modification stamp.
     * @return The resulting content.
     */
    fun withChanges(changes: List<TextChange>, newStamp: Long): IContent

}