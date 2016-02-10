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

package org.metaborg.intellij.idea.gui;

import com.google.inject.Inject;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.metaborg.util.log.ILogger;

import javax.swing.*;

@State(
        name = MetaborgProjectLanguagesConfiguration.NAME,
        storages = {
                @Storage(file = StoragePathMacros.PROJECT_FILE),
                @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/" + MetaborgProjectLanguagesConfiguration.CONFIG_FILE, scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public final class MetaborgProjectLanguagesConfiguration extends LanguagesConfiguration {

    public static final String NAME = "LanguagesConfiguration";
    public static final String CONFIG_FILE = "Languages.xml";

    @Nullable private MetaborgProjectLanguagesConfigurableForm form;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public MetaborgProjectLanguagesConfiguration(final Project project) {
        super(project);
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Override
    @Inject
    protected void inject(final LanguageManager languageManager, final ILanguageService languageService,
                          final IIdeaLanguageManager ideaLanguageManager) {
        super.inject(languageManager, languageService, ideaLanguageManager);
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

    @Override
    protected void updateLanguagesList() {
        this.form.getLanguagesPanel().setLanguages(getLanguages());
    }
//
//    @Nullable
//    @Override
//    public LanguagesConfiguration getState() {
//        throw new UnsupportedOperationException();
////        logger.error("STATE getting!");
////        return null;
//    }
//
//    @Override
//    public void loadState(final LanguagesConfiguration state) {
//        throw new UnsupportedOperationException();
////        logger.error("STATE loading!");
//    }
}
