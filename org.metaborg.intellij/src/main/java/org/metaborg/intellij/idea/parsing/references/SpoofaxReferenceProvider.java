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

package org.metaborg.intellij.idea.parsing.references;

import com.google.inject.*;
import com.intellij.openapi.vfs.*;
import com.intellij.util.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.*;
import org.metaborg.core.analysis.*;
import org.metaborg.core.language.*;
import org.metaborg.core.processing.analyze.*;
import org.metaborg.core.project.*;
import org.metaborg.core.source.*;
import org.metaborg.core.tracing.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.resources.*;
import org.spoofax.interpreter.terms.*;

import javax.annotation.*;
import javax.annotation.Nullable;
import java.util.*;

// TODO: Remove IStrategoTerm type arguments to make it generic, then rename to Metaborg (and the Reference too).
/**
 * Provides reference resolution for Spoofax languages.
 */
public final class SpoofaxReferenceProvider extends MetaborgReferenceProvider {

    private final Object objectLock = new Object();
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
     * {@inheritDoc}
     */
    @Override
    protected Iterable<MetaborgReference> getReferences(
            final ILanguageImpl language,
            final IProject project,
            final FileObject resource,
            final MetaborgReferenceElement element,
            final ProcessingContext context) {
        @Nullable final AnalysisFileResult<IStrategoTerm, IStrategoTerm> analysisFileResult
                = this.analysisResultRequester.get(resource);
        if (analysisFileResult == null)
            return Collections.emptyList();

        synchronized(this.objectLock) {
            try {
                @Nullable final Resolution resolution = this.resolverService.resolve(
                        element.getTextOffset(), analysisFileResult);

                if (resolution == null) {
                    return Collections.emptyList();
                }

                return getMetaborgReferences(element, resolution);
            } catch (final MetaborgException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @NotNull
    private Iterable<MetaborgReference> getMetaborgReferences(final MetaborgReferenceElement element,
                                                              final Resolution resolution) {
        final List<MetaborgReference> references = new ArrayList<>();
        for (final ISourceLocation location : resolution.targets) {
            @Nullable final SpoofaxReference reference = getMetaborgReference(element, location);
            if (reference != null) {
                references.add(reference);
            }
        }
        return references;
    }

    @Nullable
    private SpoofaxReference getMetaborgReference(final MetaborgReferenceElement element,
                                                  final ISourceLocation location) {

        synchronized(this.objectLock) {
            @Nullable final FileObject targetResource = location.resource();
            if (targetResource == null)
                return null;
            @Nullable final VirtualFile targetVirtualFile = this.resourceService.unresolve(targetResource);
            if (targetVirtualFile == null)
                return null;

            return new SpoofaxReference(
                    element,
                    targetVirtualFile,
                    location.region()
            );
        }
    }

}
