/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    public void disposeUIResources() {
        // Nothing to do.
    }

    @Override
    public void saveData() {
        // Nothing to do.
    }

    @Override
    public void moduleStateChanged() {
        // Nothing to do.
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
