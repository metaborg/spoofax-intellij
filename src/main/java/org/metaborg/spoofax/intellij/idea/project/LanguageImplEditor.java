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
    private LanguageImplEditor(@Assisted @NotNull final ModuleConfigurationState state,
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
        JPanel mainPanel = new JPanel(new BorderLayout());
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
