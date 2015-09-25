package org.metaborg.spoofax.intellij.idea.model;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;

import javax.swing.*;

public class PageOneWizardStep extends ModuleWizardStep {

    private final PageOne step;

    public PageOneWizardStep() {
        this.step = new PageOne();
    }

    @Override
    public JComponent getComponent() {
        return step.getPanel();
    }

    @Override
    public void updateDataModel() {

    }
}
