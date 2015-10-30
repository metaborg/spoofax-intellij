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

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public final class SettingsStubFactory implements ISettingsFactory {

    @NotNull
    @Override
    public Set<SettingDescriptor> settingDescriptors() {
        return Sets.newHashSet(
                new SettingDescriptor(SettingsStub.NAME_KEY, false),
                new SettingDescriptor(SettingsStub.ID_KEY, false),
                new SettingDescriptor(SettingsStub.LIST_OF_ID_KEY, false)
        );
    }

    @NotNull
    @Override
    public Settings create(
            @NotNull final Map<SettingKey, Object> settings, @Nullable final Settings parent) {
        return new SettingsStub(settings, parent);
    }
}
