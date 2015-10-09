package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Singleton;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.PsiElement;
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
        PsiBuilder.Marker m = builder.mark();
        //this.logger.info("Mark!");
        while (!builder.eof())
        {
            builder.advanceLexer();
            //this.logger.info("advance...");
        }
        SpoofaxIdeaLanguage language = (SpoofaxIdeaLanguage)root.getLanguage();
        m.done(this.dummyAstTokenType);
        //this.logger.info("Done!");
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
