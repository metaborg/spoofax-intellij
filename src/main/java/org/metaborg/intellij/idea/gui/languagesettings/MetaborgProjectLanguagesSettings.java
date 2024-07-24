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

package org.metaborg.intellij.idea.gui.languagesettings;


import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;

import javax.swing.*;

/**
 * Metaborg language configuration dialog.
 */
public final class MetaborgProjectLanguagesSettings extends LanguagesSettings {

    private MetaborgProjectLanguagesConfigurableForm form;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgProjectLanguagesSettings(final Project project) {
        super(project);
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @jakarta.inject.Inject
    @SuppressWarnings("unused")
    protected void inject() {
    }

    /**
     * {@inheritDoc}
     */
    @Nls
    @Override
    public String getDisplayName() {
        return "Metaborg Languages";
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        if (this.form == null) {
            this.form = new MetaborgProjectLanguagesConfigurableForm();
        }
        return this.form.getComponent();
    }


    /**
     * Called on form load and Cancel.
     */
    @Override
    public void reset() {
        super.reset();
        if (this.form == null)
            return;
        this.form.getLanguagesPanel().attachController(this);
        updateLanguagesList();
    }

    /**
     * Called on form unload.
     */
    @Override
    public void disposeUIResources() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateLanguagesList() {
        if (this.form == null)
            return;
        this.form.getLanguagesPanel().setLanguages(getLanguages());
    }

}
