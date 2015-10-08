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
import org.metaborg.spoofax.intellij.languages.IdeaLanguageManager;
import org.metaborg.spoofax.intellij.languages.LanguageManager;

/**
 * Created by daniel on 10/8/15.
 */
public class LoadLanguagesAction extends AnAction {

    @NotNull
    private final LanguageManager languageManager;
    @NotNull
    private final ILanguageService languageService;
    @NotNull
    private final IdeaLanguageManager ideaLanguageManager;

    public LoadLanguagesAction() {
        this.languageManager = IdeaPlugin.injector().getInstance(LanguageManager.class);
        this.languageService = IdeaPlugin.injector().getInstance(ILanguageService.class);
        this.ideaLanguageManager = IdeaPlugin.injector().getInstance(IdeaLanguageManager.class);
    }

    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        this.languageManager.loadMetaLanguages();
        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (ILanguage language : this.languageService.getAllLanguages()) {
                this.ideaLanguageManager.register(language);
            }
        });

    }
}
