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
import com.intellij.lang.*;
import com.intellij.psi.*;
import com.intellij.util.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.processing.analyze.*;
import org.metaborg.core.tracing.*;
import org.metaborg.intellij.resources.*;
import org.spoofax.interpreter.terms.*;

/**
 * An identifier in a Metaborg language.
 */
public class MetaborgIdentifier extends MetaborgPsiElement implements PsiNamedElement {   // TODO:, MetaborgReferenceElement, MetaborgDeclarationElement, MetaborgIdentifierElement {

    /**
     * Initializes a new instance of the {@link MetaborgIdentifier} class.
     *
     * @param node The AST node to wrap.
     */
    @Inject
    public MetaborgIdentifier(
            @Assisted final ASTNode node,
            final IResolverService<IStrategoTerm, IStrategoTerm> resolverService,
            final IIntelliJResourceService resourceService,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester) {
        super(node, resolverService, resourceService, analysisResultRequester);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PsiElement setName(@NonNls final String name) throws IncorrectOperationException {
        // TODO: Rename refactoring
        throw new RuntimeException("Not implemented.");
        // See also:
        // http://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/rename_refactoring.html
        // https://github.com/JetBrains/intellij-community/blob/master/plugins/properties/properties-psi-impl/src/com/intellij/lang/properties/psi/impl/PropertyImpl.java#L58
    }
}
