package org.metaborg.spoofax.intellij.factories;

import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;

/**
 * Factory for highlighting lexers.
 */
public interface IHighlightingLexerFactory {

    /**
     * Creates a highlighting lexer.
     *
     * @param language          The language implementation.
     * @param tokenTypesManager The token type manager.
     * @return The created lexer.
     */
    @NotNull
    Lexer create(@NotNull ILanguageImpl language, @NotNull SpoofaxTokenTypeManager tokenTypesManager);
}
