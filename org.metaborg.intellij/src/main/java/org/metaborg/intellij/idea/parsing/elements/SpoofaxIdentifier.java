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

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.lang.*;
import com.intellij.psi.*;
import com.intellij.util.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.processing.analyze.*;
import org.metaborg.core.tracing.*;
import org.metaborg.intellij.idea.parsing.references.*;
import org.metaborg.intellij.resources.*;
import org.spoofax.interpreter.terms.*;

/**
 * An identifier in a Metaborg language.
 */
public class SpoofaxIdentifier extends MetaborgPsiElement
        implements PsiNamedElement, MetaborgReferenceElement, MetaborgDeclarationElement, MetaborgIdentifierElement {

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
