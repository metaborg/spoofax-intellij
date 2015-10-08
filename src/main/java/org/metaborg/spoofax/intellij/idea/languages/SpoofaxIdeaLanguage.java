package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;

/**
 * A Spoofax languages used in IntelliJ IDEA.
 *
 * There are no implementations of this class because it's instantiated dynamically.
 */
public abstract class SpoofaxIdeaLanguage extends Language {

    @NotNull
    private final ILanguageImpl language;

    /**
     * Gets the associated language implementation.
     *
     * @return The associated language implementation.
     */
    @NotNull
    public final ILanguageImpl language() {
        return this.language;
    }

    /**
     * Initializes a new instance of the {@link SpoofaxIdeaLanguage} class.
     *
     * @param language The language implementation.
     */
    protected SpoofaxIdeaLanguage(@NotNull final ILanguageImpl language) {
        super(language.id().toString());

        this.language = language;
    }
}
