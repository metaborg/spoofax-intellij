/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.parsing.elements;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.core.processing.analyze.ISpoofaxAnalysisResultRequester;
import org.metaborg.spoofax.core.tracing.ISpoofaxResolverService;

/**
 * A Spoofax PSI element.
 * <p>
 * The Program Structure Interface (PSI) is a hierarchy of PSI elements
 * that represent the files and their structure (e.g. AST).
 */
public class MetaborgPsiElement extends ASTWrapperPsiElement implements PsiElement {

    private final ISpoofaxResolverService resolverService;
    private final IIntelliJResourceService resourceService;
    private final ISpoofaxAnalysisResultRequester analysisResultRequester;

    /**
     * Initializes a new instance of the {@link MetaborgPsiElement} class.
     *
     * @param node The AST node to wrap.
     */
    @Inject
    public MetaborgPsiElement(
            @Assisted final ASTNode node,
            final ISpoofaxResolverService resolverService,
            final IIntelliJResourceService resourceService,
            final ISpoofaxAnalysisResultRequester analysisResultRequester) {
        super(node);
        assert node != null;
        assert resolverService != null;
        assert resourceService != null;
        assert analysisResultRequester != null;
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
