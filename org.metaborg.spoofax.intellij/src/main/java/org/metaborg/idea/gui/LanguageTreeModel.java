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

package org.metaborg.idea.gui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeColumnInfo;
import com.intellij.util.ui.ColumnInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxIcons;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * Tree model for languages and their implementation.
 */
public final class LanguageTreeModel extends ListTreeTableModelOnColumns {

    private static final ColumnInfo[] COLUMNS = ArrayUtils.toArray(
            new TreeModelColumnInfo("Name")
    );

    public LanguageTreeModel() {
        super(new DefaultMutableTreeNode(""), COLUMNS);
    }

    @Override
    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode)super.getRoot();
    }

    /**
     * Appends a node to a parent.
     *
     * @param newChild The new child node.
     * @param parent The parent node.
     */
    public void appendNodeInto(@Nullable MutableTreeNode newChild, MutableTreeNode parent) {
        insertNodeInto(newChild, parent, parent.getChildCount());
    }

    public static class LanguageNode extends TreeNodeWithValue<ILanguage, LanguageNode> implements ITreeNodeWithIcon {
        public LanguageNode(ILanguage language) {
            super(language);
        }

        @Nullable
        @Override
        public Object getValueOfColumn(final ModelColumnInfo<LanguageNode> column) {
            return getValue().name();
        }

        @Nullable
        @Override
        public Icon getIcon() {
            return SpoofaxIcons.INSTANCE.Default;
        }
    }

    public static class LanguageImplNode extends TreeNodeWithValue<ILanguageImpl, LanguageImplNode> implements ITreeNodeWithIcon {
        public LanguageImplNode(ILanguageImpl language) {
            super(language);
        }

        @Nullable
        @Override
        public Object getValueOfColumn(final ModelColumnInfo<LanguageImplNode> column) {
            return getValue().id();
        }

        @Nullable
        @Override
        public Icon getIcon() {
            return null;
        }
    }
}
