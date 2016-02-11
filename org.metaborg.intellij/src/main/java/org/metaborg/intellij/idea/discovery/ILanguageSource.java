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

package org.metaborg.intellij.idea.discovery;

import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;

import javax.annotation.*;

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
