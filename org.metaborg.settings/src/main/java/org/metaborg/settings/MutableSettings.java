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

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Setting in a settings hierarchy.
 * <p>
 * You can derive from this class to provide getters and setters.
 */
public class MutableSettings extends Settings {

    private final Map<ISettingKey<?>, Object> settings;

    /**
     * Initializes a new instance of the {@link MutableSettings} class.
     */
    public MutableSettings() {
        this(null);
    }

    /**
     * Initializes a new instance of the {@link MutableSettings} class.
     *
     * @param parent The parent settings; or <code>null</code>.
     */
    public MutableSettings(@Nullable final Settings parent) {
        this(new LinkedHashMap<>(), parent);
    }

    /**
     * Initializes a new instance of the {@link MutableSettings} class.
     *
     * @param settings The map of settings to use.
     * @param parent   The parent settings; or <code>null</code>.
     */
    public MutableSettings(final Map<ISettingKey<?>, Object> settings, @Nullable final Settings parent) {
        super(settings, parent, true);

        Preconditions.checkNotNull(settings);
        this.settings = settings;
    }

    /**
     * Sets the local setting with the specified key to the specified value.
     *
     * @param key   The key to set.
     * @param value The value to set the key to, which may be <code>null</code>.
     * @param <T>   The type of value.
     */
    public <T> void setLocalSetting(final ISettingKey<T> key, @Nullable final T value) {
        Preconditions.checkNotNull(key);

        this.settings.put(key, value);
    }

    /**
     * Clears the local setting with the specified key.
     * <p>
     * It is not an error to clear an undefined setting.
     *
     * @param key The key to clear.
     * @param <T> The type of value.
     */
    public <T> void clearLocalSetting(final ISettingKey<T> key) {
        Preconditions.checkNotNull(key);

        this.settings.remove(key);
    }

}
