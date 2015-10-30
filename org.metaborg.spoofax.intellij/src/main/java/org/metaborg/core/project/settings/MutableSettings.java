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
 * Derive from this class for mutable settings.
 */
public class MutableSettings extends Settings {

    @NotNull
    private final Map<SettingKey, Object> settings = new HashMap<>();

    /**
     * Initializes a new instance of the {@link MutableSettings} class.
     *
     * @param parent The parent settings; or <code>null</code>.
     */
    public MutableSettings(@Nullable final Settings parent) {
        super(new HashMap<>(), parent, true);
    }

    /**
     * Initializes a new instance of the {@link MutableSettings} class.
     */
    public MutableSettings() {
        this(null);
    }

    /**
     * Sets the local setting with the specified key to the specified value.
     *
     * @param key The key to set.
     * @param value The value to set the key to, which may be <code>null</code>.
     * @param <T> The type of value.
     */
    public <T> void setLocalSetting(@NotNull final SettingKey key, @Nullable final T value) {
        Preconditions.checkNotNull(key);

        this.settings.put(key, value);
    }

    /**
     * Clears the local setting with the specified key.
     *
     * It is not an error to clear an undefined setting.
     *
     * @param key The key to clear.
     * @param <T> The type of value.
     */
    public <T> void clearLocalSetting(@NotNull final SettingKey key) {
        Preconditions.checkNotNull(key);

        this.settings.remove(key);
    }

}
