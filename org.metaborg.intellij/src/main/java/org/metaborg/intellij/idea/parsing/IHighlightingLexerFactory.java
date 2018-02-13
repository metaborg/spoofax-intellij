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

package org.metaborg.intellij.idea.parsing;

import com.intellij.lexer.Lexer;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenTypeManager;

import javax.annotation.Nullable;

/**
 * Factory for highlighting lexers.
 */
@Deprecated
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
    Lexer create(@Nullable FileObject file, @Nullable IProject project, ILanguageImpl language,
                 SpoofaxTokenTypeManager tokenTypesManager);
}
