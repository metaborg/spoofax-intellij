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
 * Read/writes settings.
 */
public interface ISettingsSerializer {

    /**
     * Reads settings from an input stream.
     *
     * @param input The input stream.
     * @return The read settings.
     */
    ISettings readFrom(@NotNull InputStream input);

    /**
     * Writes settings to an output stream.
     *
     * @param output The output stream.
     * @param settings The settings to write.
     */
    void writeTo(@NotNull OutputStream output, @NotNull ISettings settings);

    /**
     * Reads settings from a file.
     *
     * @param input The input file.
     * @return The read settings.
     */
    ISettings readFrom(@NotNull FileObject input) throws IOException;

    /**
     * Writes settings to a file.
     *
     * @param output The output file.
     * @param settings The settings to write.
     */
    void writeTo(@NotNull FileObject output, @NotNull ISettings settings) throws IOException;

}
