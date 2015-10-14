package org.metaborg.spoofax.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.spoofax.intellij.languages.LanguageManager;

// Will be replaced in the future.
public class UnloadLanguagesAction extends AnAction {
    @NotNull
    private final LanguageManager languageManager;
    @NotNull
    private final ILanguageService languageService;
    @NotNull
    private final IIdeaLanguageManager ideaLanguageManager;

    public UnloadLanguagesAction() {
        this.languageManager = IdeaPlugin.injector().getInstance(LanguageManager.class);
        this.languageService = IdeaPlugin.injector().getInstance(ILanguageService.class);
        this.ideaLanguageManager = IdeaPlugin.injector().getInstance(IIdeaLanguageManager.class);
    }

    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final ILanguage[] loadedLanguages = this.ideaLanguageManager.getLoaded().toArray(new ILanguage[0]);

        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (ILanguage language : loadedLanguages) {
                //for (ILanguage language : this.languageService.getAllLanguages()) {
                this.ideaLanguageManager.unload(language);
            }
        });

    }
}
