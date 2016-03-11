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

package org.metaborg.intellij.configuration;

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
