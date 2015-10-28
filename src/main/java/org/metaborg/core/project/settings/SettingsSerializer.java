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
import org.apache.commons.vfs2.FileSystemException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Settings serializer base class.
 */
public abstract class SettingsSerializer implements ISettingsSerializer {

    /**
     * Initializes a new instance of the {@link SettingsSerializer} class.
     */
    protected SettingsSerializer() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ISettings readFrom(@NotNull final InputStream input);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void writeTo(
            @NotNull final OutputStream output, @NotNull final ISettings settings);

    /**
     * {@inheritDoc}
     */
    @Override
    public final ISettings readFrom(@NotNull final FileObject file) throws IOException {
        try (InputStream input = file.getContent().getInputStream()) {
            return readFrom(input);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void writeTo(@NotNull final FileObject file, @NotNull final ISettings settings) throws IOException {
        try (OutputStream output = file.getContent().getOutputStream(false)) {
            writeTo(output, settings);
        }
    }
}
