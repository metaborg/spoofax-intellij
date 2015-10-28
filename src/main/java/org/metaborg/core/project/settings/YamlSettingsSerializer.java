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

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Settings serializer using YAML.
 */
public final class YamlSettingsSerializer extends SettingsSerializer {

    @NotNull private final ISettingsFactory settingsFactory;

    /* package private */ YamlSettingsSerializer(@NotNull final ISettingsFactory settingsFactory) {
        this.settingsFactory = settingsFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ISettings readFrom(@NotNull final InputStream input) {
        // TODO
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTo(
            @NotNull final OutputStream output, @NotNull final ISettings settings) {
        // TODO
    }
}
