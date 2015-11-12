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

package org.metaborg.spoofax.intellij.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A reference to a Spoofax identifier element.
 */
public final class SpoofaxIdentifierReference extends PsiReferenceBase<PsiElement> {

    public SpoofaxIdentifierReference(@NotNull final PsiElement element) {
        super(element);
    }

    /**
     * {@inheritDoc}
     *
     * This method is called upon <em>Go To declaration</em>.
     */
    @Nullable
    @Override
    public PsiElement resolve() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Object[] getVariants() {
        // TODO: For code completion?
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }
}
