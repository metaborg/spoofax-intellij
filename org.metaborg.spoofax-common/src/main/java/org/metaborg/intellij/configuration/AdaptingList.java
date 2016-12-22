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

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

/**
 * An adapting list.
 */
public final class AdaptingList<E, U> extends AbstractList<E> {

    private final List<U> underlyingList;
    private final Function<E, U> convertTo;
    private final Function<U, E> convertFrom;

    /**
     * Initializes a new instance of the {@link AdaptingList} class.
     *
     * @param underlyingList The underlying list.
     * @param convertTo The conversion function from this list's element type
     *                  to the underlying list's element type.
     * @param convertFrom The conversion function from the underlying list's element type
     *                    to this list's element type.
     */
    public AdaptingList(final List<U> underlyingList,
                        final Function<E, U> convertTo,
                        final Function<U, E> convertFrom) {
        this.underlyingList = underlyingList;
        this.convertTo = convertTo;
        this.convertFrom = convertFrom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.underlyingList.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E get(final int index) {
        return this.convertFrom.apply(this.underlyingList.get(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E set(final int index, final E element) {
        return this.convertFrom.apply(this.underlyingList.set(index, this.convertTo.apply(element)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final int index, final E element) {
        this.underlyingList.add(index, this.convertTo.apply(element));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E remove(final int index) {
        return this.convertFrom.apply(this.underlyingList.remove(index));
    }
}
