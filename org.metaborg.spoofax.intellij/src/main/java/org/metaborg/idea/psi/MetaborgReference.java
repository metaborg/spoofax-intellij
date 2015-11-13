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

package org.metaborg.idea.psi;

import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A reference to a definition.
 */
public abstract class MetaborgReference extends PsiReferenceBase<MetaborgReferenceElement> {

    /**
     * Initializes a new instance of the {@link MetaborgReference} class.
     *
     * @param reference The reference.
     * @param isSoft Whether the reference is a soft reference.
     */
    public MetaborgReference(final MetaborgReferenceElement reference, boolean isSoft) {
        super(reference, isSoft);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public abstract MetaborgDeclarationElement resolve();

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final Object[] getVariants() {
        return new Object[0];
    }

}
