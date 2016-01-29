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

package org.metaborg.intellij.idea.gui.wizards;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.UnhandledException;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.language.LanguageVersion;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxIcons;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxModuleBuilder;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.regex.Pattern;

/**
 * Wizard step for creating a Metaborg language project.
 */
public final class MetaborgModuleWizardStep extends ModuleWizardStep {

    private JTextField txtName;
    private JTextField txtGroupId;
    private JTextField txtArtifactId;
    private JTextField txtVersion;
    private JPanel mainPanel;
    private JTextField txtExtension;

    private final SpoofaxModuleBuilder builder;
    private final WizardContext context;
    private boolean isArtifactIdChangedByUser = false;
    private boolean isExtensionChangedByUser = false;

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
        return SpoofaxIcons.INSTANCE.defaultIcon();
    }

    /**
     * Initializes a new instance of the {@link MetaborgModuleWizardStep} class.
     *
     * @param builder The {@link ModuleBuilder}.
     * @param context The {@link WizardContext}.
     */
    public MetaborgModuleWizardStep(
            @NotNull final SpoofaxModuleBuilder builder,
            @NotNull final WizardContext context) {
        super();

        this.builder = builder;
        this.context = context;

        initComponents();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() throws ConfigurationException {
        validateLanguageIdentifier("Name", this.txtName.getText());
        validateExtension("File Extension", this.txtExtension.getText());
        validateLanguageIdentifier("Group ID", this.txtGroupId.getText());
        validateLanguageIdentifier("Artifact ID", this.txtArtifactId.getText());
        validateLanguageVersion("Version", this.txtVersion.getText());

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
        if (StringUtil.isEmptyOrSpaces(text))
            throw new ConfigurationException("Please specify a " + fieldName + ".");
        if (!LanguageIdentifier.validId(text))
            throw new ConfigurationException("Please specify a valid " + fieldName + "; " +
                                                     LanguageIdentifier.errorDescription);
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
        if (StringUtil.isEmptyOrSpaces(text))
            throw new ConfigurationException("Please specify a " + fieldName + ".");
        if (!LanguageVersion.valid(text))
            throw new ConfigurationException("Please specify a valid " + fieldName + "; " +
                                                     LanguageVersion.errorDescription);
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
        if (StringUtil.isEmptyOrSpaces(text))
            throw new ConfigurationException("Please specify a " + fieldName + ".");
        if (!isValidExtension(text))
            throw new ConfigurationException("Please specify a valid " + fieldName +
                                                     "; only alphanumeric characters and dot.");
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
        if (text.startsWith(".")) {
            // The extension may optionally start with a dot.
            // Just ignore it.
            text = text.substring(1);
        }
        if (text.startsWith(".") || text.endsWith(".")) {
            // An extension cannot start or end with a dot.
            return false;
        }
        if (text.contains("src/test")) {
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
                this.txtGroupId.getText(),
                this.txtArtifactId.getText(),
                LanguageVersion.parse(this.txtVersion.getText())
        ));
        this.builder.setExtension(cleanupExtension(this.txtExtension.getText()));
        this.context.setProjectName(this.builder.getName());
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
        this.txtName.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(final DocumentEvent e) {
                if (!MetaborgModuleWizardStep.this.isArtifactIdChangedByUser) {
                    MetaborgModuleWizardStep.this.txtArtifactId.setText(toArtifactId(getDocumentText(e.getDocument())));
                }
                if (!MetaborgModuleWizardStep.this.isExtensionChangedByUser) {
                    MetaborgModuleWizardStep.this.txtExtension.setText(toExtension(getDocumentText(e.getDocument())));
                }
            }
        });

        this.txtArtifactId.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(final DocumentEvent e) {
                MetaborgModuleWizardStep.this.isArtifactIdChangedByUser =
                        !getDocumentText(e.getDocument()).equals(
                                toArtifactId(MetaborgModuleWizardStep.this.txtName.getText()));
            }
        });

        this.txtExtension.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(final DocumentEvent e) {
                MetaborgModuleWizardStep.this.isExtensionChangedByUser =
                        !getDocumentText(e.getDocument()).equals(
                                toExtension(MetaborgModuleWizardStep.this.txtName.getText()));
            }
        });

    }

    /**
     * Gets the text in a document.
     *
     * @param doc The document.
     * @return The document text.
     */
    private static String getDocumentText(final Document doc) {
        try {
            return doc.getText(0, doc.getLength());
        } catch (final BadLocationException ex) {
            throw new UnhandledException(ex);
        }
    }

    /**
     * Turns any string into a valid identifier.
     *
     * @param input The input string.
     * @return The identifier.
     */
    private static String toIdentifier(final String input) {
        String id = input
                .trim()
                .replaceAll("[^A-Za-z0-9]", "-")
                .replaceAll("-+", "-");
        if (id.startsWith("-"))
            id = id.substring(1, id.length() - 1);
        if (id.endsWith("-"))
            id = id.substring(0, id.length() - 1);
        return id;
    }

    /**
     * Turns any string into a valid artifact ID.
     *
     * @param input The input string.
     * @return The artifact ID.
     */
    private static String toArtifactId(final String input) {
        return toIdentifier(input).toLowerCase();
    }

    /**
     * Turns any string into an extension.
     *
     * @param input The input string.
     * @return The extension, max 8 characters.
     */
    private static String toExtension(String input) {
        if (input == null)
            return "";
        input = input.trim();
        input = input.replaceAll("[^A-Za-z0-9]", "");
        if (input.isEmpty())
            return "";
        // NOTE: We keep the first alphanumeric character, even if it's lowercase.
        String ext = input.substring(0, 1) + input.substring(1).replaceAll("[^A-Z0-9]", "");
        ext = ext.toLowerCase();
        if (ext.length() > 8)
            ext = ext.substring(0, 8);
        return ext;
    }

    /**
     * Updates the component according to the data in
     * the {@link ModuleBuilder} and {@link WizardContext}.
     */
    private void updateComponents() {
        final String name = this.context.getProjectName() != null ? this.context.getProjectName() : this.builder.getName();
        this.txtName.setText(name);
        this.txtExtension.setText(toExtension(name));
        this.txtGroupId.setText(this.builder.getLanguageIdentifier().groupId);
        this.txtArtifactId.setText(toArtifactId(name));
        this.txtVersion.setText(this.builder.getLanguageIdentifier().version.toString());
    }

}
