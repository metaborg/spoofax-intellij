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
