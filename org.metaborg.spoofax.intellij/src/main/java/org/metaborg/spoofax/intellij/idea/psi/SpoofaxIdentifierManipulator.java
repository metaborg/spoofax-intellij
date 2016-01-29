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

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public final class SpoofaxIdentifierManipulator extends AbstractElementManipulator<SpoofaxIdentifier> {
    @Override
    public SpoofaxIdentifier handleContentChange(
            final SpoofaxIdentifier element, final TextRange range, final String newContent) throws
            IncorrectOperationException {

        final String oldText = element.getText();
        final String newText = oldText.substring(
                0,
                range.getStartOffset()
        ) + newContent + oldText.substring(range.getEndOffset());
        final PsiElement child = element.getFirstChild();
        if (child instanceof LeafPsiElement) {
            ((LeafPsiElement)child).replaceWithText(newText);
            return element;
        }
        throw new IncorrectOperationException("Bad PSI.");
    }
}
