package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;

/**
 * Creates and caches {@link IdeaLanguageAttachment}
 * and {@link IdeaLanguageImplAttachment} objects.
 */
public interface IIdeaAttachmentManager {

    /**
     * Gets the {@link IdeaLanguageAttachment} for a particular language.
     * <p>
     * If no {@link IdeaLanguageAttachment} exists yet for the language,
     * a new  {@link IdeaLanguageAttachment} is created and cached.
     *
     * @param language The language.
     * @return The corresponding {@link IdeaLanguageAttachment}.
     */
    @NotNull
    IdeaLanguageAttachment get(@NotNull ILanguage language);

    /**
     * Gets the {@link IdeaLanguageImplAttachment} for a particular language implementation.
     * <p>
     * If no {@link IdeaLanguageImplAttachment} exists yet for the language implementation,
     * a new  {@link IdeaLanguageImplAttachment} is created and cached.
     *
     * @param implementation The language implementation.
     * @return The corresponding {@link IdeaLanguageImplAttachment}.
     */
    @NotNull
    IdeaLanguageImplAttachment get(@NotNull ILanguageImpl implementation);
}
