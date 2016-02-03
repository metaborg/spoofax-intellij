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
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.tracing.IResolverService;
import org.metaborg.spoofax.intellij.factories.ISpoofaxPsiElementFactory;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

public final class SpoofaxPsiElementFactory implements ISpoofaxPsiElementFactory {

    private final IResolverService<IStrategoTerm, IStrategoTerm> resolverService;
    private final IIntelliJResourceService resourceService;
    private final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester;

    /**
     * Initializes a new instance of the {@link SpoofaxPsiElementFactory} class.
     */
    @Inject
    public SpoofaxPsiElementFactory(
            final IResolverService<IStrategoTerm, IStrategoTerm> resolverService,
            final IIntelliJResourceService resourceService,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester) {
        this.resolverService = resolverService;
        this.resourceService = resourceService;
        this.analysisResultRequester = analysisResultRequester;
    }

    @Override
    public SpoofaxPsiElement create(final ASTNode node) {
        final IElementType type = node.getElementType();
        if (type instanceof SpoofaxIdentifierType) {
            return new SpoofaxIdentifier(
                    node,
                    this.resolverService,
                    this.resourceService,
                    this.analysisResultRequester
            );
        } else if (type instanceof SpoofaxElementType) {
            return new SpoofaxPsiElement(
                    node,
                    this.resolverService,
                    this.resourceService,
                    this.analysisResultRequester
            );
        } else {
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}
