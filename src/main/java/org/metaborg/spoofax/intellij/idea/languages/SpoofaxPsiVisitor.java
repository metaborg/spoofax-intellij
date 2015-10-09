package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class SpoofaxPsiVisitor extends PsiElementVisitor {

    public void visitProperty(@NotNull ISpoofaxPsiElement o) {
        visitPsiElement(o);
    }

    public void visitPsiElement(@NotNull PsiElement o) {
        visitElement(o);
    }


}
