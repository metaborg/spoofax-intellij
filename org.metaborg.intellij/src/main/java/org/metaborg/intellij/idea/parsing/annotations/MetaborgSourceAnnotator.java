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

package org.metaborg.intellij.idea.parsing.annotations;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.analysis.AnalysisException;
import org.metaborg.core.context.ContextException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.messages.MessageSeverity;
import org.metaborg.core.project.IProject;
import org.metaborg.core.source.ISourceRegion;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.idea.parsing.SourceRegionUtil;
import org.metaborg.intellij.idea.projects.IIdeaProjectService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.core.processing.analyze.ISpoofaxAnalysisResultRequester;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnitService;
import org.metaborg.util.log.ILogger;

/**
 * Annotates metaborg source files.
 */
@Singleton
public class MetaborgSourceAnnotator extends ExternalAnnotator<MetaborgSourceAnnotationInfo, ISpoofaxAnalyzeUnit> {
    private final IContextService contextService;
    private final IIdeaProjectService projectService;
    private final IIntelliJResourceService resourceService;
    private final ILanguageIdentifierService identifierService;
    private final ISpoofaxInputUnitService unitSerivce;
    private final ISpoofaxAnalysisResultRequester analysisResultProcessor;
    @InjectLogger
    private ILogger logger;


    @Inject
    public MetaborgSourceAnnotator(IContextService contextService, IIdeaProjectService projectService,
                                   IIntelliJResourceService resourceService, ILanguageIdentifierService identifierService,
                                   ISpoofaxInputUnitService unitSerivce, ISpoofaxAnalysisResultRequester analysisResultProcessor) {
        this.contextService = contextService;
        this.projectService = projectService;
        this.resourceService = resourceService;
        this.identifierService = identifierService;
        this.unitSerivce = unitSerivce;
        this.analysisResultProcessor = analysisResultProcessor;
    }


    @Override public @Nullable
    MetaborgSourceAnnotationInfo collectInformation(final PsiFile file) {
        throw new UnsupportedOperationException("This method is not expected to be called, ever.");
    }

    @Override public @Nullable
    MetaborgSourceAnnotationInfo collectInformation(final PsiFile file, final Editor editor,
                                                    final boolean hasErrors) {

        this.logger.debug("Collecting annotation information for file: {}", file);

        final MetaborgSourceAnnotationInfo info;
        @Nullable final IProject project = this.projectService.get(file);
        if(project == null) {
            this.logger.warn("Cannot annotate source code; cannot get language specification for resource {}. "
                + "Is the file excluded?", Objects.firstNonNull(file.getVirtualFile(), "<unknown>"));
            return null;
        }

        try {
            @Nullable final FileObject resource = this.resourceService.resolve(file);
            @Nullable final ILanguageImpl language = this.identifierService.identify(resource, project);
            if(language == null) {
                this.logger.warn("Skipping annotation. Could not identify the language of resource: {}", resource);
                return null;
            }
            final IContext context = this.contextService.get(resource, project, language);
            final String text = editor.getDocument().getImmutableCharSequence().toString();
            info = new MetaborgSourceAnnotationInfo(resource, text, context);
        } catch(final ContextException e) {
            throw LoggerUtils2.exception(this.logger, UnhandledException.class, "Unexpected unhandled exception.", e);
        }

        this.logger.info("Collected annotation information for file: {}", file);

        return info;
    }

    @Nullable
    @Override
    public ISpoofaxAnalyzeUnit doAnnotate(final MetaborgSourceAnnotationInfo info) {
        this.logger.debug("Requesting analysis result for file: {}", info.resource());

        @Nullable ISpoofaxAnalyzeUnit analysisResult = null;
        try {
            final IContext context = info.context();
            final ISpoofaxInputUnit input =
                unitSerivce.inputUnit(info.resource(), info.text(), context.language(), null);
            analysisResult = this.analysisResultProcessor.request(input, context).toBlocking().single();
        } catch(final RuntimeException ex) {
            // FIXME: Dedicated exception!
            if(ex.getCause() instanceof AnalysisException
                && ex.getCause().getMessage().equals("No analysis results.")) {
                this.logger.info("No analysis results for file: {}", info.resource());
            } else {
                this.logger.error("Runtime exception while annotating file: {}", ex, info.resource());
            }
        }

        this.logger.info("Requested analysis result for file: {}", info.resource());

        return analysisResult;
    }

    @Override public void apply(final PsiFile file, final ISpoofaxAnalyzeUnit analysisResult,
                                final AnnotationHolder holder) {

        this.logger.debug("Applying analysis result to file: {}", file);

        for(final IMessage message : analysisResult.messages()) {
            addAnnotation(message, file, holder);
        }

        this.logger.info("Applied analysis result to file: {}", file);
    }

    /**
     * Adds an annotation.
     *
     * @param message
     *            The message.
     * @param file
     *            The PSI file.
     * @param holder
     *            The annotation holder.
     */
    private void addAnnotation(final IMessage message, final PsiFile file, final AnnotationHolder holder) {
        final TextRange range = getRange(message.region(), file);
        final HighlightSeverity severity = getSeverity(message.severity());
        // NOTE: We can add a HTML tooltip if we want.
        holder.createAnnotation(severity, range, message.message());
    }

    /**
     * Gets the text range that corresponds to the specified source region.
     *
     * @param region
     *            The source region.
     * @param file
     *            The PSI file.
     * @return The text range.
     */
    private TextRange getRange(@Nullable final ISourceRegion region, final PsiFile file) {
        final TextRange range;
        if(region != null) {
            range = SourceRegionUtil.toTextRange(region);
        } else {
            // The message affects the entire source file.
            // FIXME: Is this actually desirable? Do we want to annotate the entire file?
            range = file.getTextRange();
        }
        return range;
    }

    /**
     * Gets the {@link HighlightSeverity} that corresponds to the specified {@link MessageSeverity}.
     * 
     * @param messageSeverity
     *            The severity (of the message).
     * @return The severity (of the annotation).
     */
    private HighlightSeverity getSeverity(final MessageSeverity messageSeverity) {
        switch(messageSeverity) {
            case ERROR:
                return HighlightSeverity.ERROR;
            case WARNING:
                return HighlightSeverity.WARNING;
            case NOTE:
                return HighlightSeverity.INFORMATION;
            default:
                return HighlightSeverity.INFORMATION;
        }
    }
}
