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
import org.apache.commons.vfs2.FileObject;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Utility functions for working with {@link ISettingsFormat}.
 */
public final class SettingsFormatUtils {

    // Prevent instantiation.
    private SettingsFormatUtils() { }

    /**
     * Reads settings from an input stream.
     *
     * @param format The settings format.
     * @param input The input stream.
     * @return The read settings.
     */
    public static Settings readFromStream(final ISettingsFormat format, final InputStream input) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(input);

        return readFromStream(format, input, null);
    }

    /**
     * Reads settings from an input stream.
     *
     * @param format The settings format.
     * @param input The input stream.
     * @param parent The parent settings; or <code>null</code>.
     * @return The read settings.
     */
    public static Settings readFromStream(final ISettingsFormat format, final InputStream input, @Nullable final Settings parent) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(input);

        return format.read(input, parent);
    }

    /**
     * Writes settings to an output stream.
     *
     * @param format The settings format.
     * @param output The output stream.
     * @param settings The settings to write.
     */
    public static void writeToStream(final ISettingsFormat format, final OutputStream output, final Settings settings) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(output);
        Preconditions.checkNotNull(settings);

        format.write(output, settings);
    }

    /**
     * Reads settings from a file.
     *
     * @param format The settings format.
     * @param file The input file.
     * @return The read settings.
     */
    public static Settings readFromFile(final ISettingsFormat format, final File file) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(file);

        return readFromFile(format, file, null);
    }

    /**
     * Reads settings from a file.
     *
     * @param format The settings format.
     * @param file The input file.
     * @param parent The parent settings; or <code>null</code>.
     * @return The read settings.
     */
    public static Settings readFromFile(final ISettingsFormat format, final File file, @Nullable final Settings parent) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(file);

        try (InputStream input = new FileInputStream(file)) {
            return format.read(input, parent);
        }
    }

    /**
     * Writes settings to a file.
     *
     * @param format The settings format.
     * @param file The output file.
     * @param settings The settings to write.
     */
    public static void writeToFile(final ISettingsFormat format, final File file, final Settings settings) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(settings);

        try (OutputStream output = new FileOutputStream(file)) {
            format.write(output, settings);
        }
    }

    /**
     * Reads settings from a file.
     *
     * @param format The settings format.
     * @param file The input file.
     * @return The read settings.
     */
    public static Settings readFromFile(final ISettingsFormat format, final FileObject file) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(file);

        return readFromFile(format, file, null);
    }

    /**
     * Reads settings from a file.
     *
     * @param format The settings format.
     * @param file The input file.
     * @param parent The parent settings; or <code>null</code>.
     * @return The read settings.
     */
    public static Settings readFromFile(final ISettingsFormat format, final FileObject file, @Nullable final Settings parent) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(file);

        try (InputStream input = file.getContent().getInputStream()) {
            return format.read(input, parent);
        }
    }

    /**
     * Writes settings to a file.
     *
     * @param format The settings format.
     * @param file The output file.
     * @param settings The settings to write.
     */
    public static void writeToFile(final ISettingsFormat format, final FileObject file, final Settings settings) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(settings);

        try (OutputStream output = file.getContent().getOutputStream(false)) {
            format.write(output, settings);
        }
    }

    /**
     * Reads settings from a string.
     *
     * @param format The settings format.
     * @param str The input string.
     * @return The read settings.
     */
    public static Settings readFromString(final ISettingsFormat format, final String str) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(str);

        return readFromString(format, str, null);
    }

    /**
     * Reads settings from a string.
     *
     * @param format The settings format.
     * @param str The input string.
     * @param parent The parent settings; or <code>null</code>.
     * @return The read settings.
     */
    public static Settings readFromString(final ISettingsFormat format, final String str, @Nullable final Settings parent) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(str);

        try (InputStream input = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8))) {
            return format.read(input, parent);
        }
    }

    /**
     * Writes settings to a string.
     *
     * @param format The settings format.
     * @param settings The setting to write.
     * @return The resulting string.
     */
    public static String writeToString(final ISettingsFormat format, final Settings settings) throws IOException {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(settings);

        try(OutputStream output = new ByteArrayOutputStream()) {
            format.write(output, settings);
            return output.toString();
        }
    }
}
