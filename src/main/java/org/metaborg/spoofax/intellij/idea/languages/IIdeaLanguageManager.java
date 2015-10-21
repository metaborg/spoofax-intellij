package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;

import java.util.Set;

/**
 * Manages the Spoofax languages loaded in IntelliJ IDEA.
 */
public interface IIdeaLanguageManager {

    /**
     * Loads a language.
     *
     * @param language The language to load.
     */
    void load(@NotNull ILanguage language);

    /**
     * Unloads a language.
     *
     * @param language The language to unload.
     * @return <code>true</code> when the language was successfully unloaded;
     * otherwise, <code>false</code> when the language was not loaded.
     */
    boolean unload(@NotNull ILanguage language);

    /**
     * Returns whether the specified language is currently loaded.
     *
     * @param language The language to check.
     * @return <code>true</code> when the language
     * is loaded; otherwise <code>false</code>.
     */
    boolean isLoaded(@NotNull ILanguage language);

    /**
     * Returns whether the specified language could ever be loaded by this manager.
     * <p>
     * The result of this method does not depend on whether the language is already loaded.
     *
     * @param language The language to check.
     * @return <code>true</code> when this manager can load the language;
     * otherwise, <code>false</code>.
     */
    boolean canLoad(@NotNull ILanguage language);

    /**
     * Returns a collection of loaded languages.
     *
     * @return The loaded languages.
     */
    @NotNull
    Set<ILanguage> getLoaded();

}
