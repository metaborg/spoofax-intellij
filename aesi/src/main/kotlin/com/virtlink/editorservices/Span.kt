package com.virtlink.editorservices

import java.io.Serializable

/**
 * A span in a document.
 *
 * @property startOffset The inclusive start offset of the span.
 * @property endOffset The exclusive end offset of the span.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
data class Span(val startOffset: Offset, val endOffset: Offset): Serializable {

    companion object {
        /**
         * Constructs a span from a start offset and length.
         *
         * @param startOffset The start offset.
         * @param length The length.
         * @return The constructed span.
         */
        @JvmStatic fun fromLength(startOffset: Offset, length: Int): Span {
            if (startOffset < 0)
                throw IllegalArgumentException("The start offset $startOffset must be positive or zero.")
            if (length < 0)
                throw IllegalArgumentException("The length $length must be positive or zero.")
            return Span(startOffset, startOffset + length)
        }

        /**
         * Creates an empty span at the specified location.
         *
         * @param location The location.
         * @return The created empty span.
         */
        @JvmStatic fun at(location: Offset): Span {
            return Span(location, location)
        }


        /**
         * Creates a span of a single element.
         *
         * @param location The location of the element.
         * @return The created span.
         */
        @JvmStatic fun of(location: Offset): Span {
            if (location == Offset.MAX_VALUE)
                throw IllegalArgumentException("The location is out of bounds.")
            return Span(location, location + 1)
        }


        /**
         * Gets an empty span.
         */
        @JvmStatic val empty: Span = Span.at(0)
    }

    init {
        if (startOffset < 0)
            throw IllegalArgumentException("The start offset $startOffset must be positive or zero.")
        if (endOffset < startOffset)
            throw IllegalArgumentException("The end offset $endOffset must be at or after the start offset $startOffset.")
    }

    /**
     * Gets the inclusive end of the span.
     */
    val endInclusive: Offset
        get() = this.endOffset - 1

    /**
     * Gets the length of the span.
     */
    val length: Int
        get() = this.endOffset - this.startOffset

    /**
     * Gets whether the span is empty.
     */
    val isEmpty: Boolean
        get() = this.startOffset == this.endOffset

    /**
     * Determines whether the span contains the specified location.
     *
     * @param location The location to look for.
     * @return `true` when the location is in the span;
     * otherwise, `false`.
     */
    operator fun contains(location: Offset): Boolean
            = contains(Span.of(location))

    /**
     * Determines whether this span contains the specified span.
     *
     * This span contains a specified empty span when the empty span is strictly
     * between this span's bounds.
     *
     * @param other The other span.
     * @return `true` when the other span is in this span;
     * otherwise, `false`.
     */
    operator fun contains(other: Span): Boolean {
        return (overlaps(other)
                && this.startOffset <= other.startOffset
                && other.endOffset <= this.endOffset)
    }

    /**
     * Determines whether this span overlaps the specified span.
     *
     *
     * This span overlaps a specified empty span when the empty span is strictly
     * between this span's bounds.
     *
     * @param other The other span.
     * @return `true` when the other span overlaps this span;
     * otherwise, `false`.
     */
    fun overlaps(other: Span): Boolean
            = !isBefore(other) && !isAfter(other)

    /**
     * Determines whether this span ends before the specified span starts.
     *
     * @param other The other span.
     * @return `true` when this span ends before the specified span starts;
     * otherwise, `false`.
     */
    fun isBefore(other: Span): Boolean
            = other.startOffset >= this.endOffset

    /**
     * Determines whether this span starts after the specified span ends.
     *
     * @param other The other span.
     * @return `true` when this span starts after the specified span ends;
     * otherwise, `false`.
     */
    fun isAfter(other: Span): Boolean
            = this.startOffset >= other.endOffset

    /**
     * Determines whether this span starts after the specified element.
     *
     * @param element The element.
     * @return `true` when this span starts after the specified element;
     * otherwise, `false`.
     */
    fun isAfter(element: Int): Boolean
            = isAfter(Span.of(element))

    /**
     * Determines whether this span ends before the specified element.
     *
     * @param element The element.
     * @return `true` when this span ends before the specified element;
     * otherwise, `false`.
     */
    fun isBefore(element: Int): Boolean
            = isBefore(Span.of(element))

    /**
     * Determines whether this span starts directly after the specified element.
     *
     * @param element The element.
     * @return `true` when this span starts directly after after the specified element;
     * otherwise, `false`.
     */
    fun isStartedBy(element: Int): Boolean
            = isStartedBy(Span.of(element))

    /**
     * Determines whether this span starts directly after the specified span ends.
     *
     * @param other The other span.
     * @return `true` when this span starts directly after the specified span ends;
     * otherwise, `false`.
     */
    fun isStartedBy(other: Span): Boolean
            = this.startOffset == other.endOffset

    /**
     * Determines whether this span ends before the specified element.
     *
     * @param element The element.
     * @return `true` when this span ends before the specified element;
     * otherwise, `false`.
     */
    fun isEndedBy(element: Int): Boolean
            = isEndedBy(Span.of(element))

    /**
     * Determines whether this span ends directly before the specified span starts.
     *
     * @param other The other span.
     * @return `true` when this span ends directly before the specified span starts;
     * otherwise, `false`.
     */
    fun isEndedBy(other: Span): Boolean
            = this.endOffset == other.startOffset

    /**
     * Determines the intersection between this span and the specified span.
     *
     * @param other The other span.
     * @return The intersection of this span and the other span;
     * or an empty span (with no specific start and end) when the ranges don't overlap or touch.
     */
    fun intersectionWith(other: Span): Span {
        if (!isTouchedBy(other))
            throw IllegalArgumentException("The ranges must touch.")

        val start = Math.max(this.startOffset, other.startOffset)
        val end = Math.min(this.endOffset, other.endOffset)

        return Span(start, end)
    }

    /**
     * Determines whether this span is started by, ended by, or overlapping the specified span.
     *
     * @param other The other span.
     * @return `true` when this span touches the specified span;
     * otherwise, `false`.
     */
    fun isTouchedBy(other: Span): Boolean
            = isStartedBy(other) || overlaps(other) || isEndedBy(other)


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
        = "(${this.startOffset}-${this.endOffset}]"

}