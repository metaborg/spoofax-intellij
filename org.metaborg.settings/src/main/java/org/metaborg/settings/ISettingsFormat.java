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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A settings storage format.
 * <p>
 * Use the methods of the {@link SettingsFormatUtils} instead of calling the members
 * of this interface directly.
 */
public interface ISettingsFormat {

    /**
     * Reads settings from an input stream.
     *
     * @param input  The input stream.
     * @param parent The parent settings; or <code>null</code>.
     * @return The read settings.
     */
    Settings read(InputStream input, @Nullable Settings parent) throws IOException;

    /**
     * Writes settings to an output stream.
     *
     * @param output   The output stream.
     * @param settings The settings to write.
     */
    void write(
            final OutputStream output, final Settings settings) throws IOException;

}
