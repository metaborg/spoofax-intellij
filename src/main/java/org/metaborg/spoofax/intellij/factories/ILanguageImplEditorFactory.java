package org.metaborg.spoofax.intellij.factories;

import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplEditor;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplPanel;

/**
 * Factory for language implementation editor.
 */
public interface ILanguageImplEditorFactory {

    /**
     * Creates a language implementation editor.
     *
     * @param state          The module configuration state.
     * @return The editor.
     */
    @NotNull
    LanguageImplEditor create(@NotNull final ModuleConfigurationState state);
}
