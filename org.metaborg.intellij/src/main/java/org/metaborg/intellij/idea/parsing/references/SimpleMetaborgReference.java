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

package org.metaborg.intellij.idea.parsing.references;

import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.idea.parsing.elements.MetaborgDeclarationElement;

/**
 * A reference to a definition.
 */
public final class SimpleMetaborgReference extends MetaborgReference {

    @Nullable
    private final MetaborgDeclarationElement declaration;

    /**
     * Initializes a new instance of the {@link SimpleMetaborgReference} class.
     *
     * @param reference   The reference.
     * @param declaration The declaration; or <code>null</code>.
     */
    public SimpleMetaborgReference(
            final MetaborgReferenceElement reference,
            @Nullable final MetaborgDeclarationElement declaration) {
        super(reference, false);
        this.declaration = declaration;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MetaborgDeclarationElement resolve() {
        return this.declaration;
    }

}
