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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.tracing.IResolverService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * A Spoofax PSI element.
 * <p>
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
    public SpoofaxPsiElement(
            @Assisted final ASTNode node,
            final IResolverService<IStrategoTerm, IStrategoTerm> resolverService,
            final IIntelliJResourceService resourceService,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester) {
        super(node);
        this.resolverService = resolverService;
        this.resourceService = resourceService;
        this.analysisResultRequester = analysisResultRequester;
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }


    @Override
    public PsiReference getReference() {
        final PsiReference[] references = getReferences();
        return references.length == 0 ? null : references[0];
    }

    /**
     * Accepts a PSI element visitor.
     *
     * @param visitor The visitor.
     */
    @Override
    public void accept(@NotNull final PsiElementVisitor visitor) {
        if (visitor instanceof SpoofaxPsiVisitor)
            ((SpoofaxPsiVisitor)visitor).visitProperty(this);
        else super.accept(visitor);
    }

}
