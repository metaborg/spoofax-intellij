package org.metaborg.spoofax.intellij.factories;

import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplPanel;

/**
 * Factory for language implementation panel.
 */
public interface ILanguageImplPanelFactory {

    /**
     * Creates a language implementation panel.
     *
     * @param state The module configuration state.
     * @return The table panel.
     */
    @NotNull
    LanguageImplPanel create(@NotNull final ModuleConfigurationState state);
}
