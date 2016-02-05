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

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.util.ui.ColumnInfo;
import org.metaborg.core.language.*;
import org.metaborg.core.project.configuration.ILanguageComponentConfig;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxIcons;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Tree model for languages and their implementation.
 */
public final class LanguageTreeModel extends ListTreeTableModelOnColumns {

    /**
     * Renders the first column of the tree.
     */
    public static class LanguageTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(
                final JTree tree,
                final Object value,
                final boolean sel,
                final boolean expanded,
                final boolean leaf,
                final int row,
                final boolean hasFocus) {

            final Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof LanguageTreeModel.ILanguageTreeNode) {
                final LanguageTreeModel.ILanguageTreeNode node = (LanguageTreeModel.ILanguageTreeNode)value;
                setText(node.getName());
                if (node.getIcon() != null)
                    setIcon(node.getIcon());
            }
            return c;
        }
    }

    public LanguageTreeModel() {
        super(
                new DefaultMutableTreeNode(""),
                new ColumnInfo[]{
                        new TreeKeyColumnInfo("Language"),
                        new ColumnInfo("Group ID") {
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Object valueOf(final Object obj) {
                                if (obj instanceof LanguageTreeModel.ILanguageTreeNode) {
                                    final LanguageTreeModel.ILanguageTreeNode node =
                                            (LanguageTreeModel.ILanguageTreeNode)obj;
                                    return node.getGroupId();
                                }
                                return null;
                            }
                        },
                        new ColumnInfo("Version") {
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Object valueOf(final Object obj) {

                                if (obj instanceof LanguageTreeModel.ILanguageTreeNode) {
                                    final LanguageTreeModel.ILanguageTreeNode node =
                                            (LanguageTreeModel.ILanguageTreeNode)obj;
                                    return node.getVersion();
                                }
                                return null;
                            }
                        },
                        new ColumnInfo("Status") {
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Object valueOf(final Object obj) {
                                if (obj instanceof LanguageTreeModel.ILanguageTreeNode) {
                                    final LanguageTreeModel.ILanguageTreeNode node =
                                            (LanguageTreeModel.ILanguageTreeNode)obj;
                                    return node.getStatus();
                                }
                                return null;
                            }
                        },
                }
        );
    }

    @Override
    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode)super.getRoot();
    }

    /**
     * Appends a node to a parent.
     *
     * @param newChild The new child node.
     * @param parent   The parent node.
     */
    public void appendNodeInto(@Nullable final MutableTreeNode newChild, final MutableTreeNode parent) {
        insertNodeInto(newChild, parent, parent.getChildCount());
    }

    /**
     * Gets the node for the specified language,
     * or adds it if not already present.
     *
     * @param language The language of the node to get.
     * @return The node representing the language.
     */
    public LanguageNode getOrAddLanguageNode(final ILanguage language) {
        return getOrAddLanguageNode(language, (MutableTreeNode)this.root);
    }

    /**
     * Gets the node for the specified language,
     * or adds it if not already present.
     *
     * @param language The language of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language.
     */
    public LanguageNode getOrAddLanguageNode(final ILanguage language, final MutableTreeNode parent) {
        @Nullable LanguageNode node = getLanguageNode(language, parent);
        if (node == null) {
            node = new LanguageNode(language);
            appendNodeInto(node, parent);
        }
        return node;
    }

    /**
     * Gets the node for the specified language.
     *
     * @param language The language of the node to get.
     * @return The node representing the language; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageNode getLanguageNode(final ILanguage language) {
        return getLanguageNode(language, this.root);
    }

    /**
     * Gets the node for the specified language.
     *
     * @param language The language of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageNode getLanguageNode(final ILanguage language, @Nullable final TreeNode parent) {
        return getLanguageTreeNode(LanguageNode.class, language, parent);
    }

    /**
     * Gets the node for the specified language implementation,
     * or adds it if not already present.
     *
     * @param languageImpl The language implementation of the node to get.
     * @return The node representing the language implementation.
     */
    public LanguageImplNode getOrAddLanguageImplNode(final ILanguageImpl languageImpl) {
        return getOrAddLanguageImplNode(languageImpl, getOrAddLanguageNode(languageImpl.belongsTo()));
    }

    /**
     * Gets the node for the specified language implementation,
     * or adds it if not already present.
     *
     * @param languageImpl The language implementation of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language implementation.
     */
    public LanguageImplNode getOrAddLanguageImplNode(final ILanguageImpl languageImpl, final MutableTreeNode parent) {
        @Nullable LanguageImplNode node = getLanguageImplNode(languageImpl, parent);
        if (node == null) {
            node = new LanguageImplNode(languageImpl);
            appendNodeInto(node, parent);
        }
        return node;
    }

    /**
     * Gets the node for the specified language implementation.
     *
     * @param languageImpl The language implementation of the node to get.
     * @return The node representing the language implementation; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode getLanguageImplNode(final ILanguageImpl languageImpl) {
        return getLanguageImplNode(languageImpl, getLanguageNode(languageImpl.belongsTo()));
    }

    /**
     * Gets the node for the specified language implementation.
     *
     * @param languageImpl The language implementation of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language implementation; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode getLanguageImplNode(final ILanguageImpl languageImpl, @Nullable final TreeNode parent) {
        return getLanguageTreeNode(LanguageImplNode.class, languageImpl, parent);
    }

    /**
     * Gets the node for the specified language component,
     * or adds it if not already present.
     *
     * @param languageComponent The language component of the node to get.
     * @param languageImpl The language implementation to which the component belongs.
     * @return The node representing the language component.
     */
    public LanguageComponentNode getOrAddLanguageComponentNode(final ILanguageComponent languageComponent, final ILanguageImpl languageImpl) {
        return getOrAddLanguageComponentNode(languageComponent, getOrAddLanguageImplNode(languageImpl));
    }

    /**
     * Gets the node for the specified language component,
     * or adds it if not already present.
     *
     * @param languageComponent The language component of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language component.
     */
    public LanguageComponentNode getOrAddLanguageComponentNode(final ILanguageComponent languageComponent, final MutableTreeNode parent) {
        @Nullable LanguageComponentNode node = getLanguageComponentNode(languageComponent, parent);
        if (node == null) {
            node = new LanguageComponentNode(languageComponent);
            appendNodeInto(node, parent);
        }
        return node;
    }

    /**
     * Gets the node for the specified language component.
     *
     * @param languageComponent The language component of the node to get.
     * @param languageImpl The language implementation to which the component belongs.
     * @return The node representing the language component; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageComponentNode getLanguageComponentNode(final ILanguageComponent languageComponent, final ILanguageImpl languageImpl) {
        return getLanguageComponentNode(languageComponent, getLanguageImplNode(languageImpl));
    }

    /**
     * Gets the node for the specified language component.
     *
     * @param languageComponent The language component of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language component; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageComponentNode getLanguageComponentNode(final ILanguageComponent languageComponent, @Nullable final TreeNode parent) {
        return getLanguageTreeNode(LanguageComponentNode.class, languageComponent, parent);
    }

    /**
     * Gets the node for the specified value.
     *
     * @param value The value of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the value; or <code>null</code> if not found.
     */
    @Nullable
    private <N extends ILanguageTreeNode<V>, V> N getLanguageTreeNode(final Class<N> nodeClass, final V value,
                                                                     @Nullable final TreeNode parent) {
        if (parent == null)
            return null;

        for (int i = 0; i < parent.getChildCount(); i++) {
            final TreeNode child = parent.getChildAt(i);
            if (!nodeClass.isInstance(child))
                continue;
            final N node = (N)child;
            if (node.getValue() != null && node.getValue().equals(value))
                return node;
        }
        return null;
    }



    public interface ILanguageTreeNode<V> {
        @Nullable String getName();
        @Nullable String getGroupId();
        @Nullable LanguageVersion getVersion();
        @Nullable LanguageStatus getStatus();
        @Nullable Icon getIcon();
        @Nullable V getValue();
    }

    public static class LanguageNode extends TreeNodeWithValue<ILanguage, LanguageNode> implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguage> {
        public LanguageNode(final ILanguage language) {
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
            return SpoofaxIcons.INSTANCE.defaultIcon();
        }

        @Nullable
        @Override
        public String getName() {
            return this.getValue().name();
        }

        @Override
        @Nullable
        public String getGroupId() {
            return null;
        }

        @Override
        @Nullable
        public LanguageVersion getVersion() {
            return null;
        }

        @Override
        @Nullable
        public LanguageStatus getStatus() {
            return null;
        }
    }
    public static class LanguageImplNode extends TreeNodeWithValue<ILanguageImpl, LanguageImplNode>
            implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguageImpl> {

        private final LanguageIdentifier id;

        public LanguageImplNode(final ILanguageImpl languageImpl) {
            super(languageImpl);
            this.id = languageImpl.id();
        }

        public LanguageImplNode(final LanguageIdentifier id) {
            super(null);
            this.id = id;
        }

        @Nullable
        @Override
        public Object getValueOfColumn(final ModelColumnInfo<LanguageImplNode> column) {
            return this.id();
        }

        @Nullable
        @Override
        public Icon getIcon() {
            return null;
        }

        /**
         * Gets the language identifier.
         *
         * @return The language identifier.
         */
        public LanguageIdentifier id() {
            return this.id;
        }

        @Nullable
        @Override
        public String getName() {
            return this.id().id;
        }

        @Override
        @Nullable
        public String getGroupId() {
            return this.id().groupId;
        }

        @Override
        @Nullable
        public LanguageVersion getVersion() {
            return this.id().version;
        }

        @Override
        @Nullable
        public LanguageStatus getStatus() {
            return null;
        }
    }

    public static class LanguageComponentNode extends TreeNodeWithValue<ILanguageComponent, LanguageComponentNode>
            implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguageComponent>  {

        public LanguageComponentNode(final ILanguageComponent languageComponent) {
            super(languageComponent);
        }

        public LanguageComponentNode(final LanguageIdentifier id) {
            super(null);
        }

        @Nullable
        @Override
        public Object getValueOfColumn(final ModelColumnInfo<LanguageComponentNode> column) {
            return this.getValue().id();
        }

        @Nullable
        @Override
        public Icon getIcon() {
            return null;
        }


        @Nullable
        @Override
        public String getName() {
            return this.getValue().id().id;
        }

        @Override
        @Nullable
        public String getGroupId() {
            return this.getValue().id().groupId;
        }

        @Override
        @Nullable
        public LanguageVersion getVersion() {
            return this.getValue().id().version;
        }

        @Override
        @Nullable
        public LanguageStatus getStatus() {
            return LanguageStatus.Loaded;
        }
    }


    public static class LanguageRequestNode extends TreeNodeWithValue<ILanguageDiscoveryRequest, LanguageRequestNode>
            implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguageDiscoveryRequest>  {

        public LanguageRequestNode(final ILanguageDiscoveryRequest request) {
            super(request);
        }

        @Nullable
        @Override
        public Object getValueOfColumn(final ModelColumnInfo<LanguageRequestNode> column) {
            if (this.getValue().available()) {
                @Nullable final ILanguageComponentConfig config = this.getValue().config();
                assert config != null : "The configuration should not be null since the request is available.";
                return config.identifier();
            } else {
                return this.getValue().location();
            }
        }

        @Nullable
        @Override
        public Icon getIcon() {
            return null;
        }

        @Nullable
        @Override
        public String getName() {
            if (this.getValue().available()) {
                @Nullable final ILanguageComponentConfig config = this.getValue().config();
                assert config != null : "The configuration should not be null since the request is available.";
                return config.identifier().id;
            } else {
                return this.getValue().location().toString();
            }
        }

        @Override
        @Nullable
        public String getGroupId() {
            if (this.getValue().available()) {
                @Nullable final ILanguageComponentConfig config = this.getValue().config();
                assert config != null : "The configuration should not be null since the request is available.";
                return config.identifier().groupId;
            } else {
                return null;
            }
        }

        @Override
        @Nullable
        public LanguageVersion getVersion() {
            if (this.getValue().available()) {
                @Nullable final ILanguageComponentConfig config = this.getValue().config();
                assert config != null : "The configuration should not be null since the request is available.";
                return config.identifier().version;
            } else {
                return null;
            }
        }

        @Override
        @Nullable
        public LanguageStatus getStatus() {
            if (this.getValue().available()) {
                return LanguageStatus.Standby;
            } else {
                return LanguageStatus.Error;
            }
        }
    }
}
