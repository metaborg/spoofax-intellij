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
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.metaborg.settings.ISettingKey;
import org.metaborg.settings.ISettingsFactory;
import org.metaborg.settings.SettingDescriptor;
import org.metaborg.settings.Settings;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * Factory for the {@link NewProjectSettings} class.
 */
@Singleton
public final class NewProjectSettingsFactory implements ISettingsFactory {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Set<SettingDescriptor> settingDescriptors() {
        return Sets.newHashSet(
                new SettingDescriptor(NewProjectSettings.IDENTIFIER_KEY, false),
                new SettingDescriptor(NewProjectSettings.NAME_KEY, false),
                new SettingDescriptor(NewProjectSettings.COMPILE_DEPENDENCIES_KEY, false),
                new SettingDescriptor(NewProjectSettings.RUNTIME_DEPENDENCIES_KEY, false),
                new SettingDescriptor(NewProjectSettings.LANGUAGE_CONTRIBUTIONS_KEY, false)
        );
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Settings create(
            @NotNull final Map<ISettingKey<?>, Object> settings, @Nullable final Settings parent) {
        return new NewProjectSettings(settings, parent);
    }
}
