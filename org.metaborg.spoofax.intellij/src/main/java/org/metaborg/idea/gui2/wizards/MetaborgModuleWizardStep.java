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

package org.metaborg.idea.gui2.wizards;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.language.LanguageVersion;
import org.metaborg.idea.gui2.LanguageIdentifierVerifier;
import org.metaborg.idea.gui2.LanguageVersionVerifier;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxIcons;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxModuleBuilder;

import javax.swing.*;

/**
 * Wizard step for creating a Metaborg language project.
 */
public final class MetaborgModuleWizardStep extends ModuleWizardStep {

    private JTextField txtName;
    private JTextField txtGroupId;
    private JTextField txtArtifactId;
    private JTextField txtVersion;
    private JPanel mainPanel;

    private final SpoofaxModuleBuilder builder;
    private final WizardContext context;

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getComponent() {
        return this.mainPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getPreferredFocusedComponent() {
        return this.txtGroupId;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    /**
     * Initializes a new instance of the {@link MetaborgModuleWizardStep} class.
     *
     * @param builder The {@link ModuleBuilder}.
     * @param context The {@link WizardContext}.
     */
    public MetaborgModuleWizardStep(@NotNull final SpoofaxModuleBuilder builder,
                                    @NotNull final WizardContext context) {

        this.builder = builder;
        this.context = context;

        initComponents();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() throws ConfigurationException {
        validateLanguageIdentifier("Name", txtName.getText());
        validateLanguageIdentifier("Group ID", txtGroupId.getText());
        validateLanguageIdentifier("Artifact ID", txtArtifactId.getText());
        validateLanguageVersion("Version", txtVersion.getText());

        return super.validate();
    }

    /**
     * Validates that a language identifier is not empty and valid.
     *
     * @param fieldName The human-readable name of the field. Usually the label.
     * @param text The text to validate.
     * @return Always <code>true</code>.
     * @throws ConfigurationException Validation failed.
     */
    private boolean validateLanguageIdentifier(String fieldName, String text) throws ConfigurationException {
        if (StringUtil.isEmptyOrSpaces(text))
            throw new ConfigurationException("Please specify a " + fieldName + ".");
        if (!LanguageIdentifier.validId(text))
            throw new ConfigurationException("Please specify a valid " + fieldName + "; " + LanguageIdentifier.errorDescription);
        return true;
    }

    /**
     * Validates that a language version is not empty and valid.
     *
     * @param fieldName The human-readable name of the field. Usually the label.
     * @param text The text to validate.
     * @return Always <code>true</code>.
     * @throws ConfigurationException Validation failed.
     */
    private boolean validateLanguageVersion(String fieldName, String text) throws ConfigurationException {
        if (StringUtil.isEmptyOrSpaces(text))
            throw new ConfigurationException("Please specify a " + fieldName + ".");
        if (!LanguageVersion.valid(text))
            throw new ConfigurationException("Please specify a valid " + fieldName + "; " + LanguageVersion.errorDescription);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStep() {
        updateComponents();
    }

    /**
     * Commits the data to the {@link ModuleBuilder} and {@link WizardContext}.
     */
    @Override
    public void updateDataModel() {

        this.builder.setName(this.txtName.getText());
        this.builder.setLanguageIdentifier(new LanguageIdentifier(
                this.txtGroupId.getText(),
                this.txtArtifactId.getText(),
                LanguageVersion.parse(this.txtVersion.getText())));


        if (this.context.getProjectName() == null) {
            this.context.setProjectName(this.builder.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeUIResources() {

    }

    /**
     * Initializes the components.
     */
    private void initComponents() {
//        this.txtName.setInputVerifier(new LanguageIdentifierVerifier());
//        this.txtGroupId.setInputVerifier(new LanguageIdentifierVerifier());
//        this.txtArtifactId.setInputVerifier(new LanguageIdentifierVerifier());
//        this.txtVersion.setInputVerifier(new LanguageVersionVerifier());
    }

    /**
     * Updates the component according to the data in
     * the {@link ModuleBuilder} and {@link WizardContext}.
     */
    private void updateComponents() {

    }

}
