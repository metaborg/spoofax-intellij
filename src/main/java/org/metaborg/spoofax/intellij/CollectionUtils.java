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

package org.metaborg.spoofax.intellij;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public static <T> List<T> toList(@NotNull final Iterable<? extends T> iterable) {
        return Collections.unmodifiableList(toMutableList(iterable));
    }

    /**
     * Returns a mutable list with the elements in the iterable.
     *
     * @param iterable The iterable.
     * @param <T>      The type of elements.
     * @return A mutable list.
     */
    public static <T> List<T> toMutableList(@NotNull final Iterable<? extends T> iterable) {
        ArrayList<T> list = new ArrayList<>();
        for (T item : iterable) {
            list.add(item);
        }
        return list;
    }

    /**
     * Returns an array with the elements in the iterable.
     *
     * @param iterable The iterable.
     * @param <T>      The type of elements.
     * @return An array.
     */
    public static <T> T[] toArray(@NotNull final Iterable<? extends T> iterable, Class<T> clazz) {
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
    public static <T> T[] toArray(@NotNull final Collection<? extends T> collection, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(clazz, collection.size());
        int index = 0;
        for (T item : collection) {
            result[index] = item;
        }
        return result;
    }

}
