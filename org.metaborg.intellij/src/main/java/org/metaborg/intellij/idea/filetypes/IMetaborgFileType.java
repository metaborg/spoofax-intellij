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

import com.intellij.openapi.fileTypes.FileType;

/**
 * An interface for file types of Metaborg languages.
 */
@Deprecated
public interface IMetaborgFileType extends FileType {

    /**
     * Gets all extensions for which this file type must be registered by default.
     *
     * @return A sequence of extensions, all without a leading '.'.
     */
    Iterable<String> getExtensions();

}
