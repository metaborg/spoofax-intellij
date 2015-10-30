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

package org.metaborg.settings.serialization;

import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.settings.Settings;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A settings storage format.
 */
public abstract class SettingsFormat {

    /**
     * Initializes a new instance of the {@link SettingsFormat} class.
     */
    protected SettingsFormat() {

    }

    /**
     * Reads settings from an input stream.
     *
     * @param input The input stream.
     * @param parent The parent settings; or <code>null</code>.
     * @return The read settings.
     */
    @NotNull
    public abstract Settings readFromStream(@NotNull InputStream input, @Nullable Settings parent) throws IOException;

    /**
     * Writes settings to an output stream.
     *
     * @param output The output stream.
     * @param settings The settings to write.
     */
    public abstract void writeToStream(
            @NotNull final OutputStream output, @NotNull final Settings settings) throws IOException;


    /**
     * Reads settings from an input stream.
     *
     * @param input The input stream.
     * @return The read settings.
     */
    @NotNull
    public final Settings readFromStream(@NotNull InputStream input) throws IOException {
        Preconditions.checkNotNull(input);

        return readFromStream(input, null);
    }


    /**
     * Reads settings from a file.
     *
     * @param file The input file.
     * @return The read settings.
     */
    @NotNull
    public final Settings readFromFile(@NotNull FileObject file) throws IOException {
        Preconditions.checkNotNull(file);

        return readFromFile(file, null);
    }

    /**
     * Reads settings from a file.
     *
     * @param file The input file.
     * @param parent The parent settings; or <code>null</code>.
     * @return The read settings.
     */
    @NotNull
    public final Settings readFromFile(@NotNull FileObject file, @Nullable Settings parent) throws IOException {
        Preconditions.checkNotNull(file);

        try (InputStream input = file.getContent().getInputStream()) {
            return readFromStream(input, parent);
        }
    }

    /**
     * Reads settings from a string.
     *
     * @param str The input string.
     * @return The read settings.
     */
    @NotNull
    public final Settings readFromString(@NotNull String str) throws IOException {
        Preconditions.checkNotNull(str);

        return readFromString(str, null);
    }

    /**
     * Reads settings from a string.
     *
     * @param str The input string.
     * @param parent The parent settings; or <code>null</code>.
     * @return The read settings.
     */
    @NotNull
    public final Settings readFromString(@NotNull String str, @Nullable Settings parent) throws IOException {
        Preconditions.checkNotNull(str);

        try (InputStream input = IOUtils.toInputStream(str, "UTF-8")) {
            return readFromStream(input, parent);
        }
    }

    /**
     * Writes settings to a file.
     *
     * @param file The output file.
     * @param settings The settings to write.
     */
    public final void writeToFile(@NotNull final FileObject file, @NotNull final Settings settings) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(settings);

        try (OutputStream output = file.getContent().getOutputStream(false)) {
            writeToStream(output, settings);
        }
    }

    /**
     * Writes settings to a string.
     *
     * @param settings The setting to write.
     * @return The resulting string.
     */
    @NotNull
    public final String writeToString(@NotNull Settings settings) throws IOException {
        Preconditions.checkNotNull(settings);

        try(OutputStream output = new ByteArrayOutputStream()) {
            writeToStream(output, settings);
            return output.toString();
        }
    }
}
