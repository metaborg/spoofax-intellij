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

package org.metaborg.intellij.idea.extensions;

import com.intellij.lang.*;
import com.intellij.openapi.fileTypes.*;

/**
 * Syntax highlighter factory extension point value wrapper.
 * <p>
 * This wrapper is used to provide an instance to a language extension point instead of a class.
 */
public final class InstanceSyntaxHighlighterFactoryExtensionPoint extends SyntaxHighlighterFactoryEP
        implements IExtensionPointValue {

    private final SyntaxHighlighterFactory instance;

    /**
     * Initializes a new instance of the {@link InstanceLanguageExtensionPoint} class.
     *
     * @param language The language.
     * @param instance The instance.
     */
    public InstanceSyntaxHighlighterFactoryExtensionPoint(
            final Language language,
            final SyntaxHighlighterFactory instance) {
        super();
        this.instance = instance;
        this.language = language.getID();
        this.implementationClass = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SyntaxHighlighterFactory getInstance() {
        return this.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ExtensionIds.SyntaxHighlighterFactory;
    }
}
