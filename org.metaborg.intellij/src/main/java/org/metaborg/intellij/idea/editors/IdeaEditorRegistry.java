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

package org.metaborg.intellij.idea.editors;

import com.google.inject.Inject;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.editor.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.projects.IdeaProject;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.*;

import javax.annotation.Nullable;
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
        assert resourceService != null;
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

    @Override
    public void open(final Iterable<FileObject> iterable, final IProject project) {
        // TODO: Ask user if they really want to open many files.

        for(final FileObject fo : iterable) {
            open(fo, project);
        }
    }
}
