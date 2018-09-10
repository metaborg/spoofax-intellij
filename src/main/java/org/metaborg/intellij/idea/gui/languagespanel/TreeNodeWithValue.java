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

import javax.annotation.Nullable;
import javax.swing.tree.DefaultMutableTreeNode;

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
