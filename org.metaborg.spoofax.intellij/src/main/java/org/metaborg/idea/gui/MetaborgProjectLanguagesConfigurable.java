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

package org.metaborg.idea.gui;

import com.google.inject.Inject;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.idea.gui2.MetaborgProjectLanguagesConfigurableForm;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;

import javax.swing.*;

public final class MetaborgProjectLanguagesConfigurable extends LanguagesConfigurable {

    private MetaborgProjectLanguagesConfigurableForm form;
    private LanguagesPanel languagesPanel;
    private ILanguageService languageService;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public MetaborgProjectLanguagesConfigurable(Project project) {
        super(project);
        IdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(final ILanguageService languageService) {
        this.languageService = languageService;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Metaborg";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
//        this.languagesPanel = new LanguagesPanel();
//        this.languagesPanel.attachActions(this);
//        this.languagesPanel.myFileTypesList.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(@Nullable ListSelectionEvent e) {
//                updateExtensionList();
//            }
//        });
//        return myFileTypePanel.getComponent();
//
        if (this.form == null) {
            this.form = new MetaborgProjectLanguagesConfigurableForm();
        }
        return this.form.getComponent();
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {
        this.form.getLanguagesPanel().attachController(this);
        updateLanguagesList();
    }

    @Override
    public void disposeUIResources() {

    }

    @SuppressWarnings("unchecked")
    private void updateLanguagesList() {
        this.form.getLanguagesPanel().setLanguages((Iterable<ILanguageImpl>) this.languageService.getAllImpls());
    }
}
