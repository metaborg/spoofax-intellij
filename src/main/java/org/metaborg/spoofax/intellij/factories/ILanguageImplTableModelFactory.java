package org.metaborg.spoofax.intellij.factories;

import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplTableModel;

/**
 * Factory for language implementation table models.
 */
public interface ILanguageImplTableModelFactory {

    /**
     * Creates a language implementation table model.
     *
     * @param state The module configuration state.
     * @return The table model.
     */
    @NotNull
    LanguageImplTableModel create(@NotNull final ModuleConfigurationState state);
}
