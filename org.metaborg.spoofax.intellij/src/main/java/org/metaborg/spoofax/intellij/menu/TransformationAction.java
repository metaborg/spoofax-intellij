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
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.ITransformAction;
import org.metaborg.core.action.ITransformGoal;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.ILogger;

import java.util.List;

//import org.jetbrains.annotations.NotNull;

/**
 * A transformation action from a builder menu.
 */
public final class TransformationAction<P, A, T> extends AnActionWithId {

    private final ITransformGoal goal;
    private final ILanguageImpl language;
    private final ActionHelper actionHelper;
    private final IIntelliJResourceService resourceService;
    private final IResourceTransformer transformer;
    @InjectLogger
    private ILogger logger;

    @Inject
    private TransformationAction(
            @Assisted final String id,
            @Assisted final ITransformAction action,
            @Assisted final ILanguageImpl language,
            final ActionHelper actionHelper,
            final IResourceTransformer transformer,
            final IIntelliJResourceService resourceService) {
        super(id, action.name(), null, null);
        this.language = language;
        this.goal = action.goal();
        this.actionHelper = actionHelper;
        this.transformer = transformer;
        this.resourceService = resourceService;
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        final List<TransformResource> resources = this.actionHelper.getActiveResources(e);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                final List<FileObject> outputFiles = this.transformer.execute(resources, this.language, this.goal);
                for (final FileObject output : outputFiles) {

                    final VirtualFile virtualFile = this.resourceService.unresolve(output);
                    virtualFile.refresh(true, false);
//                    FileEditorManager.getInstance(project).openFile(virtualFile, false);
                }
            } catch (final MetaborgException ex) {
                this.logger.error("An exception occurred: {}", ex);
            }
        });
    }

}