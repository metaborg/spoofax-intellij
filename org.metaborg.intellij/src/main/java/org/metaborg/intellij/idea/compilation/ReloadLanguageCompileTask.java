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

package org.metaborg.intellij.idea.compilation;

import com.google.inject.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.module.Module;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.meta.core.project.*;
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
                if (project instanceof ILanguageSpec) {
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
