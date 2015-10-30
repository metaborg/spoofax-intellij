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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.StringFormatter;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default settings.
 *
 * This is intended for immutable (default) settings.
 */
public class DefaultSettings extends Settings {

    /**
     * Initializes a new instance of the {@link DefaultSettings} class.
     * @param settings A map with the default settings.
     */
    public DefaultSettings(@NotNull final Map<SettingKey<?>, Object> settings) {
        super(settings, null);
        Preconditions.checkNotNull(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Settings parent() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean hasSetting(@NotNull final SettingKey<T> key) {
        Preconditions.checkNotNull(key);

        return hasLocalSetting(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T> T getSetting(@NotNull final SettingKey<T> key) {
        Preconditions.checkNotNull(key);

        return getLocalSetting(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T> T getSettingOrDefault(@NotNull final SettingKey<T> key, @Nullable final T defaultValue) {
        Preconditions.checkNotNull(key);

        return getLocalSettingOrDefault(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Set<SettingKey<?>> getAllSettings() {
        return getAllLocalSettings();
    }
}
