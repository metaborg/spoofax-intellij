/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.idea.parsing.elements;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.extapi.psi.*;
import com.intellij.lang.*;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.*;
import org.metaborg.core.processing.analyze.*;
import org.metaborg.core.tracing.*;
import org.metaborg.intellij.resources.*;
import org.spoofax.interpreter.terms.*;

/**
 * A Spoofax PSI element.
 * <p>
 * The Program Structure Interface (PSI) is a hierarchy of PSI elements
 * that represent the files and their structure (e.g. AST).
 */
public class MetaborgPsiElement extends ASTWrapperPsiElement implements PsiElement {

    private final IResolverService<IStrategoTerm, IStrategoTerm> resolverService;
    private final IIntelliJResourceService resourceService;
    private final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester;

    /**
     * Initializes a new instance of the {@link MetaborgPsiElement} class.
     *
     * @param node The AST node to wrap.
     */
    @Inject
    public MetaborgPsiElement(
            @Assisted final ASTNode node,
            final IResolverService<IStrategoTerm, IStrategoTerm> resolverService,
            final IIntelliJResourceService resourceService,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester) {
        super(node);
        this.resolverService = resolverService;
        this.resourceService = resourceService;
        this.analysisResultRequester = analysisResultRequester;
    }

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
    public void accept(final PsiElementVisitor visitor) {
        if (visitor instanceof MetaborgPsiVisitor)
            ((MetaborgPsiVisitor)visitor).visitProperty(this);
        else super.accept(visitor);
    }

}
