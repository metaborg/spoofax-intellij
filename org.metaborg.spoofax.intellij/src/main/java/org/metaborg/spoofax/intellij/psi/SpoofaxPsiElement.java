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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.source.ISourceLocation;
import org.metaborg.core.tracing.IResolverService;
import org.metaborg.core.tracing.Resolution;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import java.util.ArrayList;
import java.util.List;

/**
 * A Spoofax PSI element.
 *
 * The Program Structure Interface (PSI) is a hierarchy of PSI elements
 * that represent the files and their structure (e.g. AST).
 */
public class SpoofaxPsiElement extends ASTWrapperPsiElement implements PsiElement {

    private final IResolverService<IStrategoTerm, IStrategoTerm> resolverService;
    private final IIntelliJResourceService resourceService;
    private final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester;

    /**
     * Initializes a new instance of the {@link SpoofaxPsiElement} class.
     *
     * @param node The AST node to wrap.
     */
    @Inject
    public SpoofaxPsiElement(@Assisted final ASTNode node, final IResolverService<IStrategoTerm, IStrategoTerm> resolverService, final IIntelliJResourceService resourceService, final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester) {
        super(node);
        this.resolverService = resolverService;
        this.resourceService = resourceService;
        this.analysisResultRequester = analysisResultRequester;
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

    @Override
    public PsiReference getReference() {
        PsiReference[] references = getReferences();
        return references.length == 0 ? null : references[0];
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        PsiFile containingFile = this.getContainingFile();
        if (containingFile == null)
            return new PsiReference[0];
        VirtualFile virtualFile = containingFile.getVirtualFile();
        if (virtualFile == null)
            return new PsiReference[0];
        FileObject resource = this.resourceService.resolve(virtualFile);
        AnalysisFileResult<IStrategoTerm, IStrategoTerm> analysisFileResult = this.analysisResultRequester.get(
                resource);
        if (analysisFileResult == null)
            return new PsiReference[0];

        List<SpoofaxReference> references = new ArrayList<>();
        try {
            Resolution resolution = resolverService.resolve(this.getTextOffset(), analysisFileResult);
            if (resolution != null) {
                for (ISourceLocation location : resolution.targets) {
                    FileObject targetResource = location.resource();
                    if (targetResource == null)
                        continue;
                    VirtualFile targetVirtualFile = this.resourceService.unresolve(targetResource);
                    SpoofaxReference reference = new SpoofaxReference(this, targetVirtualFile, location.region());
                    references.add(reference);
                }
            }
        } catch (MetaborgException e) {
            throw new RuntimeException(e);
        }

        return references.toArray(new SpoofaxReference[references.size()]);
    }

}
