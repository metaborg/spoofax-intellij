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

package org.metaborg.spoofax.intellij.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.processing.parse.IParseResultRequester;
import org.metaborg.core.transform.ITransformService;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Transformer for Stratego resources.
 */
@Singleton
public final class StrategoResourceTransformer extends ResourceTransformer<IStrategoTerm, IStrategoTerm, IStrategoTerm> {

    @Inject
    /* package private */ StrategoResourceTransformer(
            final IContextService contextService,
            final IParseResultRequester<IStrategoTerm> parseResultRequester,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester,
            final ITransformService<IStrategoTerm, IStrategoTerm, IStrategoTerm> transformService
    ) {
        super(
                contextService,
                parseResultRequester,
                analysisResultRequester,
                transformService
        );
    }
}
