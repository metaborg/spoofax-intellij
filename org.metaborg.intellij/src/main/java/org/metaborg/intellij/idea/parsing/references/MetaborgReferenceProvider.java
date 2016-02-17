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

import com.google.common.base.*;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.*;
import com.intellij.util.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.intellij.utils.*;

import javax.annotation.*;

/**
 * Provides the references for a certain PSI element in a certain context.
 */
public abstract class MetaborgReferenceProvider extends PsiReferenceProvider {

    private final IIdeaProjectService projectService;
    private final IIntelliJResourceService resourceService;
    private final ILanguageProjectService languageProjectService;

    /**
     * Initializes a new instance of the {@link MetaborgReferenceProvider} class.
     *
     * @param projectService         The project service.
     * @param resourceService        The resource service.
     * @param languageProjectService The language project service.
     */
    protected MetaborgReferenceProvider(
            final IIdeaProjectService projectService,
            final IIntelliJResourceService resourceService,
            final ILanguageProjectService languageProjectService) {
        super();
        Preconditions.checkNotNull(projectService);
        Preconditions.checkNotNull(resourceService);
        Preconditions.checkNotNull(languageProjectService);

        this.projectService = projectService;
        this.resourceService = resourceService;
        this.languageProjectService = languageProjectService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final PsiReference[] getReferencesByElement(
            final PsiElement element, final ProcessingContext context) {
        final MetaborgFile fileElement = (MetaborgFile)element.getContainingFile();
        final ILanguage language = fileElement.getFileType().getMetaborgLanguage();
        @Nullable final FileObject resource = getResource(element);
        if (resource == null)
            return PsiReference.EMPTY_ARRAY;
        @Nullable final IdeaProject project = this.projectService.get(element);
        if (project == null)
            return PsiReference.EMPTY_ARRAY;
        @Nullable final LanguageDialect languageDialect = this.languageProjectService.getImpl(
                language, project, resource);
        assert languageDialect != null;

        final ILanguageImpl impl = languageDialect.dialectOrBaseLanguage();

        final Iterable<MetaborgReference> references = getReferences(impl, project, resource,
                                                                     (MetaborgReferenceElement)element, context
        );
        return CollectionUtils.toArray(references, PsiReference.class);
    }

    /**
     * Gets the references of the specified element.
     * <p>
     * The returned references may be invalid, that is, return <code>null</code>
     * from their {@link MetaborgReference#resolve()} method.
     *
     * @param language The language implementation of the resource.
     * @param project  The project that contains the resource.
     * @param resource The resource that contains the element.
     * @param element  The reference PSI element.
     * @param context  The processing context.
     * @return An iterable of references.
     */
    protected abstract Iterable<MetaborgReference> getReferences(
            final ILanguageImpl language,
            final IProject project,
            final FileObject resource,
            final MetaborgReferenceElement element,
            final ProcessingContext context);

    /**
     * Gets the source file.
     *
     * @param element The PSI element.
     * @return The source file; or <code>null</code> when not known.
     */
    @Nullable
    private FileObject getResource(final PsiElement element) {
        @Nullable final PsiFile containingFile = element.getContainingFile();
        if (containingFile == null)
            return null;
        @Nullable final VirtualFile virtualFile = containingFile.getVirtualFile();
        if (virtualFile == null)
            return null;
        return this.resourceService.resolve(virtualFile);
    }
}
