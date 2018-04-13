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

package org.metaborg.intellij.idea.parsing.elements;

import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;

/**
 * Factory for ATerm AST element type providers.
 */
public interface IATermAstElementTypeProviderFactory {

    /**
     * Creates a new ATerm AST element type provider.
     *
     * @param language         The language implementation.
     * @param parseResult      The parse result.
     * @param tokenTypeManager The token type manager.
     * @return The created ATerm AST element type providers.
     */
    ATermAstElementTypeProvider create(
            ILanguageImpl language,
            ISpoofaxParseUnit parseResult,
            SpoofaxTokenTypeManager tokenTypeManager);

}
