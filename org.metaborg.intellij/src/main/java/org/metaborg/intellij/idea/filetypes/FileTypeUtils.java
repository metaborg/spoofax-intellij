/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.filetypes;

import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;

/**
 * Utility functions for working with IntelliJ IDEA file types.
 */
@Deprecated
public final class FileTypeUtils {

    /**
     * Registers the given file type with IntelliJ IDEA.
     *
     * @param fileType The file type to register.
     */
    @Deprecated
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
    @Deprecated
    public static void unregister(final IMetaborgFileType fileType) {
        FileTypeManagerEx.getInstanceEx().unregisterFileType(fileType);
    }

    private FileTypeUtils() { /* Prevent instantiation. */ }

}
