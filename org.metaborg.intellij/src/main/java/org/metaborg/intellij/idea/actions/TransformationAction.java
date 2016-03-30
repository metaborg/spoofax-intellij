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

package org.metaborg.intellij.idea.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.ITransformAction;
import org.metaborg.core.action.ITransformGoal;
import org.metaborg.core.context.ContextException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.transform.ITransformUnit;
import org.metaborg.core.transform.TransformException;
import org.metaborg.intellij.idea.transformations.TransformResource;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.core.processing.analyze.ISpoofaxAnalysisResultRequester;
import org.metaborg.spoofax.core.processing.parse.ISpoofaxParseResultRequester;
import org.metaborg.spoofax.core.transform.ISpoofaxTransformService;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnitService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;
import org.metaborg.util.concurrent.IClosableLock;
import org.metaborg.util.log.ILogger;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * A transformation action from a builder menu.
 */
public final class TransformationAction extends AnActionWithId {
    private final ITransformGoal goal;
    private final ILanguageImpl language;
    private final ActionUtils actionUtils;
    private final IIntelliJResourceService resourceService;
    private final IContextService contextService;
    private final ISpoofaxInputUnitService unitService;
    private final ISpoofaxTransformService transformService;
    private final ISpoofaxParseResultRequester parseResultRequester;
    private final ISpoofaxAnalysisResultRequester analysisResultRequester;
    @InjectLogger private ILogger logger;


    @Inject private TransformationAction(@Assisted final String id, @Assisted final ITransformAction action,
        @Assisted final ILanguageImpl language, final ActionUtils actionUtils,
        final IIntelliJResourceService resourceService, final IContextService contextService,
        final ISpoofaxInputUnitService unitService, final ISpoofaxTransformService transformService,
        final ISpoofaxParseResultRequester parseResultRequester,
        final ISpoofaxAnalysisResultRequester analysisResultRequester) {
        super(id, action.name(), null, null);
        this.language = language;
        this.goal = action.goal();
        this.actionUtils = actionUtils;
        this.resourceService = resourceService;
        this.contextService = contextService;
        this.unitService = unitService;
        this.transformService = transformService;
        this.parseResultRequester = parseResultRequester;
        this.analysisResultRequester = analysisResultRequester;
    }


    @Override public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final List<TransformResource> resources = actionUtils.getActiveResources(e);
        logger.debug("Transforming active resources: {}", resources);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                final List<FileObject> outputFiles = transformAll(resources);
                logger.debug("Transformation resulted in output files: {}", outputFiles);
                for(final FileObject output : outputFiles) {
                    @Nullable final VirtualFile virtualFile = resourceService.unresolve(output);
                    if(virtualFile != null) {
                        virtualFile.refresh(true, false);
                        logger.debug("Refreshed output file: {}", virtualFile);
                    }
                }
            } catch(final MetaborgException ex) {
                logger.error("An exception occurred: {}", ex);
            }
        });

        logger.info("Transformed active resources: {}", resources);
    }

    private List<FileObject> transformAll(final List<TransformResource> resources)
        throws ContextException, TransformException {
        final List<FileObject> outputFiles = new ArrayList<>(resources.size());
        for(final TransformResource transformResource : resources) {
            final FileObject resource = transformResource.resource();
            final Collection<? extends ITransformUnit<?>> transformResults =
                transform(resource, transformResource.project(), language, transformResource.text());
            for(ITransformUnit<?> result : transformResults) {
                outputFiles.add(result.output());
            }
        }

        return outputFiles;
    }

    private Collection<? extends ITransformUnit<?>> transform(final FileObject resource, final IProject project,
        final ILanguageImpl language, final String text) throws ContextException, TransformException {
        final Collection<? extends ISpoofaxTransformUnit<?>> transformResults;
        final IContext context = contextService.get(resource, project, language);
        final ISpoofaxInputUnit input = unitService.inputUnit(resource, text, language, null);
        if(transformService.requiresAnalysis(context, goal)) {
            logger.debug("Requesting analysis result.");
            final ISpoofaxAnalyzeUnit result = analysisResultRequester.request(input, context).toBlocking().single();
            logger.debug("Requesting context read lock.");
            try(IClosableLock lock = context.read()) {
                logger.debug("Transforming: {}", resource);
                transformResults = transformService.transform(result, context, goal);
                logger.info("Transformed: {}", resource);
            }
        } else {
            logger.debug("Requesting parse result.");
            final ISpoofaxParseUnit result = parseResultRequester.request(input).toBlocking().single();
            logger.debug("Transforming: {}", resource);
            transformResults = transformService.transform(result, context, goal);
            logger.info("Transformed: {}", resource);
        }
        return transformResults;
    }

}
