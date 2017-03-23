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

package org.metaborg.intellij.idea.actions;

import com.google.inject.Inject;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.intellij.idea.projects.IIdeaProjectService;
import org.metaborg.intellij.idea.transformations.TransformResource;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.ILogger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility functions for working with IntelliJ IDEA actions.
 */
public final class ActionUtils {

    private final IIntelliJResourceService resourceService;
    private final IIdeaProjectService projectService;
    private final ILanguageIdentifierService identifierService;
    @InjectLogger
    private ILogger logger;

    @Inject
    public ActionUtils(
            final IIntelliJResourceService resourceService,
            final IIdeaProjectService projectService,
            final ILanguageIdentifierService identifierService) {
        assert resourceService != null;
        assert projectService != null;
        assert identifierService != null;

        this.resourceService = resourceService;
        this.projectService = projectService;
        this.identifierService = identifierService;
    }

    /**
     * Adds an action(group) to a parent and registers all its children.
     *
     * @param action   The action to add.
     * @param parentID The parent ID.
     * @param relativeToActionId The ID relative to which to position this action; or <code>null</code>.
     * @param anchor The anchor indicating where to position this action; or <code>null</code> for the default.
     */
    public void addAndRegisterActionGroup(final AnAction action, final String parentID,
                                          @Nullable final String relativeToActionId, @Nullable final Anchor anchor) {
        final ActionManager manager = ActionManager.getInstance();
        final DefaultActionGroup parent = (DefaultActionGroup)manager.getAction(parentID);
        parent.add(action, getActionConstraints(relativeToActionId, anchor));
        registerAction(manager, action);
    }

    /**
     * Gets an object that specifies where the action is positioned.
     *
     * @param relativeToActionId The action ID relative to which to position the action;
     *                           or <code>null</code> to position the action at the start or end.
     * @param anchor The anchor indicating where to position the action;
     *               or <code>null</code> to position the action after or at the end.
     * @return The {@link Constraints}.
     */
    private Constraints getActionConstraints(@Nullable final String relativeToActionId, @Nullable final Anchor anchor) {
        if (relativeToActionId != null && anchor != null) {
            return new Constraints(anchor, relativeToActionId);
        } else if (relativeToActionId != null) {
            return new Constraints(Anchor.AFTER, relativeToActionId);
        } else if (anchor == Anchor.BEFORE || anchor == Anchor.FIRST) {
            return Constraints.FIRST;
        } else {
            return Constraints.LAST;
        }
    }

    /**
     * Registers the action and its children.
     *
     * @param action The action.
     */
    private void registerAction(final ActionManager manager, final AnAction action) {
        if (action instanceof AnActionWithId) {
            manager.registerAction(((AnActionWithId)action).id(), action);
        }
        if (action instanceof DefaultActionGroup) {
            registerActionGroup(manager, (DefaultActionGroup)action);
        }
    }

    /**
     * Registers all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private void registerActionGroup(final ActionManager manager, final DefaultActionGroup actionGroup) {
        for (final AnAction action : actionGroup.getChildActionsOrStubs()) {
            registerAction(manager, action);
        }
    }

    /**
     * Removes an action(group) from a parent and unregisters all its children.
     *
     * @param action   The action to remove.
     * @param parentID The parent ID.
     */
    public void removeAndUnregisterActionGroup(final AnAction action, final String parentID) {
        final ActionManager manager = ActionManager.getInstance();
        final DefaultActionGroup parent = (DefaultActionGroup)manager.getAction(parentID);
        parent.remove(action);
        unregisterAction(manager, action);
    }

    /**
     * Unregisters the action and its children.
     *
     * @param action The action.
     */
    private void unregisterAction(final ActionManager manager, final AnAction action) {
        if (action instanceof AnActionWithId) {
            manager.unregisterAction(((AnActionWithId)action).id());
        }
        if (action instanceof DefaultActionGroup) {
            unregisterActionGroup(manager, (DefaultActionGroup)action);
        }
    }

    /**
     * Unregisters all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private void unregisterActionGroup(
            final ActionManager manager,
            final DefaultActionGroup actionGroup) {
        for (final AnAction action : actionGroup.getChildActionsOrStubs()) {
            unregisterAction(manager, action);
        }
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
        @Nullable final VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
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
        @Nullable final PsiFile[] files = getSelectedPsiFiles(e);
        if (files == null || files.length == 0)
            return Collections.emptyList();
        final ArrayList<TransformResource> result = new ArrayList<>(files.length);
        for (final PsiFile file : files) {
            if (file.isDirectory())
                continue;
            final FileObject resource = this.resourceService.resolve(file.getVirtualFile());
            @Nullable final IProject project = this.projectService.get(file);
            @Nullable final Document document = FileDocumentManager.getInstance().getDocument(file.getVirtualFile());
            if (project == null || document == null) {
                this.logger.debug("Resource ignored because it has no project or document: {}", resource);
                continue;
            }
            result.add(new TransformResource(resource, project, document.getText()));
        }
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
        @Nullable final com.intellij.openapi.project.Project project = e.getData(CommonDataKeys.PROJECT);
        if (project == null)
            return null;

        @Nullable final VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files == null)
            return null;

        final PsiFile[] psiFiles = new PsiFile[files.length];
        for (int i = 0; i < files.length; i++) {
            psiFiles[i] = PsiManager.getInstance(project).findFile(files[i]);
            if (psiFiles[i] == null) {
                // If one of the files wasn't found in the project, it's probably a file
                // in a different project. No support for mixing projects like that.
                throw LoggerUtils2.exception(this.logger, RuntimeException.class,
                        "Couldn't determine PsiFile for VirtualFile: {}", files[i]);
            }
        }
        return psiFiles;
    }

}