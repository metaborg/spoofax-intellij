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
import com.intellij.ide.highlighter.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.idea.graphics.*;

import javax.swing.*;
import java.util.*;

/**
 * The file type for Spoofax artifacts.
 */
public final class LanguageArtifactFileType extends ArchiveFileType implements IMetaborgFileType {

    private static final String ID = "SPOOFAX_ARTIFACT";
    private static final String DESCRIPTION = "Spoofax artifact";
    private static final String EXTENSION = "spoofax-language";

    private final IIconManager iconManager;

    /**
     * Initializes a new instance of the {@link LanguageArtifactFileType} class.
     */
    @Inject
    public LanguageArtifactFileType(final IIconManager iconManager) {
        this.iconManager = iconManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultExtension() {
        return getExtensions().iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return this.iconManager.getLanguageArtifactIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<String> getExtensions() {
        return Collections.singletonList(EXTENSION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getDescription();
    }
}
