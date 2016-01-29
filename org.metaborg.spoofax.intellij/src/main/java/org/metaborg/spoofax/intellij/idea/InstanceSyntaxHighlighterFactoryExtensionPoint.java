/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.idea;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactoryEP;
import org.jetbrains.annotations.NotNull;

/**
 * Syntax highlighter factory extension point value wrapper.
 * <p>
 * This wrapper is used to provide an instance to a language extension point instead of a class.
 */
public final class InstanceSyntaxHighlighterFactoryExtensionPoint extends SyntaxHighlighterFactoryEP {
    @NotNull
    private final SyntaxHighlighterFactory instance;

    /**
     * Initializes a new instance of the {@link InstanceLanguageExtensionPoint} class.
     *
     * @param language The language.
     * @param instance The instance.
     */
    public InstanceSyntaxHighlighterFactoryExtensionPoint(
            @NotNull final Language language,
            @NotNull final SyntaxHighlighterFactory instance) {
        super();
        this.instance = instance;
        this.language = language.getID();
        this.implementationClass = null;
    }

    @NotNull
    @Override
    public final SyntaxHighlighterFactory getInstance() {
        return this.instance;
    }
}
