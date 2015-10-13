package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Token type for a single character.
 * <p>
 * This is used by the character lexer, see {@link org.metaborg.spoofax.intellij.factories.ICharacterLexerFactory}.
 */
public final class CharacterTokenType extends IElementType {

    /**
     * Initializes a new instance of the {@link CharacterTokenType} class.
     *
     * @param language The associated IDEA language.
     */
    public CharacterTokenType(@NotNull final SpoofaxIdeaLanguage language) {
        super("CHARACTER", language);
    }

}
