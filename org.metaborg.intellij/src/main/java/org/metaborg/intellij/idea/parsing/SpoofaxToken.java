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

import org.metaborg.intellij.IntRange;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenType;

/**
 * Represents a Spoofax token. This class contains enough information
 * to reconstruct the Spoofax AST.
 */
@Deprecated
/* package private */ final class SpoofaxToken {

    private final SpoofaxTokenType type;
    private final IntRange range;

    public SpoofaxTokenType type() { return this.type; }

    public IntRange range() { return this.range; }

    /**
     * Initializes a new instance of the {@link SpoofaxToken} class.
     *
     * @param type  The token type.
     * @param range The range of the token.
     */
    public SpoofaxToken(final SpoofaxTokenType type, final IntRange range) {
        this.type = type;
        this.range = range;
    }

    @Override
    public String toString() {
        return this.type.toString() + " [" + this.range.start + ", " + this.range.end + ")";
    }

}