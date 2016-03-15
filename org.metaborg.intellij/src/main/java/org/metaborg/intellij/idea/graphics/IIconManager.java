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
