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

package org.metaborg.intellij.idea.actions;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.*;
import org.metaborg.core.action.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.transformations.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

/**
 * A transformation action from a builder menu.
 */
public final class TransformationAction extends AnActionWithId {

    private final ITransformGoal goal;
    private final ILanguageImpl language;
    private final ActionUtils actionUtils;
    private final IIntelliJResourceService resourceService;
    private final IResourceTransformer transformer;
    @InjectLogger
    private ILogger logger;

    @Inject
    private TransformationAction(
            @Assisted final String id,
            @Assisted final ITransformAction action,
            @Assisted final ILanguageImpl language,
            final ActionUtils actionUtils,
            final IResourceTransformer transformer,
            final IIntelliJResourceService resourceService) {
        super(id, action.name(), null, null);
        this.language = language;
        this.goal = action.goal();
        this.actionUtils = actionUtils;
        this.transformer = transformer;
        this.resourceService = resourceService;
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        final List<TransformResource> resources = this.actionUtils.getActiveResources(e);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                final List<FileObject> outputFiles = this.transformer.execute(resources, this.language, this.goal);
                for (final FileObject output : outputFiles) {
                    @Nullable final VirtualFile virtualFile = this.resourceService.unresolve(output);
                    if (virtualFile != null) {
                        virtualFile.refresh(true, false);
                    }
                }
            } catch (final MetaborgException ex) {
                this.logger.error("An exception occurred: {}", ex);
            }
        });
    }

}