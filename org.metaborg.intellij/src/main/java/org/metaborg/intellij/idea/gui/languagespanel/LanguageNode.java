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