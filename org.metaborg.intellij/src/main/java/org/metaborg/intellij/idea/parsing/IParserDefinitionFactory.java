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

import com.intellij.lang.*;
import com.intellij.psi.tree.*;
import org.metaborg.intellij.idea.filetypes.*;

/**
 * Factory for parser definitions.
 */
public interface IParserDefinitionFactory {

    /**
     * Creates a new parser definition for the specified file type.
     *
     * @param fileType        The file type.
     * @param fileElementType The file element type.
     * @return The created parser definition.
     */
    ParserDefinition create(MetaborgLanguageFileType fileType, IFileElementType fileElementType);

}
