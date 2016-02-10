/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.idea.filetypes;

import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.fileTypes.ex.*;

/**
 * Utility functions for working with IntelliJ IDEA file types.
 */
public final class FileTypeUtils {

    /**
     * Registers the given file type with IntelliJ IDEA.
     *
     * @param fileType The file type to register.
     */
    public static void register(final IMetaborgFileType fileType) {
        FileTypeManagerEx.getInstanceEx().registerFileType(fileType);

        // Associate the file type with its file extensions.
        final FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        for (final String ext : fileType.getExtensions()) {
            final FileNameMatcher matcher = new ExtensionFileNameMatcher(ext);
            fileTypeManager.associate(fileType, matcher);
        }
    }

    /**
     * Unregisters the given file type from IntelliJ IDEA.
     *
     * @param fileType The file type to unregister.
     */
    public static void unregister(final IMetaborgFileType fileType) {
        FileTypeManagerEx.getInstanceEx().unregisterFileType(fileType);
    }

    private FileTypeUtils() { /* Prevent instantiation. */ }

}
