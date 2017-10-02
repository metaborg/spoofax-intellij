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

package org.metaborg.intellij.idea.parsing.references;

import com.google.inject.Inject;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ProcessingContext;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.source.ISourceLocation;
import org.metaborg.core.tracing.Resolution;
import org.metaborg.intellij.idea.languages.ILanguageProjectService;
import org.metaborg.intellij.idea.projects.IIdeaProjectService;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.core.processing.analyze.ISpoofaxAnalysisResultRequester;
import org.metaborg.spoofax.core.tracing.ISpoofaxResolverService;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO: Remove IStrategoTerm type arguments to make it generic, then rename to Metaborg (and the Reference too).

/**
 * Provides reference resolution for Spoofax languages.
 */
public final class SpoofaxReferenceProvider extends MetaborgReferenceProvider {

    private final Object objectLock = new Object();
    private final IIntelliJResourceService resourceService;
    private final ISpoofaxResolverService resolverService;
    private final ISpoofaxAnalysisResultRequester analysisResultRequester;

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
            final ISpoofaxResolverService resolverService,
            final ISpoofaxAnalysisResultRequester analysisResultRequester) {
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
        @Nullable final ISpoofaxAnalyzeUnit analysisFileResult
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
                if (e.getMessage().endsWith("it does not have a resolver facet"))
                    return Collections.emptyList();

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
