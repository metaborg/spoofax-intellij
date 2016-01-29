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

package org.metaborg.intellij.idea.psi;

import org.jetbrains.annotations.Nullable;

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
