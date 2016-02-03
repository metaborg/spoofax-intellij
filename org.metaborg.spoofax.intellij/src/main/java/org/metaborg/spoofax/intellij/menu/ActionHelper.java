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
import com.google.inject.Singleton;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.ILanguageSpec;
import org.metaborg.core.project.ILanguageSpecService;
import org.metaborg.intellij.idea.project.IIdeaProjectService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility functions for working with IntelliJ actions.
 */
@Singleton
public final class ActionHelper {

    private final IIntelliJResourceService resourceService;
    private final IIdeaProjectService projectService;
    private final ILanguageSpecService languageSpecService;
    private final ILanguageIdentifierService identifierService;

    @Inject
    /* package private */ ActionHelper(
            final IIntelliJResourceService resourceService,
            final IIdeaProjectService projectService,
            final ILanguageSpecService languageSpecService,
            final ILanguageIdentifierService identifierService) {
        this.resourceService = resourceService;
        this.projectService = projectService;
        this.languageSpecService = languageSpecService;
        this.identifierService = identifierService;
    }

    /**
     * Determines whether all active files are of the specified language.
     *
     * @param e        The event arguments.
     * @param language The language implementation to check.
     * @return <code>true</code> when all active files are of the specified language;
     * otherwise, <code>false</code>.
     */
    public boolean isActiveFileLanguage(final AnActionEvent e, final ILanguageImpl language) {
        final List<FileObject> files = getActiveFiles(e);
        if (files.isEmpty())
            return false;
        for (final FileObject file : files) {
            if (!this.identifierService.identify(file, language))
                return false;
        }
        return true;
    }

    /**
     * Gets a list of files currently selected.
     *
     * @param e The event arguments.
     * @return A list of files.
     */
    public List<FileObject> getActiveFiles(final AnActionEvent e) {
        final VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (files == null || files.length == 0)
            return Collections.emptyList();
        final ArrayList<FileObject> result = new ArrayList<>(files.length);
        for (final VirtualFile file : files) {
            if (file.isDirectory())
                continue;
            result.add(this.resourceService.resolve(file));
        }
        return result;
    }

    /**
     * Gets a list of files currently selected.
     *
     * @param e The event arguments.
     * @return A list of files.
     */
    public List<TransformResource> getActiveResources(final AnActionEvent e) {
//        @Nullable final VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        @Nullable final PsiFile[] files = getSelectedPsiFiles(e);
        if (files == null || files.length == 0)
            return Collections.emptyList();
        final ArrayList<TransformResource> result = new ArrayList<>(files.length);
        for (final PsiFile file : files) {
            if (file.isDirectory())
                continue;
            final FileObject resource = this.resourceService.resolve(file.getVirtualFile());
            @Nullable final ILanguageSpec project = this.languageSpecService.get(this.projectService.get(file));
            @Nullable final Document document = FileDocumentManager.getInstance().getDocument(file.getVirtualFile());
            if (project == null || document == null)
                continue;
            result.add(new TransformResource(resource, project, document.getText()));
        }
//        for (final VirtualFile file : files) {
//            if (file.isDirectory())
//                continue;
//            final FileObject resource = this.resourceService.resolve(file);
//            final ILanguageSpec project = this.languageSpecService.get(this.projectService.get());
//            final Document document = FileDocumentManager.getInstance().getCachedDocument(file);
//            result.add(new TransformResource(resource, document.getText()));
//        }
        return result;
    }

    /**
     * Gets the {@link PsiFile} objects for each open file.
     *
     * @param e The event.
     * @return The PSI files; or <code>null</code>.
     */
    @Nullable
    private PsiFile[] getSelectedPsiFiles(final AnActionEvent e) {
        @Nullable final Project project = e.getData(CommonDataKeys.PROJECT);
        if (project == null)
            return null;
        @Nullable final VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files == null)
            return null;
        PsiFile[] psiFiles = new PsiFile[files.length];
        for (int i = 0; i < files.length; i++) {
            psiFiles[i] = PsiManager.getInstance(project).findFile(files[i]);
            if (psiFiles[i] == null)
                // If one of the files wasn't found in the project, it's probably a file
                // in a different project. No support for mixing projects like that.
                throw new RuntimeException("Couldn't determine PsiFile for VirtualFile.");
        }
        return psiFiles;
    }
}
