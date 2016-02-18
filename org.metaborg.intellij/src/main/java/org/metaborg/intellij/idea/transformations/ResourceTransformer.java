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

package org.metaborg.intellij.idea.transformations;

import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.*;
import org.metaborg.core.action.*;
import org.metaborg.core.analysis.*;
import org.metaborg.core.context.*;
import org.metaborg.core.language.*;
import org.metaborg.core.processing.analyze.*;
import org.metaborg.core.processing.parse.*;
import org.metaborg.core.syntax.*;
import org.metaborg.core.transform.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.meta.core.project.*;
import org.metaborg.util.concurrent.*;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * Executes a transformation action on resources.
 */
public final class ResourceTransformer<P, A, T> implements IResourceTransformer {

    private final IContextService contextService;
    private final IParseResultRequester<P> parseResultRequester;
    private final IAnalysisResultRequester<P, A> analysisResultRequester;
    private final ITransformService<P, A, T> transformService;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link ResourceTransformer} class.
     */
    @Inject
    public ResourceTransformer(
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
    public List<FileObject> execute(
            final Iterable<TransformResource> resources, final ILanguageImpl language,
            final ITransformGoal goal) throws MetaborgException {

        final List<FileObject> outputFiles = new ArrayList<>();
        for (final TransformResource transformResource : resources) {
            final FileObject resource = transformResource.resource();
            try {
                this.logger.info("Transforming {}", resource);
                final TransformResults<?, T> results = transform(
                        resource,
                        transformResource.project(),
                        language,
                        transformResource.text(),
                        goal
                );
                for (final TransformResult<?, T> r : results.results) {
                    outputFiles.add(r.output);
                }
            } catch (ContextException | TransformException e) {
                this.logger.error("Transformation failed for {}", e, resource);
            }
        }
        return outputFiles;
    }

    /**
     * Transforms a resource.
     *
     * @param resource The resource.
     * @param project The project that contains the resource.
     * @param language The language of the resource.
     * @param text The contents of the resource.
     * @param goal The transformation goal.
     * @return The transformation results.
     * @throws ContextException
     * @throws TransformException
     */
    private TransformResults<?, T> transform(
            final FileObject resource,
            final ILanguageSpec project,
            final ILanguageImpl language,
            final String text,
            final ITransformGoal goal)
            throws ContextException, TransformException {
        final IContext context = this.contextService.get(resource, project, language);
        final TransformResults<?, T> results;
        if (this.transformService.requiresAnalysis(context, goal)) {
            results = transformAnalysis(resource, text, goal, context);
        } else {
            results = transformParse(resource, language, text, goal, context);
        }
        return results;
    }

    /**
     * Transform a resource from its parse result.
     *
     * @param resource The resource.
     * @param language The language of the resource.
     * @param text The contents of the resource.
     * @param goal The transformation goal.
     * @param context The context.
     * @return The transformation results.
     * @throws TransformException
     */
    private TransformResults<P, T> transformParse(
            final FileObject resource,
            final ILanguageImpl language,
            final String text,
            final ITransformGoal goal, final IContext context) throws TransformException {
        final ParseResult<P> parseResult =
                this.parseResultRequester.request(resource, language, text).toBlocking().single();
        return this.transformService.transform(parseResult, context, goal);
    }

    /**
     * Transform a resource from its analysis result.
     *
     * @param resource The resource.
     * @param text The contents of the resource.
     * @param goal The transformation goal.
     * @param context The context.
     * @return The transformation results.
     * @throws TransformException
     */
    private TransformResults<A, T> transformAnalysis(
            final FileObject resource,
            final String text,
            final ITransformGoal goal,
            final IContext context) throws TransformException {
        final AnalysisFileResult<P, A> analysisResult =
                this.analysisResultRequester.request(resource, context, text).toBlocking().single();
        //noinspection unused
        try (IClosableLock lock = context.read()) {
            return this.transformService.transform(analysisResult, context, goal);
        }
    }
}
