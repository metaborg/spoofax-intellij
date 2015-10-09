package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;

/**
 * Stores the IntelliJ IDEA objects that are associated with a particular {@link ILanguageImpl}.
 */
public final class IdeaLanguageImplAttachment {

    @NotNull
    public final SpoofaxLexer lexer;
    @NotNull
    public final OldSpoofaxParser parser;

    public IdeaLanguageImplAttachment(@NotNull final SpoofaxLexer lexer,
                                      @NotNull final OldSpoofaxParser parser) {
        this.lexer = lexer;
        this.parser = parser;
    }
}