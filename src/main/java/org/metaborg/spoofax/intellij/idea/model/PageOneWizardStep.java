package org.metaborg.spoofax.intellij.idea.model;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class PageOneWizardStep extends ModuleWizardStep {

    @NotNull
    private final PageOne step;

    public PageOneWizardStep() {
        this.step = new PageOne();
    }

    @Override
    @NotNull public final JComponent getComponent() {
        return step.getPanel();
    }

    @Override
    public final void updateDataModel() {

    }
}
