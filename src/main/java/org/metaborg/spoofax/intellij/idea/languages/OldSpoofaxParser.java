package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Singleton;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

// NOTE: This class will eventually be replaced.
@Singleton
public final class OldSpoofaxParser implements PsiParser {

    @NotNull
    private final OldSpoofaxTokenType dummyAstTokenType;

    public OldSpoofaxParser(@NotNull final OldSpoofaxTokenType dummyAstTokenType) {
        this.dummyAstTokenType = dummyAstTokenType;
    }

    @NotNull
    @Override
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        PsiFile file = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY);
        VirtualFile vf = file.getVirtualFile();
        // TODO: Use the VirtualFile to determine the ILanguageImpl to use to parse.
        SpoofaxIdeaLanguage language = (SpoofaxIdeaLanguage) root.getLanguage();

        PsiBuilder.Marker m = builder.mark();
        parseFile(builder);
        //m.done(this.dummyAstTokenType);
        m.done(root);
        return builder.getTreeBuilt();
    }

    private void parseFile(PsiBuilder builder) {
        PsiBuilder.Marker m = builder.mark();
        while (!builder.eof()) {
            builder.advanceLexer();
        }
        m.done(this.dummyAstTokenType);
    }

}
