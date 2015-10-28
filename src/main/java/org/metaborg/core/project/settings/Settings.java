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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Setting in a settings hierarchy.
 *
 * This is intended for unmodifiable settings.
 */
public class Settings implements ISettings {

    @NotNull
    /* package private */ final Map<SettingKey<?>, Object> settings;
    @Nullable
    private final ISettings parent;

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param parent The parent settings; or <code>null</code>.
     * @param settings The settings map to use.
     * @param internal Unused.
     */
    /* package private */ Settings(@Nullable final ISettings parent, @NotNull final Map<SettingKey<?>, Object> settings, final boolean internal) {
        this.parent = parent;
        this.settings = settings;
        SettingsUtils.assertNoCycles(this);
    }

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param parent The parent settings; or <code>null</code>.
     * @param settings The default settings to use.
     */
    public Settings(@Nullable final ISettings parent, @NotNull final Map<SettingKey<?>, Object> settings) {
        this(parent, ImmutableMap.copyOf(settings), true);
    }

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param parent The parent settings; or <code>null</code>.
     */
    public Settings(@Nullable final ISettings parent) {
        this(parent, new HashMap<>());
    }

    /**
     * Initializes a new instance of the {@link Settings} class.
     */
    public Settings() {
        this(null);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    public ISettings parent() {
        return this.parent;
    }

    /**
     * {@inheritDoc}
     */
    public <T> boolean hasLocalSetting(@NotNull final SettingKey<T> key) {
        Preconditions.checkNotNull(key);

        return this.settings.containsKey(key);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public <T> boolean hasSetting(@NotNull final SettingKey<T> key) {
        Preconditions.checkNotNull(key);

        ISettings settings = getSettingsWithDefinitionForKey(key);
        return settings != null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    public <T> T getSetting(@NotNull final SettingKey<T> key) {
        Preconditions.checkNotNull(key);

        ISettings settings = getSettingsWithDefinitionForKey(key);
        if (settings == null)
            throw new SettingNotFoundException(StringFormatter.format("The setting with key {} was not found.", key));
        return settings.getLocalSetting(key);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    public <T> T getSettingOrDefault(@NotNull final SettingKey<T> key, @Nullable final T defaultValue) {
        Preconditions.checkNotNull(key);

        ISettings settings = getSettingsWithDefinitionForKey(key);
        if (settings == null)
            return defaultValue;
        return settings.getLocalSetting(key);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    public Set<SettingKey<?>> getAllLocalSettings() {
        return ImmutableSet.copyOf(this.settings.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    public Set<SettingKey<?>> getAllSettings() {
        HashSet<SettingKey<?>> keys = Sets.newHashSet(this.settings.keySet());
        ISettings current = this.parent();
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
    private ISettings getSettingsWithDefinitionForKey(@NotNull final SettingKey<?> key) {
        ISettings current = this;
        while (current != null && !current.hasLocalSetting(key)) {
            current = current.parent();
        }
        return current;
    }
}
