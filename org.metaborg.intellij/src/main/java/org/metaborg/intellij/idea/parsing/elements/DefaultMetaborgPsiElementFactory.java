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

import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.core.processing.analyze.ISpoofaxAnalysisResultRequester;
import org.metaborg.spoofax.core.tracing.ISpoofaxResolverService;

import com.google.inject.Inject;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;

/**
 * Default implementation of the {@link IMetaborgPsiElementFactory} interface.
 */
public final class DefaultMetaborgPsiElementFactory implements IMetaborgPsiElementFactory {

    private final ISpoofaxResolverService resolverService;
    private final IIntelliJResourceService resourceService;
    private final ISpoofaxAnalysisResultRequester analysisResultRequester;

    /**
     * Initializes a new instance of the {@link DefaultMetaborgPsiElementFactory} class.
     */
    @Inject
    public DefaultMetaborgPsiElementFactory(
            final ISpoofaxResolverService resolverService,
            final IIntelliJResourceService resourceService,
            final ISpoofaxAnalysisResultRequester analysisResultRequester) {
        this.resolverService = resolverService;
        this.resourceService = resourceService;
        this.analysisResultRequester = analysisResultRequester;
    }

    @Override
    public MetaborgPsiElement create(final ASTNode node) {
        final IElementType type = node.getElementType();
        if (type instanceof MetaborgIdentifierElementType) {
            return new SpoofaxIdentifier(
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
