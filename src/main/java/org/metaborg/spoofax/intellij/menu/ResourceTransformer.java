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
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.context.ContextException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.processing.parse.IParseResultRequester;
import org.metaborg.core.source.ISourceTextService;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.core.transform.ITransformer;
import org.metaborg.core.transform.ITransformerGoal;
import org.metaborg.core.transform.TransformerException;
import org.metaborg.spoofax.core.menu.TransformAction;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.util.concurrent.IClosableLock;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes a transformation action on resources.
 */
public class ResourceTransformer<P, A, T> implements IResourceTransformer {

    @NotNull
    private final IContextService contextService;
    @NotNull
    private final ISourceTextService sourceTextService;
    @NotNull
    private final ILanguageIdentifierService identifierService;
    @NotNull
    private final IParseResultRequester<P> parseResultRequester;
    @NotNull
    private final IAnalysisResultRequester<P, A> analysisResultRequester;
    @NotNull
    private final ITransformer<P, A, T> transformer;
    @InjectLogger
    private Logger logger;

    protected ResourceTransformer(
            @NotNull
            final IContextService contextService,
            @NotNull
            final ISourceTextService sourceTextService,
            @NotNull
            final ILanguageIdentifierService identifierService,
            @NotNull
            final IParseResultRequester<P> parseResultRequester,
            @NotNull
            final IAnalysisResultRequester<P, A> analysisResultRequester,
            @NotNull
            final ITransformer<P, A, T> transformer
    ) {
        this.contextService = contextService;
        this.sourceTextService = sourceTextService;
        this.identifierService = identifierService;
        this.parseResultRequester = parseResultRequester;
        this.analysisResultRequester = analysisResultRequester;
        this.transformer = transformer;
    }

    /**
     * Executes the specified action.
     *
     * @param action      The action to execute.
     * @param language    The language implementation.
     * @param activeFiles The active files.
     */
    @Override
    public boolean execute(@NotNull final TransformAction action,
                           @NotNull final ILanguageImpl language,
                           @NotNull final List<FileObject> activeFiles) throws
            MetaborgException {

        List<TransformResource> resources = new ArrayList<>();
        for (FileObject file : activeFiles) {
            if (!this.identifierService.identify(file, language))
                continue;
            try {
                final String text = this.sourceTextService.text(file);
                resources.add(new TransformResource(file, text));
            } catch (IOException e) {
                this.logger.error("Cannot transform {}; skipped. Exception while retrieving text: {}", file, e);
                continue;
            }
        }
        for (TransformResource resource : resources) {
            try {
                transform(resource, action.goal, language, action.flags.parsed);
            } catch (ContextException | TransformerException e) {
                this.logger.error("Transformation failed for {}: {}", resource.resource(), e);
                // TODO: Display error!
                return false;
            }
        }

        return true;
    }

    /**
     * Transforms the specified resource.
     *
     * @param resource The resource.
     * @param goal     The goal.
     * @param language The language.
     * @param parsed   Whether this transformation action has the <em>parsed</em> flag.
     * @throws ContextException
     * @throws TransformerException
     */
    private void transform(@NotNull final TransformResource resource,
                           @NotNull final ITransformerGoal goal,
                           @NotNull final ILanguageImpl language,
                           boolean parsed) throws ContextException,
            TransformerException {
        final IContext context = this.contextService.get(resource.resource(), language);
        if (parsed) {
            final ParseResult<P> result = this.parseResultRequester.request(resource.resource(),
                                                                            language,
                                                                            resource.text()).toBlocking().single();
            this.transformer.transform(result, context, goal);
        } else {
            final AnalysisFileResult<P, A> result = this.analysisResultRequester.request(resource.resource(),
                                                                                         context,
                                                                                         resource.text()).toBlocking().single();
            try (IClosableLock lock = context.read()) {
                transformer.transform(result, context, goal);
            }
        }
    }
}
