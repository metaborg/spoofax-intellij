package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

// NOTE: This class will eventually be replaced.
public class OldSpoofaxTokenType extends IElementType {

    public OldSpoofaxTokenType(@NotNull String debugName, @NotNull SpoofaxIdeaLanguage language) {
        super(debugName, language);
    }

    @Override
    public String toString() {
        return "OldSpoofaxTokenType." + super.toString();
    }

}