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

package org.metaborg.intellij.idea.compilation;

import com.google.inject.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.module.Module;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.annotation.*;

/**
 * Reloads the project's languages after compile.
 */
public final class ReloadLanguageCompileTask implements IAfterCompileTask {

    private final IIdeaProjectService projectService;
    private final IIdeaLanguageManager languageManager;
    private final ILanguageDiscoveryService discoveryService;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link ReloadLanguageCompileTask} class.
     */
    @Inject
    public ReloadLanguageCompileTask( final IIdeaProjectService projectService,
            final IIdeaLanguageManager languageManager,
            final ILanguageDiscoveryService discoveryService) {
        this.projectService = projectService;
        this.languageManager = languageManager;
        this.discoveryService = discoveryService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(final CompileContext context) {
        final Module[] affectedModules = context.getCompileScope().getAffectedModules();

        ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            this.logger.debug("Reloading language specification languages.");

            for (final Module module : affectedModules) {
                @Nullable final IdeaProject project = this.projectService.get(module);
                if (project instanceof IdeaLanguageSpec) {
                    this.logger.debug("Reloading languages of language specification: {}", project);
                    this.languageManager.reloadLanguageSpec((IdeaLanguageSpec)project);
                } else {
                    this.logger.debug("Module skipped as it's not a language specification project: {}", module);
                }
            }

            this.logger.info("Reloaded language specification languages.");
        }));

        return true;
    }

}
