package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public interface ISpoofaxParser extends PsiParser {
    @NotNull
    public PsiElement createElement(@NotNull ASTNode node);
}
