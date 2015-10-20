package org.metaborg.spoofax.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;

// Will be replaced in the future.
public class TestAction extends AnAction {

    //    @NotNull
//    private final LanguageManager languageManager;
//    @NotNull
//    private final ILanguageService languageService;
//    @NotNull
//    private final IIdeaLanguageManager ideaLanguageManager;
//    @NotNull
//    private final IProjectLanguageIdentifierService languageIdentifierService;
    private ILanguageImpl implementation;

    public TestAction(ILanguageImpl implementation) {
        // TODO: Cleanup
//        this.languageManager = IdeaPlugin.injector().getInstance(LanguageManager.class);
//        this.languageService = IdeaPlugin.injector().getInstance(ILanguageService.class);
//        this.ideaLanguageManager = IdeaPlugin.injector().getInstance(IIdeaLanguageManager.class);
        this.implementation = implementation;
    }

    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        System.out.println("Test! >>" + project);

        //this.languageManager.loadMetaLanguages();
//        WriteCommandAction.runWriteCommandAction(project, () -> {
//            for (ILanguage language : this.languageService.getAllLanguages()) {
////                for (ILanguageImpl implementation : language.impls()) {
////                    this.ideaLanguageManager.load(implementation);
////                }
////                ILanguageImpl implementation = this.languageIdentifierService.identify(language, null);
//                if (this.ideaLanguageManager.canLoad(language))
//                    this.ideaLanguageManager.load(language);
//            }
//        });

    }
}
