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

package org.metaborg.intellij.discovery;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.*;

import jakarta.annotation.Nullable;

/**
 * A language source.
 *
 * This is used to discover a language from its identifier. Implementations could
 * be as simple as looking into a folder, or as complex as browsing a Maven repository.
 *
 * The implementation must be thread-safe.
 */
public interface ILanguageSource {

    /**
     * Attempts to find a language with the specified identifier.
     *
     * If there are somehow multiple languages with the specified identifier,
     * the implementation may decide which one to return.
     *
     * This method may take a while.
     *
     * @param id The identifier to look for.
     * @return The file of the language; or <code>null</code> if not found.
     */
    @Nullable
    FileObject find(LanguageIdentifier id);

}
