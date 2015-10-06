package org.metaborg.spoofax.intellij.idea.model;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.wizard.CommitStepException;
import com.intellij.openapi.Disposable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SpoofaxProjectWizardStep extends ModuleWizardStep implements Disposable {
    WizardContext context;
    private JLabel jLabelPerlIntro;
    private JComponent myMainPanel;

    public SpoofaxProjectWizardStep(WizardContext context) {
        this.context = context;
    }

    public void dispose() {
    }

    public JComponent getPreferredFocusedComponent() {
        return this.myMainPanel;
    }

    public JComponent getComponent() {
        if (myMainPanel == null) {
            myMainPanel = new JPanel();
            myMainPanel.setBorder(new TitledBorder("Spoofax"));
            myMainPanel.setPreferredSize(new Dimension(333, 364));
            jLabelPerlIntro = new JLabel();
            jLabelPerlIntro.setText("Hello!");
            Font labelFont = jLabelPerlIntro.getFont();
            jLabelPerlIntro.setFont(new Font(labelFont.getName(), 0, 14));
            jLabelPerlIntro.setIcon(SpoofaxIcons.INSTANCE.Default);
            myMainPanel.add(jLabelPerlIntro, null);

        }
        return this.myMainPanel;
    }

    public void updateDataModel() {
        //TODO:implement
    }

    public void onWizardFinished() throws CommitStepException {
    }
}