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

package org.metaborg.intellij.utils;

import com.google.inject.Singleton;

import jakarta.annotation.Nullable;
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
    public static <T> List<T> toList(final Iterable<? extends T> iterable) {
        if (iterable == null) {
          throw new NullPointerException();
        }

        return Collections.unmodifiableList(toMutableList(iterable));
    }

    /**
     * Returns a mutable list with the elements in the iterable.
     *
     * @param iterable The iterable.
     * @param <T>      The type of elements.
     * @return A mutable list.
     */
    public static <T> List<T> toMutableList(final Iterable<? extends T> iterable) {
        if (iterable == null) {
          throw new NullPointerException();
        }

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
    public static <T extends Comparable<? super T>> List<T> toSortedList(final Collection<T> collection) {
        if (collection == null) {
          throw new NullPointerException();
        }

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
    public static <T> List<T> toSortedList(
            final Collection<T> collection,
            @Nullable final Comparator<? super T> comparator) {
        if (collection == null) {
          throw new NullPointerException();
        }

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
    public static <T> T[] toArray(final Iterable<? extends T> iterable, final Class<T> clazz) {
        if (iterable == null) {
          throw new NullPointerException();
        }
        if (clazz == null) {
          throw new NullPointerException();
        }

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
    public static <T> T[] toArray(final Collection<? extends T> collection, final Class<T> clazz) {
        if (collection == null) {
          throw new NullPointerException();
        }
        if (clazz == null) {
          throw new NullPointerException();
        }

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
