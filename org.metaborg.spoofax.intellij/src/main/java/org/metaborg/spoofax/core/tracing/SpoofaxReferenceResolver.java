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

package org.metaborg.spoofax.core.tracing;

import com.google.common.collect.Lists;
import fj.P;
import fj.P2;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.StringFormatter;
import org.metaborg.core.language.FacetContribution;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.source.ISourceLocation;
import org.metaborg.core.source.ISourceRegion;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.core.tracing.IReferenceResolver;
import org.metaborg.core.tracing.IResolution;
import org.metaborg.core.tracing.IResolverService;
import org.metaborg.core.tracing.Resolution;
import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.metaborg.spoofax.core.stratego.IStrategoRuntimeService;
import org.metaborg.spoofax.core.terms.ITermFactoryService;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.strategoxt.HybridInterpreter;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Reference resolver for Spoofax languages.
 */
public final class SpoofaxReferenceResolver implements IReferenceResolver<IStrategoTerm> {

    private final ITermFactoryService termFactoryService;
    private final IStrategoRuntimeService strategoRuntimeService;
    private final ILanguageImpl language;
    private final ITermFactory termFactory;
    private final HybridInterpreter interpreter;
    private final String strategy;
    private final TracingCommon common;
    private final IStrategoCommon strategoCommon;
//    private final IResolverService<IStrategoTerm, IStrategoTerm> resolverService;

    public SpoofaxReferenceResolver(final ILanguageImpl language, final ITermFactoryService termFactoryService, final IStrategoRuntimeService strategoRuntimeService, final TracingCommon common, final IStrategoCommon strategoCommon) {
        this.language = language;
        this.termFactoryService = termFactoryService;
        this.strategoRuntimeService = strategoRuntimeService;
        this.common = common;
        this.strategoCommon = strategoCommon;

        final FacetContribution<ResolverFacet> facetContrib = language.facetContribution(ResolverFacet.class);
        assert facetContrib != null;
        final ResolverFacet facet = facetContrib.facet;
        this.strategy = facet.strategyName;
        this.termFactory = this.termFactoryService.get(facetContrib.contributor);
        this.interpreter = this.strategoRuntimeService.genericRuntime();
//        this.runtime = this.strategoRuntimeService.runtime(facetContrib.contributor, resource);
    }

//    public SpoofaxReferenceResolver(final IResolverService<IStrategoTerm, IStrategoTerm> resolverService) {
//        this.resolverService = resolverService;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReference(final IStrategoTerm term, final ParseResult<IStrategoTerm> result) throws MetaborgException {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public IResolution<IStrategoTerm> resolve(final IStrategoTerm term, final ParseResult<IStrategoTerm> result) throws MetaborgException {
        final IStrategoTerm ast = result.result;
        final IStrategoTerm builderTerm = this.termFactory.makeTuple(term, termFactory.makeTuple(), ast, null, null);
        final IStrategoTerm output = this.strategoCommon.invoke(this.interpreter, builderTerm, this.strategy);
        if (output == null)
            return null;

        final Collection<ISourceLocation> targets = Lists.newLinkedList();
        if(output.getTermType() == IStrategoTerm.LIST) {
            for(IStrategoTerm subterm : output) {
                final ISourceLocation targetLocation = common.getTargetLocation(subterm);
                if(targetLocation == null) {
//                    logger.debug("Cannot get target location for {}", subterm);
                    continue;
                }
                targets.add(targetLocation);
            }
        } else {
            final ISourceLocation targetLocation = common.getTargetLocation(output);
            if(targetLocation == null) {
//                logger.debug("Reference resolution failed, cannot get target location for {}", output);
                return null;
            }
            targets.add(targetLocation);
        }

        if(targets.isEmpty()) {
//            logger.debug("Reference resolution failed, cannot get target locations for {}", output);
            return null;
        }
        // TODO:
        return null;
//        return new Resolution(offsetRegion, targets);
    }

}
