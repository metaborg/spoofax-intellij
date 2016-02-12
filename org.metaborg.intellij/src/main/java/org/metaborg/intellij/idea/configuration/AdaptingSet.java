/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.intellij.idea.configuration;

import java.util.*;
import java.util.function.*;

/**
 * An adapting set.
 */
public final class AdaptingSet<E, U> extends AbstractSet<E> {

    private final Set<U> underlyingSet;
    private final Function<E, U> convertTo;
    private final Function<U, E> convertFrom;

    /**
     * Initializes a new instance of the {@link AdaptingSet} class.
     *
     * @param underlyingSet The underlying set.
     * @param convertTo The conversion function from this set's element type
     *                  to the underlying set's element type.
     * @param convertFrom The conversion function from the underlying set's element type
     *                    to this set's element type.
     */
    public AdaptingSet(final Set<U> underlyingSet,
                       final Function<E, U> convertTo,
                       final Function<U, E> convertFrom) {
        this.underlyingSet = underlyingSet;
        this.convertTo = convertTo;
        this.convertFrom = convertFrom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.underlyingSet.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(final E e) {
        return this.underlyingSet.add(this.convertTo.apply(e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> iterator() {
        return new AdaptingIterator(this.underlyingSet.iterator());
    }

    /**
     * Adapting iterator.
     */
    private class AdaptingIterator implements Iterator<E> {

        private final Iterator<U> underlyingIterator;

        /**
         * Initializes a new instance of the {@link AdaptingIterator} class.
         *
         * @param underlyingIterator The underlying iterator.
         */
        public AdaptingIterator(final Iterator<U> underlyingIterator) {
            this.underlyingIterator = underlyingIterator;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return this.underlyingIterator.hasNext();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public E next() {
            return AdaptingSet.this.convertFrom.apply(this.underlyingIterator.next());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove() {
            this.underlyingIterator.remove();
        }
    }
}
