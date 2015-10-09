package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Singleton;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class OldSpoofaxParser implements ISpoofaxParser {

    @NotNull private final OldSpoofaxTokenType dummyAstTokenType;

    public OldSpoofaxParser(@NotNull final OldSpoofaxTokenType dummyAstTokenType) {
        this.dummyAstTokenType = dummyAstTokenType;
    }

    @NotNull
    @Override
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        PsiFile file = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY);
        VirtualFile vf = file.getVirtualFile();
        // TODO: Use the VirtualFile to determine the ILanguageImpl to use to parse.

        PsiBuilder.Marker m = builder.mark();
        while (!builder.eof())
        {
            builder.advanceLexer();
        }
        SpoofaxIdeaLanguage language = (SpoofaxIdeaLanguage)root.getLanguage();
        m.done(this.dummyAstTokenType);
        return builder.getTreeBuilt();
    }

//    @NotNull
//    public PsiElement createElement(@NotNull ASTNode node) {
//        IElementType type = node.getElementType();
//        if (type instanceof OldSpoofaxTokenType) {
//            return new SpoofaxPsiElement(node);
//        }
//        throw new AssertionError("Unknown element type: " + type);
//    }
}
