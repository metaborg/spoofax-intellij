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

package org.metaborg.idea.spoofax.psi;

import com.google.inject.Inject;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ProcessingContext;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageProjectService;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.project.IProject;
import org.metaborg.core.source.ISourceLocation;
import org.metaborg.core.tracing.IResolverService;
import org.metaborg.core.tracing.Resolution;
import org.metaborg.idea.psi.MetaborgReference;
import org.metaborg.idea.psi.MetaborgReferenceElement;
import org.metaborg.idea.psi.MetaborgReferenceProvider;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxReference;
import org.metaborg.idea.project.IIdeaProjectService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides reference resolution for Spoofax languages.
 */
public final class SpoofaxReferenceProvider extends MetaborgReferenceProvider {

    private final IIntelliJResourceService resourceService;
    private final IResolverService<IStrategoTerm, IStrategoTerm> resolverService;
    private final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester;

    /**
     * Initializes a new instance of the {@link MetaborgReferenceProvider} class.
     *
     * @param projectService         The project service.
     * @param resourceService        The resource service.
     * @param languageProjectService The language project service.
     */
    @Inject
    public SpoofaxReferenceProvider(
            final IIdeaProjectService projectService,
            final IIntelliJResourceService resourceService,
            final ILanguageProjectService languageProjectService,
            final IResolverService<IStrategoTerm, IStrategoTerm> resolverService,
            final IAnalysisResultRequester<IStrategoTerm, IStrategoTerm> analysisResultRequester) {
        super(projectService, resourceService, languageProjectService);
        this.resourceService = resourceService;
        this.resolverService = resolverService;
        this.analysisResultRequester = analysisResultRequester;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    protected Iterable<MetaborgReference> getReferences(
            final ILanguageImpl language,
            final IProject project,
            final FileObject resource,
            final MetaborgReferenceElement element,
            final ProcessingContext context) {
        AnalysisFileResult<IStrategoTerm, IStrategoTerm> analysisFileResult = this.analysisResultRequester.get(
                resource);
        if (analysisFileResult == null)
            return Collections.emptyList();

        List<MetaborgReference> references = new ArrayList<>();
        try {
            Resolution resolution = this.resolverService.resolve(element.getTextOffset(), analysisFileResult);
            if (resolution != null) {
                for (ISourceLocation location : resolution.targets) {
                    FileObject targetResource = location.resource();
                    if (targetResource == null)
                        continue;
                    VirtualFile targetVirtualFile = this.resourceService.unresolve(targetResource);
                    SpoofaxReference reference = new SpoofaxReference(element, targetVirtualFile, location.region());
                    references.add(reference);
                }
            }
        } catch (MetaborgException e) {
            throw new RuntimeException(e);
        }

        return references;
    }

}
