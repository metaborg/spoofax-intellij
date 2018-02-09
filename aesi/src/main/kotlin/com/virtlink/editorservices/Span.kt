package com.virtlink.editorservices

import java.io.Serializable

/**
 * A range in a document.
 *
 * @property startOffset The inclusive start offset of the span.
 * @property endOffset The exclusive end offset of the span.
 */
data class Span(val startOffset: Offset, val endOffset: Offset): Serializable {

    companion object {
        /**
         * Constructs a span from a start offset and length.
         *
         * @param startOffset The start offset.
         * @param length The length.
         */
        @JvmStatic fun fromLength(startOffset: Offset, length: Int): Span {
            if (startOffset < 0)
                throw IllegalArgumentException("The start offset $startOffset must be positive or zero.")
            if (length < 0)
                throw IllegalArgumentException("The length $length must be positive or zero.")
            return Span(startOffset, startOffset + length)
        }
    }

    init {
        if (startOffset < 0)
            throw IllegalArgumentException("The start offset $startOffset must be positive or zero.")
        if (endOffset < startOffset)
            throw IllegalArgumentException("The end offset $endOffset must be at or after the start offset $startOffset.")
    }

    val endInclusive: Offset
        get() = this.endOffset - 1

    /**
     * Gets the length of the span.
     */
    val length get() = this.endOffset - this.startOffset

    fun isEmpty(): Boolean
        = this.startOffset == this.endOffset

    fun iterator(): Iterator<Offset>
        = object : Iterator<Offset> {

            var currentOffset = startOffset

            override fun hasNext(): Boolean = currentOffset < endOffset

            override fun next(): Offset {
                val next = this.currentOffset
                currentOffset += 1
                return next
            }
        }

    override fun toString(): String
        = "(${this.startOffset}-${this.endOffset})"

}