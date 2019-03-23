/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.metaborg.intellij.idea.transformations;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.ITransformAction;
import org.metaborg.core.action.ITransformGoal;
import org.metaborg.core.analysis.IAnalyzeUnit;
import org.metaborg.core.context.ContextException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.processing.parse.IParseResultRequester;
import org.metaborg.core.project.IProject;
import org.metaborg.core.syntax.IInputUnit;
import org.metaborg.core.syntax.IParseUnit;
import org.metaborg.core.transform.*;
import org.metaborg.core.unit.IInputUnitService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.concurrent.IClosableLock;
import org.metaborg.util.log.ILogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.*;

/**
 * Executes a transformation action on resources.
 */
public final class ResourceTransformer<I extends IInputUnit, P extends IParseUnit, A extends IAnalyzeUnit, T extends ITransformUnit<?>, TUP extends ITransformUnit<P>, TUA extends ITransformUnit<A>, TA extends ITransformAction>
    implements IResourceTransformer {
    private final IContextService contextService;
    private final IInputUnitService<I> unitService;
    private final IParseResultRequester<I, P> parseResultRequester;
    private final IAnalysisResultRequester<I, A> analysisResultRequester;
    private final ITransformService<P, A, TUP, TUA, TA> transformService;
    @InjectLogger
    private ILogger logger;


    /**
     * Initializes a new instance of the {@link ResourceTransformer} class.
     */
    @Inject
    public ResourceTransformer(IContextService contextService, IInputUnitService<I> unitService,
                               IParseResultRequester<I, P> parseResultRequester, IAnalysisResultRequester<I, A> analysisResultRequester,
                               ITransformService<P, A, TUP, TUA, TA> transformService) {
        this.contextService = contextService;
        this.unitService = unitService;
        this.parseResultRequester = parseResultRequester;
        this.analysisResultRequester = analysisResultRequester;
        this.transformService = transformService;
    }


    /**
     * Executes the specified action.
     *
     * @param language
     *            The language implementation.
     * @param resources
     *            The active resources.
     * @param goal
     *            The transformation goal.
     */
    @Override public List<FileObject> execute(final Iterable<TransformResource> resources, final ILanguageImpl language,
                                              final ITransformGoal goal) throws MetaborgException {

        final List<FileObject> outputFiles = new ArrayList<>();
        for(final TransformResource transformResource : resources) {
            final FileObject resource = transformResource.resource();
            try {
                this.logger.info("Transforming {}", resource);
                final Collection<T> results =
                    transform(resource, transformResource.project(), language, transformResource.text(), goal);
                for(final T r : results) {
                    for (final ITransformOutput o : r.outputs()) {
                        @Nullable final FileObject output = o.output();
                        if (output != null)
                            outputFiles.add(output);
                    }
                }
            } catch(ContextException | TransformException e) {
                this.logger.error("Transformation failed for {}", e, resource);
            }
        }
        return outputFiles;
    }

    /**
     * Transforms a resource.
     *
     * @param resource
     *            The resource.
     * @param project
     *            The project that contains the resource.
     * @param language
     *            The language of the resource.
     * @param text
     *            The contents of the resource.
     * @param goal
     *            The transformation goal.
     * @return The transformation results.
     * @throws ContextException
     * @throws TransformException
     */
    private Collection<T> transform(final FileObject resource, final IProject project, final ILanguageImpl language,
                                    final String text, final ITransformGoal goal) throws ContextException, TransformException {
        final IContext context = this.contextService.get(resource, project, language);
        final I input = unitService.inputUnit(resource, text, language, null);
        final Collection<T> results = Lists.newArrayList();
        if(this.transformService.requiresAnalysis(context, goal)) {
            for(TUA result : transformAnalysis(input, context, goal)) {
                @SuppressWarnings("unchecked") final T genericResult = (T) result;
                results.add(genericResult);
            }
        } else {
            for(TUP result : transformParse(input, context, goal)) {
                @SuppressWarnings("unchecked") final T genericResult = (T) result;
                results.add(genericResult);
            }
        }
        return results;
    }

    /**
     * Transform a resource from its parse result.
     *
     * @param resource
     *            The resource.
     * @param language
     *            The language of the resource.
     * @param text
     *            The contents of the resource.
     * @param goal
     *            The transformation goal.
     * @param context
     *            The context.
     * @return The transformation results.
     * @throws TransformException
     */
    private Collection<TUP> transformParse(I input, IContext context, ITransformGoal goal) throws TransformException {
        final P parseResult = parseResultRequester.request(input).toBlocking().single();
        return transformService.transform(parseResult, context, goal);
    }

    /**
     * Transform a resource from its analysis result.
     *
     * @param resource
     *            The resource.
     * @param text
     *            The contents of the resource.
     * @param goal
     *            The transformation goal.
     * @param context
     *            The context.
     * @return The transformation results.
     * @throws TransformException
     */
    private Collection<TUA> transformAnalysis(I input, IContext context, ITransformGoal goal) throws TransformException {
        final A analysisResult = analysisResultRequester.request(input, context).toBlocking().single();
        // noinspection unused
        try(IClosableLock lock = context.read()) {
            return transformService.transform(analysisResult, context, goal);
        }
    }
}
