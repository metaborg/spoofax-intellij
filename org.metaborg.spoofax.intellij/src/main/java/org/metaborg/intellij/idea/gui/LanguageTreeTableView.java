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

package org.metaborg.intellij.idea.gui;

import com.intellij.ui.TreeTableSpeedSearch;
import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Tree table with languages and their implementations.
 */
public final class LanguageTreeTableView extends TreeTable {
    private static final ColumnInfo LANGUAGE = new ColumnInfo("Language") {

        @Nullable
        @Override
        public Object valueOf(final Object obj) {
            if (obj instanceof LanguageTreeModel.LanguageNode) {
                final LanguageTreeModel.LanguageNode node = (LanguageTreeModel.LanguageNode)obj;
                return node.getValue().name();
            }
            if (obj instanceof LanguageTreeModel.LanguageImplNode) {
                final LanguageTreeModel.LanguageImplNode node = (LanguageTreeModel.LanguageImplNode)obj;
                return node.getValue().id();
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<?> getColumnClass() {
            return TreeTableModel.class;
        }

        @Override
        public boolean isCellEditable(final Object o) {
            return false;
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(final Object o) {
            return new DefaultTableCellRenderer();
        }
    };

    private static final ColumnInfo VERSION = new ColumnInfo("Version") {

        @Nullable
        @Override
        public Object valueOf(final Object obj) {
            if (obj instanceof LanguageTreeModel.LanguageNode) {
                return null;
            }
            if (obj instanceof LanguageTreeModel.LanguageImplNode) {
                final LanguageTreeModel.LanguageImplNode node = (LanguageTreeModel.LanguageImplNode)obj;
                return node.getValue().id().version;
            }
            throw new UnsupportedOperationException();
        }
    };

    private final ColumnInfo[] columns;

    public LanguageTreeTableView(final TreeNode rootNode) {
        this(rootNode, new ColumnInfo[]{LANGUAGE, VERSION});
    }

    private LanguageTreeTableView(final TreeNode rootNode, final ColumnInfo[] columns) {
        super(new LanguageTreeModel(rootNode, columns));

        this.columns = columns;

        final TreeTableTree tree = getTree();
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        UIUtil.setLineStyleAngled(tree);
        setTreeCellRenderer(new LanguageCellRenderer());

        TreeUtil.expand(tree, 1);

        new TreeTableSpeedSearch(this, path -> {
            final TreeNode obj = (TreeNode)path.getLastPathComponent();
            if (obj instanceof LanguageTreeModel.LanguageNode) {
                final LanguageTreeModel.LanguageNode node = (LanguageTreeModel.LanguageNode)obj;
                return node.getValue().name();
            }
            if (obj instanceof LanguageTreeModel.LanguageImplNode) {
                final LanguageTreeModel.LanguageImplNode node = (LanguageTreeModel.LanguageImplNode)obj;
                return node.getValue().id().toString();
            }
            return null;
        });
    }

    @Override
    public TableCellRenderer getCellRenderer(final int row, final int column) {
        final TreePath path = getTree().getPathForRow(row);
        TableCellRenderer renderer = null;
        if (path != null) {
            final TreeNode node = (TreeNode)path.getLastPathComponent();
            renderer = this.columns[column].getRenderer(node);
        }
        return renderer != null ? renderer : super.getCellRenderer(row, column);
    }

    @Override
    public TableCellEditor getCellEditor(final int row, final int column) {
        final TreePath path = getTree().getPathForRow(row);
        TableCellEditor editor = null;
        if (path != null) {
            final TreeNode node = (TreeNode)path.getLastPathComponent();
            editor = this.columns[column].getEditor(node);
        }
        return editor != null ? editor : super.getCellEditor(row, column);
    }
}
