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

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.*;
import com.intellij.ui.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.meta.core.wizard.*;
import org.metaborg.spoofax.meta.core.wizard.*;
import org.metaborg.util.log.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.regex.*;

/**
 * Wizard step for creating a Metaborg language project.
 */
public final class MetaborgNewModuleWizardStep extends ModuleWizardStep {

    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtGroupId;
    private JTextField txtArtifactId;
    private JTextField txtVersion;
    private JTextField txtExtension;

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
     * Initializes a new instance of the {@link MetaborgNewModuleWizardStep} class.
     *
     * @param builder The {@link ModuleBuilder}.
     * @param context The {@link WizardContext}.
     * @param iconManager The icon manager.
     */
    @Inject
    public MetaborgNewModuleWizardStep(
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
                if (!MetaborgNewModuleWizardStep.this.isArtifactIdChangedByUser) {
                    MetaborgNewModuleWizardStep.this.txtArtifactId
                            .setText(toArtifactId(getDocumentText(e.getDocument())));
                }
                if (!MetaborgNewModuleWizardStep.this.isExtensionChangedByUser) {
                    MetaborgNewModuleWizardStep.this.txtExtension
                            .setText(toExtension(getDocumentText(e.getDocument())));
                }
            }
        });

        this.txtArtifactId.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(final DocumentEvent e) {
                MetaborgNewModuleWizardStep.this.isArtifactIdChangedByUser =
                        !getDocumentText(e.getDocument()).equals(
                                toArtifactId(MetaborgNewModuleWizardStep.this.txtName.getText()));
            }
        });

        this.txtExtension.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(final DocumentEvent e) {
                MetaborgNewModuleWizardStep.this.isExtensionChangedByUser =
                        !getDocumentText(e.getDocument()).equals(
                                toExtension(MetaborgNewModuleWizardStep.this.txtName.getText()));
            }
        });

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
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
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
    private static String toExtension(@Nullable String input) {
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
        this.txtExtension.setText(toExtension(name));
        this.txtGroupId.setText(this.builder.getLanguageIdentifier().groupId);
        this.txtArtifactId.setText(toArtifactId(name));
        this.txtVersion.setText(this.builder.getLanguageIdentifier().version.toString());
    }

}
