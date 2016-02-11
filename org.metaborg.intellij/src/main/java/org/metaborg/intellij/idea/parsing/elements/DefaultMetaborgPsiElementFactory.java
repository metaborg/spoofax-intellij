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
import com.intellij.lang.*;
import com.intellij.psi.tree.*;
import org.metaborg.core.processing.analyze.*;
import org.metaborg.core.tracing.*;
import org.metaborg.intellij.resources.*;
import org.spoofax.interpreter.terms.*;

/**
 * Default implementation of the {@link IMetaborgPsiElementFactory} interface.
 */
public final class DefaultMetaborgPsiElementFactory implements IMetaborgPsiElementFactory {

    private final IResolverService<IStrategoTerm, IStrategoTerm> resolverService;
    private final IIntelliJResourceService resourceService;
    private final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester;

    /**
     * Initializes a new instance of the {@link DefaultMetaborgPsiElementFactory} class.
     */
    @Inject
    public DefaultMetaborgPsiElementFactory(
            final IResolverService<IStrategoTerm, IStrategoTerm> resolverService,
            final IIntelliJResourceService resourceService,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester) {
        this.resolverService = resolverService;
        this.resourceService = resourceService;
        this.analysisResultRequester = analysisResultRequester;
    }

    @Override
    public MetaborgPsiElement create(final ASTNode node) {
        final IElementType type = node.getElementType();
        if (type instanceof MetaborgIdentifierElementType) {
            return new MetaborgIdentifier(
                    node,
                    this.resolverService,
                    this.resourceService,
                    this.analysisResultRequester
            );
        } else if (type instanceof SpoofaxElementType) {
            return new MetaborgPsiElement(
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
