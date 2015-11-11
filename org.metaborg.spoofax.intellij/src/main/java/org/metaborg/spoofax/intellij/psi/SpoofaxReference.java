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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.source.ISourceLocation;
import org.metaborg.core.source.ISourceRegion;
import org.metaborg.core.tracing.IResolverService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * A reference to a definition.
 */
public final class SpoofaxReference extends PsiReferenceBase<PsiElement> {

    private final ISourceRegion region;
    private final VirtualFile virtualFile;

    /**
     * Initializes a new instance of the {@link SpoofaxReference} class.
     *
     * @param element Some element.
     */
    public SpoofaxReference(final PsiElement element, final VirtualFile virtualFile, final ISourceRegion region) {
        super(element, false);
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

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
