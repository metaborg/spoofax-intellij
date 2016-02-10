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

import com.google.inject.*;
import com.intellij.icons.*;
import com.intellij.openapi.util.*;
import org.metaborg.core.language.*;

import javax.swing.*;

/**
 * Default implementation of the {@link IIconManager} interface.
 */
@Singleton
public final class DefaultIconManager implements IIconManager {

    private final Icon defaultIcon;
    private final Icon languageArtifactIcon;

    /**
     * Initializes a new instance of the {@link DefaultIconManager} class.
     */
    public DefaultIconManager() {
        this.defaultIcon = IconLoader.getIcon("/SpoofaxDefault.png");
        this.languageArtifactIcon = AllIcons.FileTypes.Archive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getDefaultIcon() {
        return this.defaultIcon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getLanguageFileIcon(final ILanguage language) {
        // TODO: Get icon from ILanguage facet, otherwise use default.
        return getDefaultIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getLanguageArtifactIcon() {
        return this.languageArtifactIcon;
    }
}
