/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.projects.newproject;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.metaborg.core.language.*;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.idea.graphics.IIconManager;
import org.metaborg.intellij.idea.projects.MetaborgModuleBuilder;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.meta.core.wizard.*;
import org.metaborg.spoofax.meta.core.generator.general.*;
import org.metaborg.util.log.*;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.regex.Pattern;

public class NewModuleWizardStep extends ModuleWizardStep {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtExtensions;
    private JTextField txtGroupID;
    private JTextField txtID;
    private JTextField txtVersion;
    private JPanel pnlLanguageOptions;
    private JPanel pnlLanguageIdentification;

    private JComboBox cmbSyntaxType;
    private JComboBox cmbAnalysisType;
    private JComboBox cmbTransformationType;


    private final IIconManager iconManager;
    private final MetaborgModuleBuilder builder;
    private final WizardContext context;
    private boolean isArtifactIdChangedByUser = false;
    private boolean isExtensionChangedByUser = false;
    @InjectLogger
    private ILogger logger;

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
        return this.txtName;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return this.iconManager.getDefaultIcon();
    }

    /**
     * Initializes a new instance of the {@link NewModuleWizardStep} class.
     *
     * @param builder     The {@link ModuleBuilder}.
     * @param context     The {@link WizardContext}.
     * @param iconManager The icon manager.
     */
    @Inject
    public NewModuleWizardStep(
            @Assisted final MetaborgModuleBuilder builder,
            @Assisted final WizardContext context,
            final IIconManager iconManager) {

        this.builder = builder;
        this.context = context;
        this.iconManager = iconManager;

        initComponents();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() throws ConfigurationException {
        validateLanguageIdentifier("name", this.txtName.getText());
        validateExtension("file extension", this.txtExtensions.getText());
        validateLanguageIdentifier("group identifier", this.txtGroupID.getText());
        validateLanguageIdentifier("artifact identifier", this.txtID.getText());
        validateLanguageVersion("version", this.txtVersion.getText());

        return super.validate();
    }

    /**
     * Validates that a language identifier is not empty and valid.
     *
     * @param fieldName The human-readable name of the field. Usually the label.
     * @param text      The text to validate.
     * @return Always <code>true</code>.
     * @throws ConfigurationException Validation failed.
     */
    private boolean validateLanguageIdentifier(final String fieldName, final String text) throws
            ConfigurationException {
        if (StringUtil.isEmptyOrSpaces(text)) {
            throw new ConfigurationException("Please specify a " + fieldName + ".");
        }
        if (!LanguageIdentifier.validId(text)) {
            throw new ConfigurationException("Please specify a valid " + fieldName + "; " +
                    LanguageIdentifier.errorDescription);
        }
        return true;
    }

    /**
     * Validates that a language version is not empty and valid.
     *
     * @param fieldName The human-readable name of the field. Usually the label.
     * @param text      The text to validate.
     * @return Always <code>true</code>.
     * @throws ConfigurationException Validation failed.
     */
    private boolean validateLanguageVersion(final String fieldName, final String text) throws ConfigurationException {
        if (StringUtil.isEmptyOrSpaces(text)) {
            throw new ConfigurationException("Please specify a " + fieldName + ".");
        }
        return true;
    }

    /**
     * Validates that a file extension is not empty and valid.
     *
     * @param fieldName The human-readable name of the field. Usually the label.
     * @param text      The text to validate.
     * @return Always <code>true</code>.
     * @throws ConfigurationException Validation failed.
     */
    private boolean validateExtension(final String fieldName, final String text) throws ConfigurationException {
        if (StringUtil.isEmptyOrSpaces(text)) {
            throw new ConfigurationException("Please specify a " + fieldName + ".");
        }
        if (!isValidExtension(text)) {
            throw new ConfigurationException("Please specify a valid " + fieldName +
                    "; only alphanumeric characters and dot.");
        }
        return true;
    }

    /**
     * Determines whether an extension is valid.
     *
     * @param text The text to test.
     * @return <code>true</code> when it is a valid extension;
     * otherwise, <code>false</code>.
     */
    private static boolean isValidExtension(String text) {
        if (StringUtil.isEmptyOrSpaces(text)) {
            // An extension cannot be empty.
            return false;
        }
        if (text.startsWith(".")) {
            // The extension may optionally start with a dot.
            // Just ignore it.
            text = text.substring(1);
        }
        if (text.startsWith(".") || text.endsWith(".")) {
            // An extension cannot start or end with a dot.
            return false;
        }
        if (text.contains("..")) {
            // An extension cannot contain a sequence of dots.
            return false;
        }
        return !Pattern.compile("[^a-zA-Z0-9.]").matcher(text).find();
    }

    /**
     * Cleans a valid extension.
     *
     * @param extension The extension.
     * @return The cleaned extension, without the leading dot.
     */
    private static String cleanupExtension(String extension) {
        if (extension.startsWith(".")) {
            // The extension may optionally start with a dot.
            // Just remove it.
            extension = extension.substring(1);
        }
        return extension.toLowerCase();
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
                this.txtGroupID.getText(),
                this.txtID.getText(),
                LanguageVersion.parse(this.txtVersion.getText())
        ));
        this.builder.setExtensions(CreateLanguageSpecWizard.splitExtensions(cleanupExtension(this.txtExtensions.getText())));
        this.builder.setSyntaxType(SyntaxType.mapping().get((String) this.cmbSyntaxType.getSelectedItem()));
        this.builder.setAnalysisType(AnalysisType.mapping().get((String) this.cmbAnalysisType.getSelectedItem()));
        this.builder.setTransformationType(TransformationType.mapping().get((String) this.cmbTransformationType.getSelectedItem()));
        this.context.setProjectName(this.builder.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeUIResources() {

    }

    public void initComponents() {
        this.txtName.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(final DocumentEvent e) {
                if (!NewModuleWizardStep.this.isArtifactIdChangedByUser) {
                    NewModuleWizardStep.this.txtID
                            .setText(toArtifactId(getDocumentText(e.getDocument())));
                }
                if (!NewModuleWizardStep.this.isExtensionChangedByUser) {
                    NewModuleWizardStep.this.txtExtensions
                            .setText(toExtension(getDocumentText(e.getDocument())));
                }
            }
        });

        this.txtID.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(final DocumentEvent e) {
                NewModuleWizardStep.this.isArtifactIdChangedByUser =
                        !getDocumentText(e.getDocument()).equals(
                                toArtifactId(NewModuleWizardStep.this.txtName.getText()));
            }
        });

        this.txtExtensions.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(final DocumentEvent e) {
                NewModuleWizardStep.this.isExtensionChangedByUser =
                        !getDocumentText(e.getDocument()).equals(
                                toExtension(NewModuleWizardStep.this.txtName.getText()));
            }
        });

        addLanguageOptions();
    }

    public void addLanguageOptions() {
        final int rows = 3;
        this.pnlLanguageOptions.setLayout(new GridLayoutManager(rows, 2));

        this.cmbSyntaxType = new JComboBox(Lists.newArrayList(SyntaxType.mapping().keySet()).toArray());
        this.cmbAnalysisType = new JComboBox(Lists.newArrayList(AnalysisType.mapping().keySet()).toArray());
        this.cmbTransformationType = new JComboBox(Lists.newArrayList(TransformationType.mapping().keySet()).toArray());

        addLanguageOption(0, "Syntax type:", this.cmbSyntaxType);
        addLanguageOption(1, "Analysis type:", this.cmbAnalysisType);
        addLanguageOption(2, "Transformation type:", this.cmbTransformationType);
    }

    private void addLanguageOption(final int row, final String label, @Nullable final Component component) {
        this.pnlLanguageOptions.add(new JLabel(label), new GridConstraints(row, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
        if (component != null) {
            this.pnlLanguageOptions.add(component, new GridConstraints(row, 1, 1, 1, 8, 1, 2, 0, null, null, null, 0, false));
        }
    }

    /**
     * Gets the text in a document.
     *
     * @param doc The document.
     * @return The document text.
     */
    private String getDocumentText(final Document doc) {
        try {
            return doc.getText(0, doc.getLength());
        } catch (final BadLocationException ex) {
            throw LoggerUtils2.exception(this.logger, UnhandledException.class,
                    "An unexpected unhandled exception.", ex);
        }
    }

    /**
     * Turns any string into a valid artifact ID.
     *
     * @param input The input string.
     * @return The artifact ID.
     */
    private static String toArtifactId(final String input) {
        return CreateLanguageSpecWizard.toId(input);
    }

    /**
     * Turns any string into an extension.
     *
     * @param input The input string.
     * @return The extension, max 8 characters.
     */
    private static String toExtension(@Nullable final String input) {
        return CreateLanguageSpecWizard.toExtension(input);
    }

    /**
     * Updates the component according to the data in
     * the {@link ModuleBuilder} and {@link WizardContext}.
     */
    private void updateComponents() {
        final String name = this.context.getProjectName() != null ?
                this.context.getProjectName() : this.builder.getName();
        this.txtName.setText(name);
        this.txtExtensions.setText(toExtension(name));
        this.txtGroupID.setText(this.builder.getLanguageIdentifier().groupId);
        this.txtID.setText(toArtifactId(name));
        this.txtVersion.setText(this.builder.getLanguageIdentifier().version.toString());
        this.cmbSyntaxType.setSelectedItem(this.builder.getSyntaxType().name);
        this.cmbAnalysisType.setSelectedItem(this.builder.getAnalysisType().name);
        this.cmbTransformationType.setSelectedItem(this.builder.getTransformationType().name);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        pnlLanguageIdentification = new JPanel();
        pnlLanguageIdentification.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        pnlLanguageIdentification.putClientProperty("BorderFactoryClass", "com.intellij.ui.IdeBorderFactory$PlainSmallWithIndent");
        mainPanel.add(pnlLanguageIdentification, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pnlLanguageIdentification.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Language identification"));
        final JLabel label1 = new JLabel();
        label1.setText("Name:");
        pnlLanguageIdentification.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Identifier:");
        pnlLanguageIdentification.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Version:");
        pnlLanguageIdentification.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Extensions:");
        pnlLanguageIdentification.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Group identifier:");
        pnlLanguageIdentification.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtName = new JTextField();
        pnlLanguageIdentification.add(txtName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtExtensions = new JTextField();
        pnlLanguageIdentification.add(txtExtensions, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtGroupID = new JTextField();
        pnlLanguageIdentification.add(txtGroupID, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtID = new JTextField();
        pnlLanguageIdentification.add(txtID, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtVersion = new JTextField();
        pnlLanguageIdentification.add(txtVersion, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pnlLanguageOptions = new JPanel();
        pnlLanguageOptions.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        pnlLanguageOptions.putClientProperty("BorderFactoryClass", "com.intellij.ui.IdeBorderFactory$PlainSmallWithIndent");
        mainPanel.add(pnlLanguageOptions, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pnlLanguageOptions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Language options"));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
