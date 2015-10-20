package org.metaborg.spoofax.intellij.factories;

import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;
import org.metaborg.spoofax.intellij.menu.BuilderActionGroup;

/**
 * Factory for builder action group.
 */
public interface IBuilderActionGroupFactory {

    /**
     * Creates a builder action group.
     *
     * @param language          The language implementation.
     * @return The created action group.
     */
    @NotNull
    BuilderActionGroup create(@NotNull ILanguageImpl language);
}
