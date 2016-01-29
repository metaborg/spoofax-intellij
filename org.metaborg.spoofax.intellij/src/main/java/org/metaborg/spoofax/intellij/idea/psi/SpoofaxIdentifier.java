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
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.tracing.IResolverService;
import org.metaborg.intellij.idea.psi.MetaborgDeclarationElement;
import org.metaborg.intellij.idea.psi.MetaborgIdentifierElement;
import org.metaborg.intellij.idea.psi.MetaborgReferenceElement;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * An identifier in a Spoofax language.
 */
public class SpoofaxIdentifier extends SpoofaxPsiElement implements PsiNamedElement, MetaborgReferenceElement, MetaborgDeclarationElement, MetaborgIdentifierElement {

    /**
     * Initializes a new instance of the {@link SpoofaxIdentifier} class.
     *
     * @param node The AST node to wrap.
     */
    @Inject
    public SpoofaxIdentifier(
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
