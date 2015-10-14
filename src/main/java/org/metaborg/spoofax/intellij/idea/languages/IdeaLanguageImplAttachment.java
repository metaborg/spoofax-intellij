package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;

/**
 * Stores the IntelliJ IDEA objects that are associated with a particular {@link ILanguageImpl}.
 */
public final class IdeaLanguageImplAttachment {

    @NotNull
    public final Lexer lexer;

    /**
     * Creates a new instance of the {@link IdeaLanguageImplAttachment} class.
     *
     * @param lexer The lexer.
     */
    /* package private */ IdeaLanguageImplAttachment(@NotNull final Lexer lexer) {
        this.lexer = lexer;
    }
}