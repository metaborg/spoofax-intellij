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

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * Factory for {@link Settings} objects.
 */
public interface ISettingsFactory {

    /**
     * Gets the setting descriptors for the settings.
     * @return The setting descriptors.
     */
    Set<SettingDescriptor> settingDescriptors();

    /**
     * Creates a settings object.
     *
     * @param settings A map with the settings.
     * @param parent The parent settings; or <code>null</code>.
     * @return The created settings object.
     */
    Settings create(Map<ISettingKey<?>, Object> settings, @Nullable Settings parent);
}