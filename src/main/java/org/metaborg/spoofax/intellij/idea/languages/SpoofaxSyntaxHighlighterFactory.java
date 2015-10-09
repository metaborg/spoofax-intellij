package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

public final class SpoofaxSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

//    @NotNull private final SpoofaxParserDefinition parserDefinition;
    @NotNull private final IIntelliJResourceService resourceService;
    @NotNull private final ILanguageIdentifierService identifierService;
    @NotNull private final ILexerParserManager lexerParserManager;

    @Inject
    private SpoofaxSyntaxHighlighterFactory(
            @NotNull final IIntelliJResourceService resourceService,
            @NotNull final ILanguageIdentifierService identifierService,
            @NotNull final ILexerParserManager lexerParserManager) {
        this.resourceService = resourceService;
        this.identifierService = identifierService;
        this.lexerParserManager = lexerParserManager;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        //Module module = ModuleUtil.findModuleForFile(virtualFile, project);
        FileObject file = this.resourceService.resolve(virtualFile);
        ILanguageImpl implementation = this.identifierService.identify(file);
        Lexer lexer = this.lexerParserManager.getHighlightingLexer(implementation);

        return new SpoofaxSyntaxHighlighter(lexer);
    }
}
