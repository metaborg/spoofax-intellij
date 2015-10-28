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