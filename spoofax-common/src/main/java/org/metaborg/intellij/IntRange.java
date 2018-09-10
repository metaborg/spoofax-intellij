/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij;

// TODO: Move this to Metaborg Core?

import org.metaborg.util.log.*;

/**
 * An integer range.
 * <p>
 * The range has an inclusive start and exclusive end (i.e. <code>start &lt;= x &lt; end</code>,
 * or alternatively <code>[start, end)</code>).
 * An empty range has a start and end that are equal.
 * <p>
 * Note that Dijkstra argues in favor of inclusive start and exclusive end.
 *
 * @see <a href="https://www.cs.utexas.edu/~EWD/ewd08xx/EWD831.PDF">Dijkstra, E. W. (1982). Why numbering
 * should start at zero.</a>.
 */
public final class IntRange {

    private final ILogger logger = LoggerUtils.logger(IntRange.class);

    /**
     * An empty range at zero.
     */
    public static final IntRange EMPTY = at(0);

    /**
     * Gets the inclusive start of the range.
     */
    public final int start;

    /**
     * Gets the exclusive end of the range.
     */
    public final int end;

    /**
     * Initializes a new instance of the {@link IntRange} class.
     * <p>
     * The start must be at or before the end.
     *
     * @param start The inclusive start of the range.
     * @param end   The exclusive end of the range.
     */
    private IntRange(final int start, final int end) {
        if (end < start)
            throw new IllegalArgumentException("The start must be equal to or less than the end.");

        this.start = start;
        this.end = end;
    }

    /**
     * Creates an empty range at the specified position.
     *
     * @param position The position.
     * @return The created empty range.
     */
    public static IntRange at(final int position) {
        return new IntRange(position, position);
    }

    /**
     * Gets whether this range is empty.
     *
     * @return <code>true</code> when the range is empty;
     * otherwise, <code>false</code>.
     */
    public boolean isEmpty() {
        return length() == 0;
    }

    /**
     * Gets the length of the range.
     *
     * @return The number of elements included in the range.
     */
    public int length() {
        return this.end - this.start;
    }

    /**
     * Determines whether the range contains the specified element.
     *
     * @param element The element to look for.
     * @return <code>true</code> when the element is in the range;
     * otherwise, <code>false</code>.
     */
    public boolean contains(final int element) {
        return containsRange(IntRange.is(element));
    }

    /**
     * Determines whether this range contains the specified range.
     * <p>
     * This range contains a specified empty range when the empty range is strictly
     * between this range's bounds.
     *
     * @param other The other range.
     * @return <code>true</code> when the other range is in this range;
     * otherwise, <code>false</code>.
     */
    public boolean containsRange(final IntRange other) {
        return overlapsRange(other)
                && this.start <= other.start
                && other.end <= this.end;
    }

    /**
     * Creates a range with a single element.
     *
     * @param element The element.
     * @return The created range.
     */
    public static IntRange is(final int element) {
        if (element == Integer.MAX_VALUE)
            throw new IllegalArgumentException("The value is out of bounds.");
        return new IntRange(element, element + 1);
    }

    /**
     * Determines whether this range overlaps the specified range.
     * <p>
     * This range overlaps a specified empty range when the empty range is strictly
     * between this range's bounds.
     *
     * @param other The other range.
     * @return <code>true</code> when the other range overlaps this range;
     * otherwise, <code>false</code>.
     */
    public boolean overlapsRange(final IntRange other) {
        return !isBeforeRange(other) && !isAfterRange(other);
    }

    /**
     * Determines whether this range ends before the specified range starts.
     *
     * @param other The other range.
     * @return <code>true</code> when this range ends before the specified range starts;
     * otherwise, <code>false</code>.
     */
    public boolean isBeforeRange(final IntRange other) {
        return other.start >= this.end;
    }

    /**
     * Determines whether this range starts after the specified range ends.
     *
     * @param other The other range.
     * @return <code>true</code> when this range starts after the specified range ends;
     * otherwise, <code>false</code>.
     */
    public boolean isAfterRange(final IntRange other) {
        return this.start >= other.end;
    }

