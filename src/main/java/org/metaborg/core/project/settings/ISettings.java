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
 */
public interface ISettings {

    /**
     * Gets the parent settings.
     *
     * @return The parent settings;
     * or <code>null</code> when there is no parent
     */
    @Nullable
    ISettings parent();

    /**
     * Gets whether a local setting with the specified key is defined.
     *
     * @param key The key to look for.
     * @param <T> The type of value.
     * @return <code>true</code> when a local setting is defined;
     * otherwise, <code>false</code>.
     */
    <T> boolean hasLocalSetting(@NotNull SettingKey<T> key);

    /**
     * Gets the local setting with the specified key.
     *
     * @param key The key to look for.
     * @param <T> The type of value.
     * @return The value of the local setting, which may be <code>null</code>.
     * @throws SettingNotFoundException No local setting with the specified key is defined.
     */
    @Nullable
    <T> T getLocalSetting(@NotNull SettingKey<T> key);

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
    <T> T getLocalSettingOrDefault(@NotNull SettingKey<T> key, @Nullable T defaultValue);

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
    <T> boolean hasSetting(@NotNull SettingKey<T> key);

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
    <T> T getSetting(@NotNull SettingKey<T> key);

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
    <T> T getSettingOrDefault(@NotNull SettingKey<T> key, @Nullable T defaultValue);

    /**
     * Gets all locally defined setting keys.
     *
     * @return A set of keys.
     */
    @NotNull
    Set<SettingKey<?>> getAllLocalSettings();

    /**
     * Gets all defined setting keys in this settings and any ancestors.
     *
     * @return A set of keys.
     */
    @NotNull
    Set<SettingKey<?>> getAllSettings();

}
