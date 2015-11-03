package org.metaborg.settings;

import java.util.Collection;

/**
 * The pre-defined setting inheritance strategies.
 */
public final class SettingStrategies {

    // Prevent instantiation.
    private SettingStrategies() {}

    private static final SettingOverrideStrategy<?> _override = new SettingOverrideStrategy<>();
    private static final SettingCollectionUnionStrategy<?> _unionCollection = new SettingCollectionUnionStrategy<>();
    private static final SettingListUnionStrategy<?> _appendList = new SettingListUnionStrategy<>();

    /**
     * Override an existing value.
     *
     * @param <T> The type of value.
     * @return The strategy.
     */
    public static <T> SettingOverrideStrategy<T> override() {
        //noinspection unchecked
        return (SettingOverrideStrategy<T>)_override;
    }

    /**
     * Union a collection with an existing collection.
     *
     * @param <E> The type of elements in the collection.
     * @return The strategy.
     */
    public static <E> SettingCollectionUnionStrategy<E> unionCollection() {
        //noinspection unchecked
        return (SettingCollectionUnionStrategy<E>)_unionCollection;
    }

    /**
     * Union a list with an existing list.
     *
     * @param <E> The type of elements in the list.
     * @return The strategy.
     */
    public static <E> SettingListUnionStrategy<E> appendList() {
        //noinspection unchecked
        return (SettingListUnionStrategy<E>)_appendList;
    }
}
