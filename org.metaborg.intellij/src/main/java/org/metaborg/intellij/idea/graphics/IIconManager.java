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

package org.metaborg.intellij.idea.graphics;

import org.metaborg.core.language.*;

import javax.annotation.*;
import javax.swing.*;

/**
 * Manages the icons for file types and other things.
 */
public interface IIconManager {

    /**
     * Gets the default icon.
     *
     * @return The default icon.
     */
    Icon getDefaultIcon();

    /**
     * Gets the icon for a file written in a specific language.
     *
     * @param language The language of the file for which to get the icon;
     *                 or <code>null</code> to get the default language icon.
     * @return The file icon.
     */
    Icon getLanguageFileIcon(@Nullable ILanguage language);

    /**
     * Gets the icon for a language artifact.
     *
     * @return The file icon.
     */
    Icon getLanguageArtifactIcon();

    /**
     * Gets the icon for the Metaborg facet.
     *
     * @return The facet icon.
     */
    Icon getFacetIcon();

    /**
     * Gets the icon for the Metaborg SDK.
     *
     * @return The SDK icon.
     */
    Icon getSdkIcon();

    /**
     * Gets the icon for the Metaborg SDK's Add Action.
     *
     * @return The SDK action icon.
     */
    Icon getSdkIconForAddAction();
}
