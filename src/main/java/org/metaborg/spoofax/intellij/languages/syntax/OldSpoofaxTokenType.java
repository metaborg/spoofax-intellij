package org.metaborg.spoofax.intellij.languages.syntax;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.languages.SpoofaxIdeaLanguage;

public class OldSpoofaxTokenType extends IElementType {

    public OldSpoofaxTokenType(@NotNull String debugName, @NotNull SpoofaxIdeaLanguage language) {
        super(debugName, language);
    }

    @Override
    public String toString() {
        return "OldSpoofaxTokenType." + super.toString();
    }

}