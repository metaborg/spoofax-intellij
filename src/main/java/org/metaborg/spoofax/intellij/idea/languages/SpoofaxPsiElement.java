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

package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Spoofax PSI element.
 */
public class SpoofaxPsiElement extends ASTWrapperPsiElement implements ISpoofaxPsiElement {

    /**
     * Initializes a new instance of the {@link SpoofaxPsiElement} class.
     *
     * @param node The node to which the element is attached.
     */
    public SpoofaxPsiElement(@NotNull final ASTNode node) {
        super(node);
    }

    /**
     * Accepts a PSI element visitor.
     *
     * @param visitor The visitor.
     */
    public void accept(@NotNull final PsiElementVisitor visitor) {
        if (visitor instanceof SpoofaxPsiVisitor)
            ((SpoofaxPsiVisitor) visitor).visitProperty(this);
        else super.accept(visitor);
    }

}
