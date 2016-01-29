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
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility functions for working with IntelliJ actions.
 */
@Singleton
public final class ActionHelper {

    private final IIntelliJResourceService resourceService;
    private final ILanguageIdentifierService identifierService;

    @Inject
    /* package private */ ActionHelper(
            final IIntelliJResourceService resourceService,
            final ILanguageIdentifierService identifierService) {
        this.resourceService = resourceService;
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
        final VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (files == null || files.length == 0)
            return Collections.emptyList();
        final ArrayList<TransformResource> result = new ArrayList<>(files.length);
        for (final VirtualFile file : files) {
            if (file.isDirectory())
                continue;
            final FileObject resource = this.resourceService.resolve(file);
            final Document document = FileDocumentManager.getInstance().getCachedDocument(file);
            result.add(new TransformResource(resource, document.getText()));
        }
        return result;
    }
}
