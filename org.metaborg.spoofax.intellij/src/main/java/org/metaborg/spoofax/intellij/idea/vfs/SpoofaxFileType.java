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

package org.metaborg.spoofax.intellij.idea.vfs;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.intellij.idea.vfs.MetaborgFileType;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxIdeaLanguage;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxIcons;
import org.metaborg.spoofax.intellij.languages.LanguageUtils;
import org.metaborg.util.log.ILogger;

import javax.swing.*;

// TODO: Generalize for Metaborg languages.

/**
 * A Spoofax language file type.
 * <p>
 * There are no implementations of this class because it's instantiated dynamically.
 */
public abstract class SpoofaxFileType extends LanguageFileType implements MetaborgFileType {

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link SpoofaxFileType} class.
     *
     * @param language The language.
     */
    protected SpoofaxFileType(final SpoofaxIdeaLanguage language) {
        super(language);
    }

    /**
     * Gets the name of the file type.
     *
     * @return The name.
     */
    @Override
    public String getName() {
        return this.getSpoofaxLanguage().name();
    }

    /**
     * Gets the Spoofax language.
     *
     * @return The Spoofax language.
     */
    public final ILanguage getSpoofaxLanguage() {
        return ((SpoofaxIdeaLanguage)super.getLanguage()).language();
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
        return this.logger.format("{} (Spoofax)", this.getSpoofaxLanguage().name());
    }

    /**
     * Gets the default extension of the file type.
     *
     * @return The default extension.
     */
    @Override
    public String getDefaultExtension() {
        return LanguageUtils.getDefaultExtension(this.getSpoofaxLanguage());
    }

    /**
     * Gets the icon of the file type.
     *
     * @return The icon.
     */
    @Nullable
    @Override
    public Icon getIcon() {
        // TODO: Get icon from ILanguage facet, otherwise use default.
        return SpoofaxIcons.INSTANCE.defaultIcon();
    }

    /**
     * Gets the extensions recognized for this file type.
     *
     * @return A set of file extensions.
     */
    @Override
    public Iterable<String> getExtensions() {
        return LanguageUtils.getExtensions(this.getSpoofaxLanguage());
    }

}
