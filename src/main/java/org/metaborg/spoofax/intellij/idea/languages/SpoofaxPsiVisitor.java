package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Visitor for Spoofax PSI elements.
 */
public final class SpoofaxPsiVisitor extends PsiElementVisitor {

    /**
     * Visits a property.
     *
     * @param property The property.
     */
    public void visitProperty(@NotNull ISpoofaxPsiElement property) {
        visitPsiElement(property);
    }

    /**
     * Visits a PSI element.
     *
     * @param element The element.
     */
    public void visitPsiElement(@NotNull PsiElement element) {
        visitElement(element);
    }


}
