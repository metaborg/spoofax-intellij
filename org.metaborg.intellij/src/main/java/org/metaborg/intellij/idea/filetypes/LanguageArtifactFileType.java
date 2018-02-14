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

import com.google.inject.Inject;
import com.intellij.ide.highlighter.ArchiveFileType;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.idea.graphics.IIconManager;

import javax.swing.*;
import java.util.Collections;

/**
 * The file type for Spoofax artifacts.
 */
@Deprecated
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
