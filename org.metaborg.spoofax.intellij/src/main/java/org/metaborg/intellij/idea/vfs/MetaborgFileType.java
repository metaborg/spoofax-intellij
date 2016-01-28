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

package org.metaborg.intellij.idea.vfs;

import com.intellij.ide.highlighter.ArchiveFileType;
import com.intellij.openapi.fileTypes.*;

/**
 * Extended file type.
 *
 * When implementing a file type that has content of a particular language,
 * extend the {@link LanguageFileType} class. Otherwise, extend one of the
 * {@link ArchiveFileType} (*.zip), {@link PlainTextFileType} (*.txt, *.sh),
 * {@link NativeFileType} (*.docx, *.chm) or {@link UnknownFileType} (*.lib,
 * *.dll) classes.
 */
public interface MetaborgFileType extends FileType {

    /**
     * Gets all extensions for which this file type must be registered by default.
     *
     * @return A sequence of extensions, all without a leading '.'.
     */
    Iterable<String> getExtensions();

}
