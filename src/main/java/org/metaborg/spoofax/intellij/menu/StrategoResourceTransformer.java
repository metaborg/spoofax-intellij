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
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.processing.parse.IParseResultRequester;
import org.metaborg.core.source.ISourceTextService;
import org.metaborg.core.transform.ITransformer;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Transformer for Stratego resources.
 */
@Singleton
public final class StrategoResourceTransformer extends ResourceTransformer<IStrategoTerm, IStrategoTerm, IStrategoTerm> {

    @Inject
    private StrategoResourceTransformer(@NotNull
                                        final IContextService contextService,
                                        @NotNull
                                        final ISourceTextService sourceTextService,
                                        @NotNull
                                        final ILanguageIdentifierService identifierService,
                                        @NotNull
                                        final IParseResultRequester<IStrategoTerm> parseResultRequester,
                                        @NotNull
                                        final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester,
                                        @NotNull
                                        final ITransformer<IStrategoTerm, IStrategoTerm, IStrategoTerm> transformer) {
        super(contextService,
              sourceTextService,
              identifierService,
              parseResultRequester,
              analysisResultRequester,
              transformer);
    }
}
