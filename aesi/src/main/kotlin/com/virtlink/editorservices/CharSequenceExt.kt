package com.virtlink.editorservices

/**
 * Finds the value after the next newline in a string.
 * This may be the end of the string if the string ends with a newline.
 *
 * @param startOffset The zero-based value at which to start searching.
 * @return The zero-based value of the start of the next line,
 * which may be at the end of the string; or null when no next line was found.
 */
fun CharSequence.indexAfterNextNewline(startOffset: Int): Int? {
    if (startOffset >= this.length) {
        // No more characters in the text.
        return null
    }

    val nextOffset = this.indexOfAny(charArrayOf('\r', '\n'), startOffset)
    return if (nextOffset == -1) {
        // Not found.
        null
    } else if (nextOffset == this.length - 1) {
        // Last character of the file
        nextOffset + 1
    } else if (this[nextOffset] == '\r' && this[nextOffset + 1] == '\n') {
        // CRLF (Windows newline)
        nextOffset + 2
    } else {
        // LF (Unix newline) or CR (old Mac newline)
        nextOffset + 1
    }
}

/**
 * Finds the value after the next newline in a string.
 * This may be the end of the string if the string ends with a newline.
 *
 * @param startOffset The zero-based value at which to start searching.
 * @return The zero-based value of the start of the next line,
 * which may be at the end of the string; or null when no next line was found.
 */
fun CharSequence.indexAfterNextNewline(startOffset: Long): Long? {
    return indexAfterNextNewline(startOffset.toInt())?.toLong()
}