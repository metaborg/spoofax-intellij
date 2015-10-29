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

package org.metaborg.core.project.settings;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.StringFormatter;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Setting in a settings hierarchy.
 *
 * Derive from this class for immutable settings.
 */
public class Settings {

    @NotNull
    /* package private */ final Map<SettingKey<?>, Object> settings;
    @Nullable
    private final Settings parent;

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param settings The settings map to use.
     * @param parent The parent settings; or <code>null</code>.
     * @param internal Unused.
     */
    /* package private */ Settings(@NotNull final Map<SettingKey<?>, Object> settings, @Nullable final Settings parent, final boolean internal) {
        this.settings = settings;
        this.parent = parent;
        SettingsUtils.assertNoCycles(this);
    }

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param settings The default settings to use.
     * @param parent The parent settings; or <code>null</code>.
     */
    public Settings(@NotNull final Map<SettingKey<?>, Object> settings, @Nullable final Settings parent) {
        this(ImmutableMap.copyOf(settings), parent, true);
    }

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param parent The parent settings; or <code>null</code>.
     */
    public Settings(@Nullable final Settings parent) {
        this(new LinkedHashMap<>(), parent);
    }

    /**
     * Initializes a new instance of the {@link Settings} class.
     */
    public Settings() {
        this(null);
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
    public <T> boolean hasLocalSetting(@NotNull final SettingKey<T> key) {
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
    public <T> T getLocalSetting(@NotNull final SettingKey<T> key) {
        Preconditions.checkNotNull(key);

        if (!hasLocalSetting(key))
            throw new SettingNotFoundException(StringFormatter.format("The setting with key {} was not found.", key));
        //noinspection unchecked
        return (T)this.settings.get(key);
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
    public <T> T getLocalSettingOrDefault(@NotNull final SettingKey<T> key, @Nullable final T defaultValue) {
        Preconditions.checkNotNull(key);

        if (!hasLocalSetting(key))
            return defaultValue;
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
    public <T> boolean hasSetting(@NotNull final SettingKey<T> key) {
        Preconditions.checkNotNull(key);

        Settings settings = getSettingsWithDefinitionForKey(key);
        return settings != null;
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
    public <T> T getSetting(@NotNull final SettingKey<T> key) {
        Preconditions.checkNotNull(key);

        Settings settings = getSettingsWithDefinitionForKey(key);
        if (settings == null)
            throw new SettingNotFoundException(StringFormatter.format("The setting with key {} was not found.", key));
        return settings.getLocalSetting(key);
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
    public <T> T getSettingOrDefault(@NotNull final SettingKey<T> key, @Nullable final T defaultValue) {
        Preconditions.checkNotNull(key);

        Settings settings = getSettingsWithDefinitionForKey(key);
        if (settings == null)
            return defaultValue;
        return settings.getLocalSetting(key);
    }

    /**
     * Gets all locally defined setting keys.
     *
     * @return A set of keys.
     */
    @NotNull
    public Set<SettingKey<?>> getAllLocalSettings() {
        return ImmutableSet.copyOf(this.settings.keySet());
    }

    /**
     * Gets all defined setting keys in this settings and any ancestors.
     *
     * @return A set of keys.
     */
    @NotNull
    public Set<SettingKey<?>> getAllSettings() {
        HashSet<SettingKey<?>> keys = Sets.newHashSet(this.settings.keySet());
        Settings current = this.parent();
        while (current != null) {
            keys.addAll(current.getAllLocalSettings());
        }
        return keys;
    }

    /**
     * Traverses up the dependency chain until a {@link Settings} with a definition
     * for the key has been found.
     *
     * @param key The key to look for.
     * @return The {@link Settings} with a definition for the key; or <code>null</code> when not found.
     */
    @Nullable
    private Settings getSettingsWithDefinitionForKey(@NotNull final SettingKey<?> key) {
        Settings current = this;
        while (current != null && !current.hasLocalSetting(key)) {
            current = current.parent();
        }
        return current;
    }
}
