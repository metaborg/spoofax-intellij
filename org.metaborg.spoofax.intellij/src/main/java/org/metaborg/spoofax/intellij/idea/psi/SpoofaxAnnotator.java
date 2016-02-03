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

package org.metaborg.spoofax.intellij.idea.psi;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.analysis.IAnalysisService;
import org.metaborg.core.context.ContextException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.processing.parse.IParseResultRequester;
import org.metaborg.core.project.ILanguageSpec;
import org.metaborg.core.project.ILanguageSpecService;
import org.metaborg.core.project.IProject;
import org.metaborg.intellij.idea.project.IIdeaProjectService;
import org.metaborg.intellij.idea.project.IdeaProject;
import org.metaborg.intellij.idea.project.IdeaProjectService;
import org.metaborg.spoofax.intellij.SourceRegionUtil;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.ILogger;
import org.spoofax.interpreter.terms.IStrategoTerm;

@Singleton
public final class SpoofaxAnnotator extends ExternalAnnotator<SpoofaxAnnotationInfo, AnalysisFileResult<IStrategoTerm, IStrategoTerm>> {

    private final IContextService contextService;
    private final IIdeaProjectService projectService;
    private final ILanguageSpecService languageSpecService;
    private final IAnalysisService<IStrategoTerm, IStrategoTerm> analysisService;
    private final IIntelliJResourceService resourceService;
    private final ILanguageIdentifierService identifierService;
    private final IParseResultRequester<IStrategoTerm> parseResultRequester;
    private final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester;
    @InjectLogger
    private ILogger logger;

    @Inject
    public SpoofaxAnnotator(
            final IContextService contextService,
            final IIdeaProjectService projectService,
            final ILanguageSpecService languageSpecService,
            final IAnalysisService<IStrategoTerm, IStrategoTerm> analysisService,
            final IIntelliJResourceService resourceService,
            final ILanguageIdentifierService identifierService,
            final IParseResultRequester<IStrategoTerm> parseResultRequester,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester
    ) {
        super();
        this.contextService = contextService;
        this.projectService = projectService;
        this.languageSpecService = languageSpecService;
        this.analysisService = analysisService;
        this.resourceService = resourceService;
        this.identifierService = identifierService;
        this.parseResultRequester = parseResultRequester;
        this.analysisResultRequester = analysisResultRequester;
    }

    @Nullable
    @Override
    public SpoofaxAnnotationInfo collectInformation(final PsiFile file) {
        throw new UnsupportedOperationException("This method is not expected to be called, ever.");
    }

    @Nullable
    @Override
    public SpoofaxAnnotationInfo collectInformation(
            final PsiFile file, final Editor editor, final boolean hasErrors) {

        @Nullable final ILanguageSpec project = this.languageSpecService.get(this.projectService.get(file));

        if (project == null) {
            this.logger.warn("Cannot annotate source code; cannot get language specification for resource {}. Is the file excluded?",
                              Objects.firstNonNull(file.getVirtualFile(), "<unknown>"));
            return null;
        }

        try {
            final FileObject resource = this.resourceService.resolve(file.getVirtualFile());
            @Nullable final ILanguageImpl language = this.identifierService.identify(resource, project);
            final IContext context = this.contextService.get(resource, project, language);
            final String text = editor.getDocument().getImmutableCharSequence().toString();
            return new SpoofaxAnnotationInfo(resource, text, context);
        } catch (final ContextException e) {
            throw new RuntimeException("Unhandled exception.", e);
        }
    }

    @Nullable
    @Override
    public AnalysisFileResult<IStrategoTerm, IStrategoTerm> doAnnotate(final SpoofaxAnnotationInfo info) {
        return this.analysisResultRequester.request(
                info.resource(),
                info.context(),
                info.text()
        ).toBlocking().single();
    }

    @Override
    public void apply(
            final PsiFile file,
            final AnalysisFileResult<IStrategoTerm, IStrategoTerm> analysisResult,
            final AnnotationHolder holder) {
        for (final IMessage message : analysisResult.messages) {
            final TextRange range = SourceRegionUtil.toTextRange(message.region());
            final HighlightSeverity severity;
            switch (message.severity()) {
                case ERROR:
                    severity = HighlightSeverity.ERROR;
                    break;
                case WARNING:
                    severity = HighlightSeverity.WARNING;
                    break;
                case NOTE:
                    severity = HighlightSeverity.INFORMATION;
                    break;
                default:
                    severity = HighlightSeverity.INFORMATION;
                    break;
            }
            // NOTE: We can add a HTML tooltip if we want.
            holder.createAnnotation(severity, range, message.message());
        }
    }
}
