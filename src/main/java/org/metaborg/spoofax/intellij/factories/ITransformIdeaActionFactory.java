package org.metaborg.spoofax.intellij.factories;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.core.menu.TransformAction;
import org.metaborg.spoofax.intellij.menu.TransformationAction;

/**
 * Factory for transformation actions.
 */
public interface ITransformIdeaActionFactory {

    /**
     * Creates a new transformation action.
     *
     * @param id       The ID of the action.
     * @param language The language implementation.
     * @param action   The action.
     * @return The created action.
     */
    @NotNull
    TransformationAction create(@NotNull String id, @NotNull ILanguageImpl language, @NotNull TransformAction action);

}
