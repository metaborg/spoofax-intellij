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

package org.metaborg.intellij.idea.filetypes;


import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.graphics.IIconManager;
import org.metaborg.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.intellij.languages.LanguageUtils2;
import org.metaborg.intellij.idea.languages.MetaborgIdeaLanguage;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import javax.swing.*;

// TODO: Generalize for Metaborg languages.

/**
 * A Spoofax language file type.
 * <p>
 * There are no implementations of this class because it's instantiated dynamically.
 */
public abstract class MetaborgLanguageFileType extends LanguageFileType implements IMetaborgFileType {

    private IIconManager iconManager;
    private IIdeaLanguageManager languageManager;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by the proxy system.
     * Do not call this constructor manually.
     *
     * @param language The language.
     */
    protected MetaborgLanguageFileType(final MetaborgIdeaLanguage language) {
        super(language);
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @jakarta.inject.Inject
    @SuppressWarnings("unused")
    private void inject(final IIconManager iconManager, final IIdeaLanguageManager languageManager) {
        this.iconManager = iconManager;
        this.languageManager = languageManager;
    }

    /**
     * Gets the name of the file type.
     *
     * @return The name.
     */
    @Override
    public String getName() {
        return this.getMetaborgLanguage().name();
    }

    /**
     * Gets the Metaborg language.
     *
     * @return The Metaborg language.
     */
    public final ILanguage getMetaborgLanguage() {
        return this.languageManager.getLanguage((MetaborgIdeaLanguage)super.getLanguage());
    }

    /**
     * Gets the description of the file type.
     * <p>
     * This is shown in the <em>File types</em> settings dialog. If multiple file types have the same description,
     * the result of {@link #getName} is appended to the description.
     *
     * @return The description.
     */
    @Override
    public String getDescription() {
        return this.logger.format("{} (Spoofax)", this.getMetaborgLanguage().name());
    }

    /**
     * Gets the default extension of the file type.
     *
     * @return The default extension.
     */
    @Override
    public String getDefaultExtension() {
        return LanguageUtils2.getDefaultExtension(this.getMetaborgLanguage());
    }

    /**
     * Gets the icon of the file type.
     *
     * @return The icon.
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return this.iconManager.getLanguageFileIcon(this.getMetaborgLanguage());
    }

    /**
     * Gets the extensions recognized for this file type.
     *
     * @return A set of file extensions.
     */
    @Override
    public Iterable<String> getExtensions() {
        return LanguageUtils2.getExtensions(this.getMetaborgLanguage());
    }

}
