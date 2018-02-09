package com.virtlink.editorservices

import java.io.Serializable

/**
 * A line:character position in a document.
 *
 * The position before the very first character of the document is always at 0:0.
 * The position right after the EOL terminator(s) of a line x is position (x+1):0 (i.e. the start of the next line).
 *
 * @property line The zero-based line number.
 * @property character The zero-based character value from the start of the line.
 */
data class Position(val line: Int, val character: Int): Comparable<Position>, Serializable {

    init {
        if (line < 0)
            throw IllegalArgumentException("The line number must be greater than or equal to zero.")
        if (character < 0)
            throw IllegalArgumentException("The character value must be greater than or equal to zero.")
    }

    override fun compareTo(other: Position): Int {
        var comparison = 0
        if (comparison == 0) comparison = this.line.compareTo(other.line)
        if (comparison == 0) comparison = this.character.compareTo(other.character)
        return comparison
    }

    override fun toString(): String
            = "$line:$character"
}