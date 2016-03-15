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

package org.metaborg.intellij.idea.gui.languagespanel;

import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.graphics.*;

import javax.annotation.*;
import javax.swing.*;

/**
 * A language node.
 */
public final class LanguageNode extends TreeNodeWithValue<ILanguage, LanguageNode>
        implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguage> {

    private final IIconManager iconManager;

    /**
     * Initializes a new instance of the {@link LanguageNode} class.
     *
     * @param language The language.
     * @param iconManager The icon manager.
     */
    public LanguageNode(@Nullable final ILanguage language, final IIconManager iconManager) {
        super(language);

        this.iconManager = iconManager;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getValueOfColumn(final ModelColumnInfo<LanguageNode> column) {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return this.iconManager.getLanguageFileIcon(this.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getName() {
        if (getValue() != null)
            return getValue().name();
        else
            return "Unknown";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getGroupId() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageVersion getVersion() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageStatus getStatus() {
        return null;
    }
}