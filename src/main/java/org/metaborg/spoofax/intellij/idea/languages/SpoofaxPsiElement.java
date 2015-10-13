package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Spoofax PSI element.
 */
public class SpoofaxPsiElement extends ASTWrapperPsiElement implements ISpoofaxPsiElement {

    /**
     * Initializes a new instance of the {@link SpoofaxPsiElement} class.
     *
     * @param node The node to which the element is attached.
     */
    public SpoofaxPsiElement(@NotNull final ASTNode node) {
        super(node);
    }

    /**
     * Accepts a PSI element visitor.
     *
     * @param visitor The visitor.
     */
    public void accept(@NotNull final PsiElementVisitor visitor) {
        if (visitor instanceof SpoofaxPsiVisitor)
            ((SpoofaxPsiVisitor) visitor).visitProperty(this);
        else super.accept(visitor);
    }

}
