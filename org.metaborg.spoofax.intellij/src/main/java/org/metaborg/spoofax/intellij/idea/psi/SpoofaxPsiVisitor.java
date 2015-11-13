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

package org.metaborg.spoofax.intellij.idea.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Visitor for Spoofax PSI elements.
 */
public final class SpoofaxPsiVisitor extends PsiElementVisitor {

    /**
     * Visits a property.
     *
     * @param property The property.
     */
    public void visitProperty(@NotNull SpoofaxPsiElement property) {
        visitPsiElement(property);
    }

    /**
     * Visits a PSI element.
     *
     * @param element The element.
     */
    public void visitPsiElement(@NotNull PsiElement element) {
        visitElement(element);
    }

    /**
     * Visits a property.
     *
     * @param property The property.
     */
    public void visitProperty(@NotNull SpoofaxIdentifier property) {
        visitPsiElement(property);
    }

}
