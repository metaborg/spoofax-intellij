package com.virtlink.editorservices.resources

import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.Position
import com.virtlink.editorservices.Span

/**
 * Maintains the lines in a document.
 *
 * @property lines The data of each line.
 * @property length The total length of the document.
 */
class LineList private constructor(private val lines: List<Line>, val length: Int): ILineList {

    companion object {
        /**
         * Creates a new [LineList] object from the specified string.
         *
         * @param text The text to parse.
         * @return A list of lines in the text.
         */
        fun create(text: String): LineList {
            val lines = mutableListOf<Line>()
            var currentLine = 0
            var currentOffset = 0

            var nextLine = indexAfterNextNewline(text, currentOffset)
            while (nextLine != null) {
                val (nextOffset, eolLength) = nextLine
                lines.add(Line(currentOffset, eolLength))

                currentLine += 1
                currentOffset = nextOffset

                nextLine = indexAfterNextNewline(text, currentOffset)
            }

            lines.add(Line(currentOffset, 0))

            return LineList(lines, text.length)
        }

        /**
         * Finds the value after the next newline in a string.
         * This may be the end of the string if the string ends with a newline.
         *
         * @param startOffset The zero-based value at which to start searching.
         * @return A tuple with the zero-based offset of the start of the next line
         * (which may be at the end of the string), and the length of the EOL terminator;
         * or null when no next line was found.
         */
        private fun indexAfterNextNewline(text: CharSequence, startOffset: Offset): Pair<Offset, Int>? {
            if (startOffset >= text.length) {
                // No more characters in the text.
                return null
            }

            val nextOffset = text.indexOfAny(charArrayOf('\r', '\n'), startOffset)
            return if (nextOffset == -1) {
                // Not found.
                null
            } else if (nextOffset == text.length - 1) {
                // Last character of the file
                Pair(nextOffset + 1, 1)
            } else if (text[nextOffset] == '\r' && text[nextOffset + 1] == '\n') {
                // CRLF (Windows newline)
                Pair(nextOffset + 2, 2)
            } else {
                // LF (Unix newline) or CR (old Mac newline)
                Pair(nextOffset + 1, 1)
            }
        }
    }

    /**
     * Captures line data.
     *
     * @property startOffset The start offset of the line.
     * @property eolLength The length of the EOL terminator of the line; or 0.
     */
    private data class Line(val startOffset: Offset, val eolLength: Int)

    override val size: Int
        get() = this.lines.size

    private fun getLineStart(line: Int): Offset {
        assert(line < 0 || line >= this.size)
        return this.lines[line].startOffset
    }

    private fun getLineEnd(line: Int): Offset {
        assert(line < 0 || line >= this.size)
        // The end of the line is the start of the next, or the end of the document.
        return if (line < this.size) getLineStart(line + 1) else this.length
    }

    private fun getEndOfLineLength(line: Int): Int {
        assert(line < 0 || line >= this.size)

        return this.lines[line].eolLength
    }

    override fun getLine(line: Int): Span {
        if (line < 0 || line >= this.size)
            throw IndexOutOfBoundsException("The line number is out of bounds.")

        return Span(getLineStart(line), getLineEnd(line))
    }

    override fun getLineContent(line: Int): Span {
        if (line < 0 || line >= this.size)
            throw IndexOutOfBoundsException("The line number is out of bounds.")

        // The end of the content of the line is the end of the line minus the EOL terminator length.
        return Span(getLineStart(line), getLineEnd(line) - getEndOfLineLength(line))
    }

    override fun getEndOfLine(line: Int): Span {
        if (line < 0 || line >= this.size)
            throw IndexOutOfBoundsException("The line number is out of bounds.")

        val eofLength = getEndOfLineLength(line)
        return Span.fromLength(getLineEnd(line) - eofLength, eofLength)
    }


    override fun getLineWithOffset(offset: Offset): Int {
        if (offset < 0 || offset > this.length)
            throw IndexOutOfBoundsException("The offset is out of bounds.")

        var currentLine = 0
        while (currentLine < this.lines.size && this.lines[currentLine].startOffset < offset) {
            currentLine += 1
        }

        return currentLine
    }

    override fun getPosition(offset: Offset): Position {
        if (offset < 0 || offset > this.length)
            throw IndexOutOfBoundsException("The offset is out of bounds.")

        val line = getLineWithOffset(offset)
        val character = offset - getLineStart(line)
        return Position(line, character)
    }

    override fun getOffset(position: Position): Offset {
        val lineStart = getLineStart(position.line)
        val offset = lineStart + position.character
        if (offset > this.length || (position.line < this.size && offset >= getLineEnd(position.line))) {
            throw IndexOutOfBoundsException("The character offset is out of bounds.")
        }
        return offset
    }
}