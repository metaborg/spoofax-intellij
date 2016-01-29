/*
 * Copyright © 2015-2015
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

package org.metaborg.core;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.*;

// TODO: Move this class to Metaborg Core?

/**
 * Utility functions for collections.
 */
@Singleton
public final class CollectionUtils {

    // To prevent instantiation.
    private CollectionUtils() {}

    /**
     * Returns an immutable list with the elements in the iterable.
     *
     * @param iterable The iterable.
     * @param <T>      The type of elements.
     * @return An immutable list.
     */
    @NotNull
    public static <T> List<T> toList(@NotNull final Iterable<? extends T> iterable) {
        Preconditions.checkNotNull(iterable);

        return Collections.unmodifiableList(toMutableList(iterable));
    }

    /**
     * Returns a mutable list with the elements in the iterable.
     *
     * @param iterable The iterable.
     * @param <T>      The type of elements.
     * @return A mutable list.
     */
    @NotNull
    public static <T> List<T> toMutableList(@NotNull final Iterable<? extends T> iterable) {
        Preconditions.checkNotNull(iterable);

        final ArrayList<T> list = new ArrayList<>();
        for (final T item : iterable) {
            list.add(item);
        }
        return list;
    }

    /**
     * Returns a sorted list with the elements in the collection.
     *
     * @param collection The collection.
     * @param <T>        The type of elements.
     * @return A sorted list.
     */
    @NotNull
    public static <T extends Comparable<? super T>> List<T> toSortedList(@NotNull final Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        return toSortedList(collection, null);
    }

    /**
     * Returns a sorted list with the elements in the collection.
     *
     * @param collection The collection.
     * @param comparator The comparator.
     * @param <T>        The type of elements.
     * @return A sorted list.
     */
    @NotNull
    public static <T> List<T> toSortedList(
            @NotNull final Collection<T> collection,
            @Nullable final Comparator<? super T> comparator) {
        Preconditions.checkNotNull(collection);

        final List<T> list = new ArrayList<>(collection);
        list.sort(comparator);
        return list;
    }

    /**
     * Returns an array with the elements in the iterable.
     *
     * @param iterable The iterable.
     * @param <T>      The type of elements.
     * @return An array.
     */
    @NotNull
    public static <T> T[] toArray(@NotNull final Iterable<? extends T> iterable, @NotNull final Class<T> clazz) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(clazz);

        return toArray(toMutableList(iterable), clazz);
    }

    /**
     * Returns an array with the elements in the iterable.
     *
     * @param collection The collection.
     * @param clazz      The type of the elements in the resulting array.
     * @param <T>        The type of elements.
     * @return An array.
     */
    @NotNull
    public static <T> T[] toArray(@NotNull final Collection<? extends T> collection, @NotNull final Class<T> clazz) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(clazz);

        @SuppressWarnings("unchecked") final
        T[] result = (T[])Array.newInstance(clazz, collection.size());
        int index = 0;
        for (final T item : collection) {
            result[index] = item;
            index++;
        }
        return result;
    }

}
