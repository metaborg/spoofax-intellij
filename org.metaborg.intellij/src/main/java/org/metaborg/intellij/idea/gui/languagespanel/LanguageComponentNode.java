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

import javax.annotation.*;
import javax.swing.*;

/**
 * A language component node.
 */
public final class LanguageComponentNode extends TreeNodeWithValue<ILanguageComponent, LanguageComponentNode>
        implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguageComponent>  {

    /**
     * Initializes a new instance of this node.
     *
     * @param languageComponent The language component.
     */
    public LanguageComponentNode(final ILanguageComponent languageComponent) {
        super(languageComponent);
    }

    /**
     * Gets the language component.
     *
     * @return The language component.
     */
    public ILanguageComponent getComponent() {
        @Nullable final ILanguageComponent component = this.getValue();
        assert component != null;
        return component;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getValueOfColumn(final ModelColumnInfo<LanguageComponentNode> column) {
        return this.getComponent().id();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getName() {
        return this.getComponent().id().id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getGroupId() {
        return this.getComponent().id().groupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageVersion getVersion() {
        return this.getComponent().id().version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageStatus getStatus() {
        return LanguageStatus.Loaded;
    }
}