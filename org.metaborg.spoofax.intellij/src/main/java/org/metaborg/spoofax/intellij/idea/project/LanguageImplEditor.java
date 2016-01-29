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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.roots.ui.configuration.ModuleElementsEditor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.spoofax.intellij.factories.ILanguageImplPanelFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Wizard page for editing language implementations.
 */
public final class LanguageImplEditor extends ModuleElementsEditor {

    private static final String NAME = "Languages";

    @NotNull
    private final ILanguageImplPanelFactory languageImplPanelFactory;
    @Nullable
    private LanguageImplPanel panel;

    @Inject
    /* package private */ LanguageImplEditor(
            @Assisted @NotNull final ModuleConfigurationState state,
            @NotNull final ILanguageImplPanelFactory languageImplPanelFactory) {
        super(state);
        this.languageImplPanelFactory = languageImplPanelFactory;
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }

    @Override
    public void canApply() throws ConfigurationException {

    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void moduleStateChanged() {
        if (this.panel == null)
            return;
        //this.panel.initFromModel();
    }

    @Override
    protected JComponent createComponentImpl() {
        this.panel = this.languageImplPanelFactory.create(getState());
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        mainPanel.add(this.panel, BorderLayout.CENTER);
        return mainPanel;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return NAME;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public void saveData() {
        //this.panel.stopEditing();
    }
}
