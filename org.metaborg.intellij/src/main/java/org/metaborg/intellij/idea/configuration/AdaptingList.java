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
