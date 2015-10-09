package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.IProjectLanguageIdentifierService;

/**
 * Manager for lexers and parsers.
 *
 * Use the {@link IProjectLanguageIdentifierService} to get the language implementation
 * for a given language in a given project.
 */
public interface ILexerParserManager {

    /**
     * Returns the lexer for the specified language implementation.
     *
     * @param implementation The language implementation.
     * @return The lexer.
     */
    @NotNull
    Lexer getLexer(@NotNull ILanguageImpl implementation);

    /**
     * Returns the parser for the specified language implementation.
     *
     * @param implementation The language implementation.
     * @return The parser.
     */
    @NotNull
    PsiParser getParser(@NotNull ILanguageImpl implementation);

}
