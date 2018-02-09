package com.virtlink.editorservices.resources

import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.Position
import com.virtlink.editorservices.Span

/**
 * List of lines in a document.
 */
interface ILineList {

    /**
     * Gets the number of lines in the document.
     *
     * This is always at least 1.
     */
    val size: Int

    /**
     * Gets the span of the specified line.
     * The line includes the EOL terminator.
     *
     * @param line The zero-based line index.
     * @return The zero-based line start offset.
     *
     * @throws IndexOutOfBoundsException The line number is out of bounds.
     */
    fun getLine(line: Int): Span

    /**
     * Gets the span of the content of the specified line.
     * The content does _not_ include the EOL terminator.
     *
     * @param line The zero-based line index.
     * @return The zero-based line start offset.
     *
     * @throws IndexOutOfBoundsException The line number is out of bounds.
     */
    fun getLineContent(line: Int): Span

    /**
     * Gets the span of the line's EOL terminator.
     *
     * @param line The zero-based line index.
     * @return The span of the line's EOL terminator,
     * or an empty span when the line is the last line in the document.
     *
     * @throws IndexOutOfBoundsException The line number is out of bounds.
     */
    fun getEndOfLine(line: Int): Span

    /**
     * Gets the line number that contains the specified offset.
     *
     * @param offset The offset to look for.
     * @return The zero-based line number of the line that contains the offset.
     *
     * @throws IndexOutOfBoundsException The offset is out of bounds.
     */
    fun getLineWithOffset(offset: Offset): Int

    /**
     * Gets the position of the specified offset in the text.
     *
     * @param offset The offset to look for.
     * @return The line:character position of the offset.
     *
     * @throws IndexOutOfBoundsException The offset is out of bounds.
     */
    fun getPosition(offset: Offset): Position

    /**
     * Gets the offset of the specified position in the text.
     *
     * @param position The position to look for.
     * @return The offset of the position.
     *
     * @throws IndexOutOfBoundsException The line number is out of bounds.
     * @throws IndexOutOfBoundsException The character offset is out of bounds.
     */
    fun getOffset(position: Position): Offset
}