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

package org.metaborg.intellij.idea.actions;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.*;
import org.metaborg.core.action.*;
import org.metaborg.core.analysis.*;
import org.metaborg.core.context.*;
import org.metaborg.core.language.*;
import org.metaborg.core.processing.analyze.*;
import org.metaborg.core.processing.parse.*;
import org.metaborg.core.project.*;
import org.metaborg.core.syntax.*;
import org.metaborg.core.transform.*;
import org.metaborg.intellij.idea.transformations.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.concurrent.*;
import org.metaborg.util.log.*;
import org.spoofax.interpreter.terms.*;

import javax.annotation.*;
import java.util.*;

/**
 * A transformation action from a builder menu.
 */
public final class TransformationAction extends AnActionWithId {

    private final ITransformGoal goal;
    private final ILanguageImpl language;
    private final ActionUtils actionUtils;
    private final IIntelliJResourceService resourceService;
    private final IResourceTransformer transformer;
    private final IContextService contextService;
    private final ITransformService<IStrategoTerm, IStrategoTerm, IStrategoTerm> transformService;
    private final IParseResultRequester<IStrategoTerm> parseResultRequester;
    private final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester;
    @InjectLogger
    private ILogger logger;

    @Inject
    private TransformationAction(
            @Assisted final String id,
            @Assisted final ITransformAction action,
            @Assisted final ILanguageImpl language,
            final ActionUtils actionUtils,
            final IResourceTransformer transformer,
            final IIntelliJResourceService resourceService,
            final IContextService contextService,
            final ITransformService<IStrategoTerm, IStrategoTerm, IStrategoTerm> transformService,
            final IParseResultRequester<IStrategoTerm> parseResultRequester,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester) {
        super(id, action.name(), null, null);
        this.language = language;
        this.goal = action.goal();
        this.actionUtils = actionUtils;
        this.transformer = transformer;
        this.resourceService = resourceService;
        this.contextService = contextService;
        this.transformService = transformService;
        this.parseResultRequester = parseResultRequester;
        this.analysisResultRequester = analysisResultRequester;
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        final List<TransformResource> resources = this.actionUtils.getActiveResources(e);

        this.logger.debug("Transforming active resources: {}", resources);

        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                final List<FileObject> outputFiles = transformAll(resources);
//                final List<FileObject> outputFiles = this.transformer.execute(resources, this.language, this.goal);
                this.logger.debug("Transformation resulted in output files: {}", outputFiles);
                for (final FileObject output : outputFiles) {
                    @Nullable final VirtualFile virtualFile = this.resourceService.unresolve(output);
                    if (virtualFile != null) {
                        virtualFile.refresh(true, false);
                        this.logger.debug("Refreshed output file: {}", virtualFile);
                    }
                }
            } catch (final MetaborgException ex) {
                this.logger.error("An exception occurred: {}", ex);
            }
        });

        this.logger.info("Transformed active resources: {}", resources);
    }

    private List<FileObject> transformAll(final List<TransformResource> resources)
            throws ContextException, TransformException {

        final List<FileObject> outputFiles = new ArrayList<>(resources.size());

        for(final TransformResource transformResource : resources) {
            final FileObject resource = transformResource.resource();
            final TransformResults<IStrategoTerm, IStrategoTerm> transformResults =
                transform(resource, transformResource.project(), this.language, transformResource.text());
            for (final TransformResult<IStrategoTerm, IStrategoTerm> result : transformResults.results) {
                outputFiles.add(result.output);
            }
        }

        return outputFiles;
    }

    private TransformResults<IStrategoTerm, IStrategoTerm> transform(final FileObject resource,
                                                                     final IProject project,
                                                                     final ILanguageImpl language,
                                                                     final String text)
            throws ContextException, TransformException {
        final TransformResults<IStrategoTerm, IStrategoTerm> transformResults;
        final IContext context = this.contextService.get(resource, project, language);
        if(this.transformService.requiresAnalysis(context, this.goal)) {
            this.logger.debug("Requesting analysis result.");
            final AnalysisFileResult<IStrategoTerm, IStrategoTerm> result =
                    this.analysisResultRequester.request(
                            resource,
                            context,
                            text)
                            .toBlocking().single();
            this.logger.debug("Requesting context read lock.");
            try(IClosableLock lock = context.read()) {
                this.logger.debug("Transforming: {}", resource);
                transformResults = this.transformService.transform(result, context, this.goal);
                this.logger.info("Transformed: {}", resource);
            }
        } else {
            this.logger.debug("Requesting parse result.");
            final ParseResult<IStrategoTerm> result = this.parseResultRequester.request(
                    resource,
                    language,
                    text)
                    .toBlocking().single();
            this.logger.debug("Transforming: {}", resource);
            transformResults = this.transformService.transform(result, context, this.goal);
            this.logger.info("Transformed: {}", resource);
        }
        return transformResults;
    }

}