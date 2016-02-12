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

package org.metaborg.intellij.idea.editors;

import com.google.inject.*;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.editor.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

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
        // This is currently not supported. It's only used to reload languages,
        // and as we already do that ourselves, it's not worth the complexity of implementing this.

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

        @Nullable final VirtualFile virtualFile = this.resourceService.unresolve(resource);
        if (virtualFile == null) {
            this.logger.error("Can't open an editor. Couldn't get the virtual file for the resource: {}", resource);
            return;
        }

        FileEditorManager.getInstance(ideaProject).openFile(virtualFile, true);
    }
}
