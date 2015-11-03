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

package org.metaborg.settings;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Setting in a settings hierarchy.
 *
 * You can derive from this class to provide getters.
 */
public class Settings {

    /* package private */ final Map<ISettingKey<?>, Object> settings;
    @Nullable
    private final Settings parent;

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param settings The settings map to use.
     * @param parent The parent settings; or <code>null</code>.
     * @param internal Unused.
     */
    /* package private */ Settings(final Map<ISettingKey<?>, Object> settings, @Nullable final Settings parent, final boolean internal) {
        Preconditions.checkNotNull(settings);

        this.settings = settings;
        this.parent = parent;
        SettingsUtils.assertNoCycles(this);
    }

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param settings The map of settings to use.
     * @param parent The parent settings; or <code>null</code>.
     */
    public Settings(final Map<ISettingKey<?>, Object> settings, @Nullable final Settings parent) {
        // NOTE: ImmutableMap preserved insertion order.
        this(ImmutableMap.copyOf(settings), parent, true);

        Preconditions.checkNotNull(settings);
    }

    /**
     * Gets the parent settings.
     *
     * @return The parent settings;
     * or <code>null</code> when there is no parent
     */
    @Nullable
    public Settings parent() {
        return this.parent;
    }

    /**
     * Gets whether a local setting with the specified key is defined.
     *
     * @param key The key to look for.
     * @param <T> The type of value.
     * @return <code>true</code> when a local setting is defined;
     * otherwise, <code>false</code>.
     */
    public <T> boolean hasLocalSetting(final ISettingKey<T> key) {
        Preconditions.checkNotNull(key);

        return this.settings.containsKey(key);
    }

    /**
     * Gets the local setting with the specified key.
     *
     * @param key The key to look for.
     * @param <T> The type of value.
     * @return The value of the local setting, which may be <code>null</code>.
     * @throws SettingNotFoundException No local setting with the specified key is defined.
     */
    @Nullable
    public <T> T getLocalSetting(final ISettingKey<T> key) {
        Preconditions.checkNotNull(key);

        return getLocalSettingOr(key, () -> {
            throw new SettingNotFoundException(MessageFormatter.format("The setting with key {} was not found.",
                                                                       key).getMessage());
        });
    }

    /**
     * Gets the local setting with the specified key;
     * otherwise returns a default value.
     *
     * @param key The key to look for.
     * @param defaultValue The default value.
     * @param <T> The type of value.
     * @return The local value of the setting, which may be <code>null</code>;
     * or the default value when no local setting with the specified key is defined.
     */
    @Nullable
    public <T> T getLocalSettingOrDefault(final ISettingKey<T> key, @Nullable final T defaultValue) {
        Preconditions.checkNotNull(key);

        return getLocalSettingOr(key, () -> defaultValue);
    }

    /**
     * Gets the local setting with the specified key;
     * otherwise returns the result of a fallback function.
     *
     * @param key The key to look for.
     * @param fallback A function that provides a fallback value.
     * @param <T> The type of value.
     * @return The local value of the setting, which may be <code>null</code>;
     * or the fallback value when no local setting with the specified key is defined.
     */
    @Nullable
    public <T> T getLocalSettingOr(final ISettingKey<T> key, @Nullable final Supplier<T> fallback) {
        Preconditions.checkNotNull(key);

        if (!hasLocalSetting(key))
            return fallback.get();
        //noinspection unchecked
        return (T)this.settings.get(key);
    }

    /**
     * Gets whether a setting with the specified key is defined.
     *
     * This method traverses up the dependency chain until a definition is found.
     *
     * @param key The key to look for.
     * @param <T> The type of value.
     * @return <code>true</code> when a setting is defined;
     * otherwise, <code>false</code>.
     */
    public <T> boolean hasSetting(final ISettingKey<T> key) {
        Preconditions.checkNotNull(key);

        return hasLocalSetting(key) || (this.parent != null && this.parent.hasSetting(key));
    }

    /**
     * Gets the setting with the specified key.
     *
     * This method traverses up the dependency chain until a definition is found.
     *
     * @param key The key to look for.
     * @param <T> The type of value.
     * @return The value of the setting, which may be <code>null</code>.
     * @throws SettingNotFoundException No setting with the specified key is defined.
     */
    @Nullable
    public <T> T getSetting(final ISettingKey<T> key) {
        Preconditions.checkNotNull(key);

        return getSettingOr(key, () -> {
            throw new SettingNotFoundException(MessageFormatter.format("The setting with key {} was not found.", key).getMessage());
        });
    }

    /**
     * Gets the setting with the specified key;
     * otherwise returns a default value.
     *
     * This method traverses up the dependency chain until a definition is found.
     *
     * @param key The key to look for.
     * @param defaultValue The default value.
     * @param <T> The type of value.
     * @return The value of the setting, which may be <code>null</code>;
     * or the default value when no setting with the specified key is defined.
     */
    @Nullable
    public <T> T getSettingOrDefault(final ISettingKey<T> key, @Nullable final T defaultValue) {
        Preconditions.checkNotNull(key);

        return getSettingOr(key, () -> defaultValue);
    }

    /**
     * Gets the setting with the specified key;
     * otherwise returns the result of a fallback function.
     *
     * This method traverses up the dependency chain until a definition is found.
     *
     * @param key The key to look for.
     * @param fallback A function that provides a fallback value.
     * @param <T> The type of value.
     * @return The value of the setting, which may be <code>null</code>;
     * or the fallback value when no setting with the specified key is defined.
     */
    @Nullable
    public <T> T getSettingOr(final ISettingKey<T> key, @Nullable final Supplier<T> fallback) {
        Preconditions.checkNotNull(key);

        boolean hasLocalValue = hasLocalSetting(key);
        T localValue = getLocalSettingOrDefault(key, null);
        boolean hasParentValue;
        T parentValue;
        if (this.parent != null) {
            hasParentValue = this.parent.hasSetting(key);
            parentValue = this.parent.getSettingOrDefault(key, null);
        } else {
            hasParentValue = false;
            parentValue = null;
        }

        if (!hasParentValue && !hasLocalValue)
            return fallback.get();
        else if (!hasParentValue)
            return localValue;
        else if (!hasLocalValue)
            return parentValue;
        else
            return key.inheritanceStrategy().apply(key, localValue, parentValue);
    }

    /**
     * Gets all locally defined setting keys.
     *
     * @return A set of keys.
     */
    public Set<ISettingKey<?>> getAllLocalSettings() {
        return ImmutableSet.copyOf(this.settings.keySet());
    }

    /**
     * Gets all defined setting keys in this settings and any ancestors.
     *
     * @return A set of keys.
     */
    public Set<ISettingKey<?>> getAllSettings() {
        HashSet<ISettingKey<?>> keys = Sets.newHashSet(this.settings.keySet());
        Settings current = this.parent();
        while (current != null) {
            keys.addAll(current.getAllLocalSettings());
        }
        return keys;
    }
}
