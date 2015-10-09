package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.style.IStyle;

/**
 * A single character token type.
 */
public final class CharacterTokenType extends IElementType {



    /**
     * Initializes a new instance of the {@link CharacterTokenType} class.
     *
     * @param language The associated IDEA language.
     */
    public CharacterTokenType(@NotNull SpoofaxIdeaLanguage language) {
        super("CHARACTER", language);
    }

    @Override
    public String toString() {
        return CharacterTokenType.class.getName() + "." + super.toString();
    }

}
