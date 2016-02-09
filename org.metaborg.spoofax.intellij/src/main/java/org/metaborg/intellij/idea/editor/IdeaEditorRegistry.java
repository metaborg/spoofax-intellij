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

package org.metaborg.intellij.idea.editor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.editor.IEditor;
import org.metaborg.core.editor.IEditorRegistry;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.project.IProject;
import org.metaborg.intellij.idea.project.IdeaProject;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.ILogger;

import java.util.Collections;

/**
 * Editor registry for IntelliJ IDEA.
 *
 * A registry belongs to a single project.
 */
public final class IdeaEditorRegistry implements IEditorRegistry {

    private final IIntelliJResourceService resourceService;
    @InjectLogger
    private ILogger logger;

    @Inject
    public IdeaEditorRegistry(final IIntelliJResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<IEditor> openEditors() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(final FileObject resource, final IProject project) {

        // NOTE: Using EditorFactory.createDocument() you can create a Document that's
        // not bound to a VirtualFile. This may be used to display the result of a transformation
        // if it doesn't necessarily need to be saved (e.g. Show ATerm transformation).

        if (!(project instanceof IdeaProject)) {
            // We can't determine the Project the resource belongs to,
            // therefore we can't open an editor.
            this.logger.error("The resource is not contained in an IdeaProject: {}", resource);
            return;
        }
        final Project ideaProject = ((IdeaProject)project).getModule().getProject();

        final VirtualFile virtualFile = this.resourceService.unresolve(resource);
        FileEditorManager.getInstance(ideaProject).openFile(virtualFile, true);
    }
}
