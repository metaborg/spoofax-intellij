package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;

/**
 * A Spoofax languages used in IntelliJ IDEA.
 * <p>
 * There are no implementations of this class because it's instantiated dynamically.
 */
public abstract class SpoofaxIdeaLanguage extends Language {

    @NotNull
    private final ILanguage language;

    /**
     * Initializes a new instance of the {@link SpoofaxIdeaLanguage} class.
     *
     * @param language The language.
     */
    protected SpoofaxIdeaLanguage(@NotNull final ILanguage language) {
        super(language.name());

        this.language = language;
    }

    /**
     * Gets the associated language.
     *
     * @return The associated language.
     */
    @NotNull
    public final ILanguage language() {
        return this.language;
    }
}
