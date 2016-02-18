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

package org.metaborg.intellij.idea.parsing;

import com.intellij.lexer.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.parsing.elements.*;

import javax.annotation.*;

/**
 * Factory for highlighting lexers.
 */
public interface IHighlightingLexerFactory {

    /**
     * Creates a highlighting lexer.
     *
     * @param file The file being lexed; or <code>null</code> when unknown.
     * @param project The project that contains the file; or <code>null</code> when unknown.
     * @param language          The language implementation.
     * @param tokenTypesManager The token type manager.
     * @return The created lexer.
     */
    Lexer create(FileObject file, @Nullable IProject project, ILanguageImpl language,
                 SpoofaxTokenTypeManager tokenTypesManager);
}
