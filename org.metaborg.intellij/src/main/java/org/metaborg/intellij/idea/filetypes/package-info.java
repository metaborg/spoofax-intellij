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

/**
 * Functionality for working with file types.
 *
 * When implementing a file type that has content of a particular language,
 * extend the {@link com.intellij.openapi.fileTypes.LanguageFileType} class. Otherwise,
 * extend one of the {@link com.intellij.ide.highlighter.ArchiveFileType} (*.zip),
 * {@link com.intellij.openapi.fileTypes.PlainTextFileType} (*.txt, *.sh),
 * {@link com.intellij.openapi.fileTypes.NativeFileType} (*.docx, *.chm) or
 * {@link com.intellij.openapi.fileTypes.UnknownFileType} (*.lib, *.dll) classes.
 *
 * Additionally, file types must implement the
 * {@link org.metaborg.intellij.idea.filetypes.IMetaborgFileType} interface. Then they
 * can be registered and unregistered using the {@link org.metaborg.intellij.idea.filetypes.FileTypeUtils}
 * class.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.filetypes;

import org.metaborg.intellij.*;