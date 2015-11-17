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

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.source.ISourceRegion;
import org.metaborg.idea.psi.MetaborgDeclarationElement;
import org.metaborg.idea.psi.MetaborgReference;
import org.metaborg.idea.psi.MetaborgReferenceElement;

/**
 * A reference to a definition.
 */
public final class SpoofaxReference extends MetaborgReference {

    private final ISourceRegion region;
    private final VirtualFile virtualFile;

    /**
     * Initializes a new instance of the {@link SpoofaxReference} class.
     *
     * @param reference The reference.
     */
    public SpoofaxReference(final MetaborgReferenceElement reference, final VirtualFile virtualFile, final ISourceRegion region) {
        super(reference, false);
        // new TextRange(0, element.getTextLength())
        this.region = region;
        this.virtualFile = virtualFile;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        PsiFile file = this.getElement().getManager().findFile(this.virtualFile);
        if (file == null)
            return null;
        return file.findElementAt(this.region.startOffset());
    }


}
