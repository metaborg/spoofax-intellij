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

package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.spoofax.intellij.serialization.SpoofaxProjectService;
import org.metaborg.util.log.ILogger;

public final class SpoofaxProjectComponent implements ProjectComponent {

    private final Project project;
    private ILanguageService languageService;
    private IIdeaLanguageManager ideaLanguageManager;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxProjectComponent(final Project project) {
        this.project = project;
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final ILanguageService languageService, final IIdeaLanguageManager ideaLanguageManager) {
        this.languageService = languageService;
        this.ideaLanguageManager = ideaLanguageManager;
    }

    @Override
    public final void projectOpened() {
//        SpoofaxProjectService.getInstance(this.project).getState().setMyName("Project name!");
    }

    @Override
    public final void projectClosed() {

    }

    @Override
    public final void initComponent() {

    }

    @Override
    public final void disposeComponent() {

    }

    @NotNull
    @Override
    public final String getComponentName() {
        return this.getClass().getName();
    }
}
