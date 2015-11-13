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

package org.metaborg.idea.psi;

import com.google.common.base.Preconditions;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.CollectionUtils;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.language.*;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxFile;
import org.metaborg.spoofax.intellij.project.IIntelliJProjectService;
import org.metaborg.spoofax.intellij.project.IntelliJProject;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import javax.annotation.Nullable;

/**
 * Provides the references for a certain PSI element in a certain context.
 */
public abstract class MetaborgReferenceProvider extends PsiReferenceProvider {

    private final IIntelliJProjectService projectService;
    private final IIntelliJResourceService resourceService;
    private final ILanguageProjectService languageProjectService;

    /**
     * Initializes a new instance of the {@link MetaborgReferenceProvider} class.
     *
     * @param projectService The project service.
     * @param resourceService The resource service.
     * @param languageProjectService The language project service.
     */
    protected MetaborgReferenceProvider(final IIntelliJProjectService projectService,
                                        final IIntelliJResourceService resourceService,
                                        final ILanguageProjectService languageProjectService) {
        Preconditions.checkNotNull(projectService);
        Preconditions.checkNotNull(resourceService);
        Preconditions.checkNotNull(languageProjectService);

        this.projectService = projectService;
        this.resourceService = resourceService;
        this.languageProjectService = languageProjectService;
    }

    /**
     *{@inheritDoc}
     */
    @NotNull
    @Override
    public final PsiReference[] getReferencesByElement(
            @NotNull final PsiElement element, @NotNull final ProcessingContext context) {
        SpoofaxFile fileElement = (SpoofaxFile)element.getContainingFile();
        ILanguage language = fileElement.getFileType().getSpoofaxLanguage();
        FileObject resource = getResource(element);
        IntelliJProject project = this.projectService.get(element);
        LanguageDialect languageDialect = this.languageProjectService.getImpl(language, project, resource);
        assert languageDialect != null;
        ILanguageImpl impl = languageDialect.dialectOrBaseLanguage();

        Iterable<MetaborgReference> references = getReferences(impl, project, resource, (MetaborgReferenceElement)element, context);
        return CollectionUtils.toArray(references, PsiReference.class);
    }

    /**
     * Gets the references of the specified element.
     *
     * The returned references may be invalid, that is, return <code>null</code>
     * from their {@link MetaborgReference#resolve()} method.
     *
     * @param language The language implementation of the resource.
     * @param project The project that contains the resource.
     * @param resource The resource that contains the element.
     * @param element The reference PSI element.
     * @param context The processing context.
     * @return An iterable of references.
     */
    protected abstract Iterable<MetaborgReference> getReferences(final ILanguageImpl language,
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
        PsiFile containingFile = element.getContainingFile();
        if (containingFile == null)
            return null;
        VirtualFile virtualFile = containingFile.getVirtualFile();
        if (virtualFile == null)
            return null;
        return this.resourceService.resolve(virtualFile);
    }
}
