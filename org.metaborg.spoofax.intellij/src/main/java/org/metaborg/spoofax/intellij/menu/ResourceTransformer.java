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

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.ITransformGoal;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.context.ContextException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.processing.parse.IParseResultRequester;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.core.transform.ITransformService;
import org.metaborg.core.transform.TransformException;
import org.metaborg.util.concurrent.IClosableLock;
import org.metaborg.util.log.ILogger;

//import org.metaborg.core.language.ILanguageIdentifierService;
//import org.metaborg.core.source.ISourceTextService;
//import org.metaborg.core.transform.ITransformer;
//import org.metaborg.core.transform.ITransformerGoal;
//import org.metaborg.core.transform.TransformerException;
//import org.metaborg.spoofax.core.menu.TransformAction;

//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

/**
 * Executes a transformation action on resources.
 */
public class ResourceTransformer<P, A, T> implements IResourceTransformer {

    @NotNull
    private final IContextService contextService;
    @NotNull
    private final IParseResultRequester<P> parseResultRequester;
    @NotNull
    private final IAnalysisResultRequester<P, A> analysisResultRequester;
    @NotNull
    private final ITransformService<P, A, T> transformService;
    @InjectLogger
    private ILogger logger;

    protected ResourceTransformer(
            final IContextService contextService,
            final IParseResultRequester<P> parseResultRequester,
            final IAnalysisResultRequester<P, A> analysisResultRequester,
            final ITransformService<P, A, T> transformService
    ) {
        this.contextService = contextService;
        this.parseResultRequester = parseResultRequester;
        this.analysisResultRequester = analysisResultRequester;
        this.transformService = transformService;
    }

    /**
     * Executes the specified action.
     *
     * @param language  The language implementation.
     * @param resources The active resources.
     * @param goal      The transformation goal.
     */
    @Override
    public boolean execute(
            final Iterable<TransformResource> resources, final ILanguageImpl language,
            final ITransformGoal goal) throws MetaborgException {

        for (final TransformResource transformResource : resources) {
            final FileObject resource = transformResource.resource();
            try {
                transform(resource, language, transformResource.text(), goal);
            } catch (ContextException | TransformException e) {
                this.logger.error("Transformation failed for {}", e, resource);
            }
        }

        return true;
    }

    private void transform(
            final FileObject resource,
            final ILanguageImpl language,
            final String text,
            final ITransformGoal goal)
            throws ContextException, TransformException {
        final IContext context = this.contextService.get(resource, language);
        if (this.transformService.requiresAnalysis(context, goal)) {
            final AnalysisFileResult<P, A> analysisResult =
                    this.analysisResultRequester.request(resource, context, text).toBlocking().single();
            try (IClosableLock lock = context.read()) {
                this.transformService.transform(analysisResult, context, goal);
            }
        } else {
            final ParseResult<P> parseResult =
                    this.parseResultRequester.request(resource, language, text).toBlocking().single();
            this.transformService.transform(parseResult, context, goal);
        }
    }
}
