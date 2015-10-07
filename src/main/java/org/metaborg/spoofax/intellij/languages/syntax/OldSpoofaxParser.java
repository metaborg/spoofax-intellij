package org.metaborg.spoofax.intellij.languages.syntax;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.languages.IdeaLanguageManager;
import org.metaborg.spoofax.intellij.languages.SpoofaxIdeaLanguage;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class OldSpoofaxParser implements ISpoofaxParser {

    @NotNull private final IdeaLanguageManager languageManager;

    public OldSpoofaxParser(@NotNull final IdeaLanguageManager languageManager) {
        this.languageManager = languageManager;
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
        m.done(this.languageManager.getDummyAstTokenType(language.language()));
        //this.logger.info("Done!");
        return builder.getTreeBuilt();
    }

    @NotNull
    public PsiElement createElement(@NotNull ASTNode node) {
        IElementType type = node.getElementType();
        if (type instanceof OldSpoofaxTokenType) {
            return new SpoofaxPsiElement(node);
        }
        throw new AssertionError("Unknown element type: " + type);
    }
}
