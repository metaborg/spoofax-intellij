package org.metaborg.spoofax.intellij.languages.syntax;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.languages.LanguageManager;

//@Singleton
public class SpoofaxSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    private SpoofaxParserDefinition parserDefinition;

    public SpoofaxSyntaxHighlighterFactory(SpoofaxParserDefinition parserDefinition) {
        this.parserDefinition = parserDefinition;
    }

//    /**
//     * This instance is created by IntelliJ's plugin system.
//     * Do not call this method manually.
//     */
//    public SpoofaxSyntaxHighlighterFactory() {
//        IdeaPlugin.injector().injectMembers(this);
//    }
//
//    @Inject
//    @SuppressWarnings("unused")
//    private final void inject(@NotNull final IIdeaLanguageManager ideaLanguageManager) {
//        this.ideaLanguageManager = ideaLanguageManager;
//    }

    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        //SpoofaxParserDefinition parserDefinition = ideaLanguageManager.getParserDefinition()
        // TODO
        return new SpoofaxSyntaxHighlighter(parserDefinition, project);
        //throw new RuntimeException();
    }
}
