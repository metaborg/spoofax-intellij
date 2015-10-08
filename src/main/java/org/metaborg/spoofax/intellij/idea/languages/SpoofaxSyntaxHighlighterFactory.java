package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class SpoofaxSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    private SpoofaxParserDefinition parserDefinition;

    public SpoofaxSyntaxHighlighterFactory(SpoofaxParserDefinition parserDefinition) {
        this.parserDefinition = parserDefinition;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        return new SpoofaxSyntaxHighlighter(parserDefinition, project);
    }
}
