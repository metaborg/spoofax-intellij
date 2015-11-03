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

/**
 * Describes a setting.
 */
public final class SettingDescriptor {

    private final SettingKey key;
    private final boolean required;

    /**
     * Initializes a new instance of the {@link SettingDescriptor} class.
     *
     * @param key The setting key.
     * @param required Whether the setting is required.
     */
    public SettingDescriptor(final SettingKey key, final boolean required) {
        Preconditions.checkNotNull(key);

        this.key = key;
        this.required = required;
    }

    /**
     * Gets the key.
     *
     * @return The key.
     */
    public SettingKey key() {
        return this.key;
    }

    /**
     * Whether the setting is required.
     *
     * @return <code>true</code> when the setting is required;
     * otherwise, <code>false</code>.
     */
    public boolean required() {
        return this.required;
    }
}
