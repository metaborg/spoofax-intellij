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

import javax.annotation.*;
import javax.swing.tree.*;

/**
 * A tree node with a value.
 *
 * @param <Item> The type of value.
 * @param <Node> The type of node.
 */
public class TreeNodeWithValue<Item, Node extends IModelItem<Node>> extends DefaultMutableTreeNode implements IModelItem<Node> {

    /**
     * Initializes a new instance of the {@link TreeNodeWithValue} class.
     *
     * @param value The value; or <code>null</code>.
     */
    public TreeNodeWithValue(@Nullable final Item value) {
        this(value, true);
    }

    /**
     * Initializes a new instance of the {@link TreeNodeWithValue} class.
     *
     * @param value The value; or <code>null</code>.
     * @param allowsChildren Whether this node can have child nodes.
     */
    public TreeNodeWithValue(@Nullable final Item value, final boolean allowsChildren) {
        super(value, allowsChildren);
    }

    /**
     * Gets the value of the node.
     *
     * @return The value of the node; or <code>null</code>.
     */
    @Nullable
    public Item getValue() {
        return (Item)getUserObject();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getValueOfColumn(final ModelColumnInfo<Node> column) {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueOfColumn(final ModelColumnInfo<Node> column, @Nullable final Object value) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canEditValueOfColumn(final ModelColumnInfo<Node> column) {
        return false;
    }
}
