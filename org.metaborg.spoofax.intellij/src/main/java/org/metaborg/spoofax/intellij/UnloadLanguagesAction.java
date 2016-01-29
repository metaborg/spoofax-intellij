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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.spoofax.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.spoofax.intellij.languages.LanguageManager;

// Will be replaced in the future.
@Deprecated
public class UnloadLanguagesAction extends AnAction {
    @NotNull
    private final LanguageManager languageManager;
    @NotNull
    private final ILanguageService languageService;
    @NotNull
    private final IIdeaLanguageManager ideaLanguageManager;

    public UnloadLanguagesAction() {
        super();
        this.languageManager = SpoofaxIdeaPlugin.injector().getInstance(LanguageManager.class);
        this.languageService = SpoofaxIdeaPlugin.injector().getInstance(ILanguageService.class);
        this.ideaLanguageManager = SpoofaxIdeaPlugin.injector().getInstance(IIdeaLanguageManager.class);
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final java.util.Set<ILanguage> var = this.ideaLanguageManager.getLoaded();
        final ILanguage[] loadedLanguages = var.toArray(new ILanguage[var.size()]);

        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (final ILanguage language : loadedLanguages) {
                //for (ILanguage language : this.languageService.getAllLanguages()) {
                this.ideaLanguageManager.unload(language);
            }
        });

    }
}
