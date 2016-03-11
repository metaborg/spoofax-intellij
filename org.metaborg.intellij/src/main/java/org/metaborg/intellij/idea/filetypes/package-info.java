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