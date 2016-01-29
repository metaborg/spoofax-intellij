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

import javax.annotation.Nullable;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNodeWithValue<Item, Node extends IModelItem<Node>> extends DefaultMutableTreeNode implements IModelItem<Node> {

    public TreeNodeWithValue(final Item value) {
        this(value, true);
    }

    public TreeNodeWithValue(final Item value, final boolean allowsChildren) {
        super(value, allowsChildren);
    }

    public Item getValue() {
        return (Item)getUserObject();
    }

    @Nullable
    @Override
    public Object getValueOfColumn(final ModelColumnInfo<Node> column) {
        return getValue();
    }

    @Override
    public void setValueOfColumn(final ModelColumnInfo<Node> column, @Nullable final Object value) {

    }

    @Override
    public boolean canEditValueOfColumn(final ModelColumnInfo<Node> column) {
        return false;
    }
}
