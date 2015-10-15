package org.metaborg.spoofax.intellij.idea.project;

import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Editor for the settings of a Spoofax module.
 */
public final class SpoofaxModuleConfigurationEditor implements ModuleConfigurationEditor {

    private JButton button1;
    private JPanel mainPanel;

    /**
     * Initializes a new instance of the {@link SpoofaxModuleConfigurationEditor} class.
     *
     * @param state The configuration state.
     */
    public SpoofaxModuleConfigurationEditor(ModuleConfigurationState state) {

    }

    /**
     * Gets the title of the settings tab.
     *
     * @return The title of the settings tab.
     */
    @Nls
    @Override
    public String getDisplayName() {
        return "Languages";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        // Default implementation.
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return this.mainPanel;
    }

    @Override
    public boolean isModified() {
        // TODO: Return whether any of the settings was changed from their current value.
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        // TODO: Apply the new settings.
    }

    @Override
    public void reset() {
        // TODO: Reset the settings to their current value.
    }

    @Override
    public void saveData() {
        // Nothing to do.
    }

    @Override
    public void moduleStateChanged() {
        // Nothing to do.
    }

    @Override
    public void disposeUIResources() {
        // Nothing to do.
    }

}
