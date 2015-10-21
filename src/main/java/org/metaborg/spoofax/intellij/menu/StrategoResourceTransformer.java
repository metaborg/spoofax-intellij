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
