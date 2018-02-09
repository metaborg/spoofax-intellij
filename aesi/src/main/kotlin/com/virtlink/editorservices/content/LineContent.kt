package com.virtlink.editorservices.content

import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.Position
import com.virtlink.editorservices.indexAfterNextNewline
import com.virtlink.editorservices.resources.IAesiContent
import com.virtlink.editorservices.resources.IContent
import com.virtlink.editorservices.resources.TextChange
import java.io.LineNumberReader

/**
 * Line-based document content.
 */
open class LineContent constructor(
        override val lastModificationStamp: Long,
        private val lines: List<Line>)
    : IAesiContent {

    /**
     * A line in the document.
     *
     * @property offset The value of the line.
     * @property text The text of the line, including the end-of-line characters.
     */
    data class Line(val offset: Offset, val text: String) {
        val length get() = this.text.length

        override fun toString(): String
                = this.text
    }

    override val length: Long = getLength(this.lines)

    override val text
            get() = this.lines.fold(StringBuilder(), { b, l -> b.appendln(l) }).toString()

    /**
     * Initializes a new instance of the [LineContent] class.
     *
     * @param text The full text of the document.
     * @param stamp The modification stamp.
     */
    constructor(text: String, stamp: Long) : this(stamp, getLines(text))

    companion object {

        /**
         * Empty content.
         */
        val empty = LineContent("", 0)

        /**
         * Gets the length from the specified lines.
         *
         * @param lines The lines.
         * @return The length, in characters.
         */
        private fun getLength(lines: List<Line>): Long
                = lines.sumBy { it.length }.toLong()

        /**
         * Gets the lines resulting from the specified text.
         *
         * @param text The text.
         * @return The list of lines.
         */
        private fun getLines(text: String): List<Line> {
            val lines = mutableListOf(Line(0L, ""))
            applyChange(lines, Position(0, 0), Position(0, 0), text)
            return lines
        }

        /**
         * Updates the document to include the specified change.
         *
         * @param lines The mutable list of lines to which to apply the change.
         * @param start The start position of the changed text.
         * @param end The end position of the changed text.
         * @param newText The next text of the specified span, which may be an empty string when text was only removed.
         */
        private fun applyChange(lines: MutableList<Line>, start: Position, end: Position, newText: String) {
            val prefix = if (lines.size > 0) lines[start.line].text.substring(0, start.character) else ""
            val suffix = if (lines.size > 0) lines[end.line].text.substring(end.character) else ""

            // Construct the new lines
            val newLines = mutableListOf<Line>()
            var currentLineOffset = 0L
            var nextLineOffset = newText.indexAfterNextNewline(currentLineOffset)
            if (nextLineOffset != null) {
                // Construct the first line
                val newFirstLineText = prefix + newText.substring(currentLineOffset.toInt(), nextLineOffset.toInt())
                newLines.add(Line(currentLineOffset, newFirstLineText))
                currentLineOffset = nextLineOffset

                // Construct the intermediate lines
                nextLineOffset = newText.indexAfterNextNewline(currentLineOffset)
                while (nextLineOffset != null) {
                    val newLineText = newText.substring(currentLineOffset.toInt(), nextLineOffset.toInt())
                    newLines.add(Line(currentLineOffset, newLineText))
                    currentLineOffset = nextLineOffset

                    nextLineOffset = newText.indexAfterNextNewline(currentLineOffset)
                }

                // Construct the last line
                val newLastLineText = newText.substring(currentLineOffset.toInt()) + suffix
                newLines.add(Line(currentLineOffset, newLastLineText))
            } else {
                // Construct the only line
                newLines.add(Line(currentLineOffset, prefix + newText + suffix))
            }

            // Replace the changed lines
            val sublist = lines.subList(start.line, end.line + 1)
            sublist.clear()
            sublist.addAll(newLines)
        }
    }

    override val lineCount: Int
        get() = this.lines.size

    override fun getOffset(position: Position): Offset? {
        if (position.line >= this.lines.size)
            return null

        val currentLine = position.line
        val currentOffset = this.lines[position.line].offset
        val currentLength = this.lines[currentLine].length

        // If the character is outside the line
        // or at the end of the line while it's not the last line,
        // then the character is out of bounds.
        if (position.character > currentLength ||
                (currentLine < this.lines.size - 1 &&
                        position.character == currentLength)) {
            return null
        }

        return currentOffset + position.character
    }

    override fun getPosition(offset: Offset): Position? {
        var currentOffset = 0L
        var currentLine = 0
        while ((currentLine < this.lines.size - 1 && currentOffset + this.lines[currentLine].length <= offset)
                || (currentLine == this.lines.size && currentOffset + this.lines[currentLine].length < offset)) {
            currentOffset += this.lines[currentLine].length
            currentLine += 1
        }

        if (currentOffset > offset) return null

        return Position(currentLine, (offset - currentOffset).toInt())
    }

    override fun withChanges(changes: List<TextChange>, newStamp: Long): IContent {
        val lines = this.lines.toMutableList()
        for (change in changes.asReversed()) {
            val start = this.getPosition(change.span.startOffset)!!
            val end = this.getPosition(change.span.endOffset)!!
            applyChange(lines, start, end, change.newText)
        }
        return LineContent(newStamp, lines)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false
        other as LineContent
        return this.lines == other.lines
    }

    override fun hashCode(): Int {
        var hash = 17
        hash = hash * 23 + this.lines.hashCode()
        return hash
    }

    override fun toString(): String {
        // TODO: Use reader to get whole text.
        val text = StringBuilder()
        this.lines.forEach {
            text.append(it.text)
        }
        return text.toString()
    }

}