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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * An adapting collection.
 */
public final class AdaptingCollection<E, U> extends AbstractCollection<E> {

    private final Collection<U> underlyingCollection;
    private final Function<E, U> convertTo;
    private final Function<U, E> convertFrom;

    /**
     * Initializes a new instance of the {@link AdaptingCollection} class.
     *
     * @param underlyingCollection The underlying collection.
     * @param convertTo The conversion function from this collection's element type
     *                  to the underlying collection's element type.
     * @param convertFrom The conversion function from the underlying collection's element type
     *                    to this collection's element type.
     */
    public AdaptingCollection(final Collection<U> underlyingCollection,
                              final Function<E, U> convertTo,
                              final Function<U, E> convertFrom) {
        this.underlyingCollection = underlyingCollection;
        this.convertTo = convertTo;
        this.convertFrom = convertFrom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.underlyingCollection.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(final E e) {
        return this.underlyingCollection.add(this.convertTo.apply(e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> iterator() {
        return new AdaptingIterator(this.underlyingCollection.iterator());
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
            return AdaptingCollection.this.convertFrom.apply(this.underlyingIterator.next());
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
