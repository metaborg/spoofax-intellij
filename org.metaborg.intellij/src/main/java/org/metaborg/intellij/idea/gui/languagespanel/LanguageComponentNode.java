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

public class LanguageComponentNode extends TreeNodeWithValue<ILanguageComponent, LanguageComponentNode>
        implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguageComponent>  {

    public LanguageComponentNode(final ILanguageComponent languageComponent) {
        super(languageComponent);
    }

    public LanguageComponentNode(final LanguageIdentifier id) {
        super(null);
    }

    public ILanguageComponent getComponent() {
        @Nullable final ILanguageComponent component = this.getValue();
        assert component != null;
        return component;
    }

    @Nullable
    @Override
    public Object getValueOfColumn(final ModelColumnInfo<LanguageComponentNode> column) {
        return this.getComponent().id();
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }


    @Nullable
    @Override
    public String getName() {
        return this.getComponent().id().id;
    }

    @Override
    @Nullable
    public String getGroupId() {
        return this.getComponent().id().groupId;
    }

    @Override
    @Nullable
    public LanguageVersion getVersion() {
        return this.getComponent().id().version;
    }

    @Override
    @Nullable
    public LanguageStatus getStatus() {
        return LanguageStatus.Loaded;
    }
}