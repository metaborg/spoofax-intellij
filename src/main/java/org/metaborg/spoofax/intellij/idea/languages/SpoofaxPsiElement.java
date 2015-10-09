package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class SpoofaxPsiElement extends ASTWrapperPsiElement implements ISpoofaxPsiElement {

    public SpoofaxPsiElement(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof SpoofaxPsiVisitor) ((SpoofaxPsiVisitor)visitor).visitProperty(this);
        else super.accept(visitor);
    }

}
