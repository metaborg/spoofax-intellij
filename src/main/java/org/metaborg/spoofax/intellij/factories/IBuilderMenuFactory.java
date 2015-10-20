package org.metaborg.spoofax.intellij.factories;

import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.idea.gui.BuilderMenu;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;

/**
 * Factory for builder menu's.
 */
public interface IBuilderMenuFactory {

    /**
     * Creates a new builder menu.
     *
     * @param implementation The language implementation.
     * @return The created lexer.
     */
    @NotNull
    BuilderMenu create(@NotNull ILanguageImpl implementation);

}
