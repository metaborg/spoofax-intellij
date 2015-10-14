package org.metaborg.spoofax.intellij.factories;

import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;

/**
 * Factory for character lexers.
 */
public interface ICharacterLexerFactory {

    /**
     * Creates a new character lexer, that is,
     * a lexer that lexes one character at a time.
     *
     * @param tokenTypeManager The token type manager.
     * @return The created lexer.
     */
    @NotNull
    Lexer create(@NotNull SpoofaxTokenTypeManager tokenTypeManager);

}
