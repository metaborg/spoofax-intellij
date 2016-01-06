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
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.ITransformAction;
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
import org.slf4j.Logger;

import javax.swing.*;
import java.util.List;

//import org.jetbrains.annotations.NotNull;

/**
 * A transformation action from a builder menu.
 */
public final class TransformationAction<P, A, T> extends AnActionWithId {

    private final ITransformGoal goal;
    private final ILanguageImpl language;
//    private final IContextService contextService;
//    private final ITransformService<P, A, T> transformService;
//    private final IParseResultRequester<P> parseResultRequester;
//    private final IAnalysisResultRequester<P, A> analysisResultRequester;
//    @NotNull
//    private final TransformAction action;
    @NotNull
    private final ActionHelper actionHelper;
    @NotNull
    private final IResourceTransformer transformer;
    @InjectLogger
    private Logger logger;

    @Inject
    private TransformationAction(
            @Assisted @NotNull final String id,
            @Assisted @NotNull final ITransformAction action,
            @Assisted @NotNull final ILanguageImpl language,
//            @Assisted @NotNull final TransformAction action,
//            @NotNull final ITransformService<P, A, T> transformService,
//            @NotNull final IContextService contextService,
//            @NotNull final IParseResultRequester<P> parseResultRequester,
//            @NotNull final IAnalysisResultRequester<P, A> analysisResultRequester,
            @NotNull final ActionHelper actionHelper,
            @NotNull final IResourceTransformer transformer) {
        super(id, action.name(), (String) null, (Icon) null);
        this.language = language;
        this.goal = action.goal();
//        this.action = action;
//        this.transformService = transformService;
//        this.parseResultRequester = parseResultRequester;
//        this.analysisResultRequester = analysisResultRequester;
//        this.contextService = contextService;
        this.actionHelper = actionHelper;
        this.transformer = transformer;
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        List<TransformResource> resources = this.actionHelper.getActiveResources(e);
        WriteCommandAction.runWriteCommandAction(project, () -> {
//            transformAll(resources, this.language, this.goal);
            try {
                this.transformer.execute(resources, this.language, this.goal);
            } catch (MetaborgException ex) {
                this.logger.error("An exception occurred: {}", ex);
            }
        });
    }

//    private void transformAll(Iterable<TransformResource> resources, ILanguageImpl language, ITransformGoal goal) {
//        for (TransformResource transformResource : resources) {
//            final FileObject resource = transformResource.resource();
//            try {
//                transform(resource, language, transformResource.text(), goal);
//            } catch (ContextException | TransformException e) {
//                logger.error("Transformation failed for {}", resource, e);
//            }
//        }
//    }
//
//    private void transform(FileObject resource, ILanguageImpl language, String text, ITransformGoal goal) throws ContextException, TransformException {
//        final IContext context = this.contextService.get(resource, language);
//        if (this.transformService.requiresAnalysis(context, goal)) {
//            final AnalysisFileResult<P, A> analysisResult =
//                    this.analysisResultRequester.request(resource, context, text).toBlocking().single();
//            try (IClosableLock lock = context.read()) {
//                this.transformService.transform(analysisResult, context, goal);
//            }
//        } else {
//            final ParseResult<P> parseResult =
//                    this.parseResultRequester.request(resource, language, text).toBlocking().single();
//            this.transformService.transform(parseResult, context, goal);
//        }
//    }
}