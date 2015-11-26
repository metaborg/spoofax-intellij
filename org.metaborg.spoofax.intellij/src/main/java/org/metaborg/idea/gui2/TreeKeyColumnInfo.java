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

package org.metaborg.idea.gui2;

import com.google.common.base.Preconditions;
import com.intellij.ui.treeStructure.treetable.TreeColumnInfo;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * Key column in a tree table view.
 */
public class TreeKeyColumnInfo<Item> extends TreeColumnInfo {

    /**
     * Initializes a new instance of the {@link TreeKeyColumnInfo} class.
     *
     * @param name The name of the column.
     */
    public TreeKeyColumnInfo(final String name) {
        super(name);
        Preconditions.checkNotNull(name);
    }

    /**
     * Gets the text to display for the specified item.
     *
     * Override this method to change the value displayed for the specified item.
     *
     * @param item The item.
     * @return The value to display.
     */
    public String getTextFor(Item item) {
        return item.toString();
    }

    /**
     * Gets the icon to display for the specified item.
     *
     * Override this method to change the icon displayed for the specified item.
     *
     * @param item The item.
     * @return The icon to display;
     * or <code>null</code> to display the default icon.
     */
    @Nullable
    public Icon getIconFor(Item item) {
        return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public final TableCellRenderer getRenderer(final Object o) {
        return super.getRenderer(o);
    }

    @Override
    public final TableCellRenderer getCustomizedRenderer(final Object o, final TableCellRenderer renderer) {
        return super.getCustomizedRenderer(o, renderer);
    }

    @Nullable
    public final TreeCellRenderer getTreeCellRenderer(final Object obj) {
        return null;
    }

    public final TreeCellRenderer getCustomizedTreeCellRenderer(final Object obj, final TreeCellRenderer renderer) {
        return renderer;
    }
}