    /**
     * Determines whether this range starts after the specified element.
     *
     * @param element The element.
     * @return <code>true</code> when this range starts after the specified element;
     * otherwise, <code>false</code>.
     */
    public boolean isAfter(final int element) {
        return isAfterRange(IntRange.is(element));
    }

    /**
     * Determines whether this range ends before the specified element.
     *
     * @param element The element.
     * @return <code>true</code> when this range ends before the specified element;
     * otherwise, <code>false</code>.
     */
    public boolean isBefore(final int element) {
        return isBeforeRange(IntRange.is(element));
    }

    /**
     * Determines whether this range starts directly after the specified element.
     *
     * @param element The element.
     * @return <code>true</code> when this range starts directly after after the specified element;
     * otherwise, <code>false</code>.
     */
    public boolean isStartedBy(final int element) {
        return isStartedByRange(IntRange.is(element));
    }

    /**
     * Determines whether this range starts directly after the specified range ends.
     *
     * @param other The other range.
     * @return <code>true</code> when this range starts directly after the specified range ends;
     * otherwise, <code>false</code>.
     */
    public boolean isStartedByRange(final IntRange other) {
        return this.start == other.end;
    }

    /**
     * Determines whether this range ends before the specified element.
     *
     * @param element The element.
     * @return <code>true</code> when this range ends before the specified element;
     * otherwise, <code>false</code>.
     */
    public boolean isEndedBy(final int element) {
        return isEndedByRange(IntRange.is(element));
    }

    /**
     * Determines whether this range ends directly before the specified range starts.
     *
     * @param other The other range.
     * @return <code>true</code> when this range ends directly before the specified range starts;
     * otherwise, <code>false</code>.
     */
    public boolean isEndedByRange(final IntRange other) {
        return this.end == other.start;
    }

    /**
     * Determines the intersection between this range and the specified range.
     *
     * @param other The other range.
     * @return The intersection of this range and the other range;
     * or an empty range (with no specific start and end) when the ranges don't overlap or touch.
     */
    public IntRange intersectionWith(final IntRange other) {
        if (!isTouchedByRange(other))
            throw new IllegalArgumentException("The ranges must touch.");

        final int start = Math.max(this.start, other.start);
        final int end = Math.min(this.end, other.end);

        return between(start, end);
    }

    /**
     * Determines whether this range is started by, ended by, or overlapping the specified range.
     *
     * @param other The other range.
     * @return <code>true</code> when this range touches the specified range;
     * otherwise, <code>false</code>.
     */
    public boolean isTouchedByRange(final IntRange other) {
        return isStartedByRange(other) || overlapsRange(other) || isEndedByRange(other);
    }

    /**
     * Creates a range.
     *
     * @param startInclusive The inclusive start.
     * @param endExclusive   The exclusive end.
     * @return The created range.
     */
    public static IntRange between(final int startInclusive, final int endExclusive) {
        return new IntRange(startInclusive, endExclusive);
    }

    /**
     * Determines the union between this range and the specified range.
     *
     * @param other The other range.
     * @return The union of this range and the other range.
     */
    public IntRange unionWith(final IntRange other) {
        if (!isTouchedByRange(other))
            throw new IllegalArgumentException("The ranges must touch.");

        final int start = Math.min(this.start, other.start);
        final int end = Math.max(this.end, other.end);

        return between(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 13;
        hash = (hash * 7) + this.start;
        hash = (hash * 7) + this.end;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof IntRange))
            return false;
        return equals((IntRange)obj);
    }

    /**
     * Determines whether this range is equal to the specified range.
     *
     * @param other The other range.
     * @return <code>true</code> when the two ranges are equal;
     * otherwise, <code>false</code>.
     */
    public boolean equals(final IntRange other) {
        return this.start == other.start
                && this.end == other.end;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.logger.format("{}..{}", this.start, this.end);
    }
}
