package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;

/**
 * Manager for lexers and parsers.
 */
public interface ILexerParserManager {

    /**
     * Returns the highlighting lexer for the specified language implementation.
     *
     * @param implementation The language implementation.
     * @return The lexer.
     */
    @NotNull
    Lexer getHighlightingLexer(@NotNull ILanguageImpl implementation);

    /**
     * Returns the lexer for the specified language.
     *
     * @param language The language.
     * @return The lexer.
     */
    @NotNull
    Lexer getCharacterLexer(@NotNull ILanguage language);

    /**
     * Returns the parser for the specified language.
     *
     * @param language The language.
     * @return The parser.
     */
    @NotNull
    PsiParser getParser(@NotNull ILanguage language);

}
