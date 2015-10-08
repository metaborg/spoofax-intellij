package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;

import java.util.Collection;
import java.util.Set;

/**
 * Manages the Spoofax languages loaded in IntelliJ IDEA.
 */
public interface IIdeaLanguageManager {

    /**
     * Loads a language implementation.
     *
     * @param language The language implementation to load.
     */
    void load(@NotNull ILanguageImpl language);

    /**
     * Unloads a language implementation.
     *
     * @param language The language implementation to unload.
     *
     * @return <code>true</code> when the language implementation was successfully unloaded;
     * otherwise, <code>false</code> when the language was not loaded.
     */
    boolean unload(@NotNull ILanguageImpl language);

    /**
     * Returns whether the specified language implementation is currently loaded.
     *
     * @param language The language implementation to check.
     * @return <code>true</code> when the language
     * is loaded; otherwise <code>false</code>.
     */
    boolean isLoaded(@NotNull ILanguageImpl language);

    /**
     * Returns a collection of loaded language implementations.
     * @return The loaded language implementations.
     */
    @NotNull
    Set<ILanguageImpl> getLoaded();

}
