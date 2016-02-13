/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.idea.filetypes;

import com.google.inject.*;
import com.intellij.openapi.fileTypes.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.languages.*;
import org.metaborg.intellij.logging.*;
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

    @Inject
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
