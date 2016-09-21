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

import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.util.ui.ColumnInfo;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.graphics.IIconManager;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Tree model for languages and their implementation.
 */
public final class LanguageTreeModel extends ListTreeTableModelOnColumns {

    private final IIconManager iconManager;

    /**
     * The column info's.
     */
    private static final ColumnInfo[] columnInfo = new ColumnInfo[]{
            new TreeKeyColumnInfo("Language"),
            new ColumnInfo("Group ID") {
                @org.jetbrains.annotations.Nullable
                @Override
                public Object valueOf(final Object obj) {
                    if (obj instanceof ILanguageTreeNode) {
                        final ILanguageTreeNode node =
                                (ILanguageTreeNode)obj;
                        return node.getGroupId();
                    }
                    return null;
                }
            },
            new ColumnInfo("Version") {
                @org.jetbrains.annotations.Nullable
                @Override
                public Object valueOf(final Object obj) {

                    if (obj instanceof ILanguageTreeNode) {
                        final ILanguageTreeNode node =
                                (ILanguageTreeNode)obj;
                        return node.getVersion();
                    }
                    return null;
                }
            },
            new ColumnInfo("Status") {
                @org.jetbrains.annotations.Nullable
                @Override
                public Object valueOf(final Object obj) {
                    if (obj instanceof ILanguageTreeNode) {
                        final ILanguageTreeNode node =
                                (ILanguageTreeNode)obj;
                        return node.getStatus();
                    }
                    return null;
                }
            },
    };

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

            if (value instanceof ILanguageTreeNode) {
                final ILanguageTreeNode node = (ILanguageTreeNode)value;
                setText(node.getName());
                if (node.getIcon() != null)
                    setIcon(node.getIcon());
            }
            return c;
        }
    }

    /**
     * Initializes a new instance of the {@link LanguageTreeModel} class.
     *
     * @param iconManager The icon manager.
     */
    public LanguageTreeModel(final IIconManager iconManager) {
        super(new DefaultMutableTreeNode(""), columnInfo);

        this.iconManager = iconManager;
    }

    /**
     * {@inheritDoc}
     */
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
     * @param language The language of the node to get; or <code>null</code> when the language is not known.
     * @return The node representing the language.
     */
    public LanguageNode getOrAddLanguageNode(@Nullable final ILanguage language) {
        final MutableTreeNode parent = (MutableTreeNode)this.root;
        return getOrAddLanguageNode(language, parent);
    }

    /**
     * Gets the node for the specified language,
     * or adds it if not already present.
     *
     * @param language The language of the node to get; or <code>null</code> when the language is not known.
     * @param parent The parent node under which to look.
     * @return The node representing the language.
     */
    public LanguageNode getOrAddLanguageNode(@Nullable final ILanguage language, final MutableTreeNode parent) {
        @Nullable LanguageNode node = getLanguageNode(language, parent);
        if (node == null) {
            node = new LanguageNode(language, this.iconManager);
            appendNodeInto(node, parent);
        }
        return node;
    }

    /**
     * Gets the node for the specified language.
     *
     * @param language The language of the node to get; or <code>null</code> when the language is not known.
     * @return The node representing the language; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageNode getLanguageNode(@Nullable final ILanguage language) {
        final TreeNode parent = this.root;
        return getLanguageNode(language, parent);
    }

    /**
     * Gets the node for the specified language.
     *
     * @param language The language of the node to get; or <code>null</code> when the language is not known.
     * @param parent The parent node under which to look.
     * @return The node representing the language; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageNode getLanguageNode(@Nullable final ILanguage language, @Nullable final TreeNode parent) {
        return getLanguageTreeNode(LanguageNode.class, language, parent);
    }

    /**
     * Removes the node for the specified language.
     *
     * @param language The language of the node to remove; or <code>null</code> when the language is not known.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageNode removeLanguageNode(@Nullable final ILanguage language) {
        final MutableTreeNode parent = (MutableTreeNode)this.root;
        return removeLanguageNode(language, parent);
    }

    /**
     * Removes the node for the specified language.
     *
     * @param language The language of the node to remove; or <code>null</code> when the language is not known.
     * @param parent The parent node under which to look.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageNode removeLanguageNode(@Nullable final ILanguage language, @Nullable final MutableTreeNode parent) {
        @Nullable final LanguageNode node = getLanguageNode(language, parent);
        return removeLanguageNode(node);
    }

    /**
     * Removes the node for the specified language.
     *
     * @param node The node to remove.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageNode removeLanguageNode(@Nullable final LanguageNode node) {
        if (node != null) {
            removeNodeFromParent(node);
        }
        return node;
    }




    /**
     * Gets the node for the specified language implementation,
     * or adds it if not already present.
     *
     * @param languageImpl The language implementation of the node to get.
     * @return The node representing the language implementation.
     */
    public LanguageImplNode getOrAddLanguageImplNode(final ILanguageImpl languageImpl) {
        final LanguageNode parent = getOrAddLanguageNode(languageImpl.belongsTo());
        return getOrAddLanguageImplNode(languageImpl, parent);
    }

    /**
     * Gets the node for the specified language implementation,
     * or adds it if not already present.
     *
     * @param languageImpl The language implementation of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language implementation.
     */
    public LanguageImplNode getOrAddLanguageImplNode(final ILanguageImpl languageImpl, final LanguageNode parent) {
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
        @Nullable final LanguageNode parent = getLanguageNode(languageImpl.belongsTo());
        return getLanguageImplNode(languageImpl, parent);
    }

    /**
     * Gets the node for the specified language implementation.
     *
     * @param languageImpl The language implementation of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language implementation; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode getLanguageImplNode(final ILanguageImpl languageImpl, @Nullable final LanguageNode parent) {
        return getLanguageTreeNode(LanguageImplNode.class, languageImpl, parent);
    }

    /**
     * Removes the node for the specified language implementation.
     *
     * @param languageImpl The language implementation of the node to remove.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode removeLanguageImplNode(final ILanguageImpl languageImpl) {
        @Nullable final LanguageNode parent = getLanguageNode(languageImpl.belongsTo());
        return removeLanguageImplNode(languageImpl, parent);
    }

    /**
     * Removes the node for the specified language implementation.
     *
     * @param languageImpl The language implementation of the node to remove.
     * @param parent The parent node under which to look.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode removeLanguageImplNode(final ILanguageImpl languageImpl, @Nullable final LanguageNode parent) {
        @Nullable final LanguageImplNode node = getLanguageImplNode(languageImpl, parent);
        return removeLanguageImplNode(node);
    }

    /**
     * Removes the node for the specified language implementation.
     *
     * @param node The the node to remove.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode removeLanguageImplNode(@Nullable final LanguageImplNode node) {
        if (node != null) {
            final LanguageNode parent = (LanguageNode) node.getParent();
            removeNodeFromParent(node);
            if (parent.getChildCount() == 0) {
                removeLanguageNode(parent);
            }
        }
        return node;
    }




    /**
     * Gets the node for the specified language implementation,
     * or adds it if not already present.
     *
     * @param id The language identifier of the node to get.
     * @return The node representing the language implementation.
     */
    public LanguageImplNode getOrAddLanguageImplNode(final LanguageIdentifier id) {
        final LanguageNode parent = getOrAddLanguageNode(null);
        return getOrAddLanguageImplNode(id, parent);
    }

    /**
     * Gets the node for the specified language implementation,
     * or adds it if not already present.
     *
     * @param id The language identifier of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language implementation.
     */
    public LanguageImplNode getOrAddLanguageImplNode(final LanguageIdentifier id, final LanguageNode parent) {
        @Nullable LanguageImplNode node = getLanguageImplNode(id, parent);
        if (node == null) {
            node = new LanguageImplNode(id);
            appendNodeInto(node, parent);
        }
        return node;
    }

    /**
     * Gets the node for the specified language implementation.
     *
     * @param id The language identifier of the node to get.
     * @return The node representing the language implementation; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode getLanguageImplNode(final LanguageIdentifier id) {
        @Nullable final LanguageNode parent = getLanguageNode(null);
        return getLanguageImplNode(id, parent);
    }

    /**
     * Gets the node for the specified language implementation.
     *
     * @param id The language identifier of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language implementation; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode getLanguageImplNode(final LanguageIdentifier id, @Nullable final LanguageNode parent) {
        return getLanguageTreeNode(LanguageImplNode.class,
                                   (Predicate<LanguageImplNode>)(n) -> n.id().equals(id), parent);
    }

    /**
     * Removes the node for the specified language implementation.
     *
     * @param id The language identifier of the node to remove.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode removeLanguageImplNode(final LanguageIdentifier id) {
        @Nullable final LanguageNode parent = getLanguageNode(null);
        return removeLanguageImplNode(id, parent);
    }

    /**
     * Removes the node for the specified language implementation.
     *
     * @param id The language identifier of the node to remove.
     * @param parent The parent node under which to look.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageImplNode removeLanguageImplNode(final LanguageIdentifier id, @Nullable final LanguageNode parent) {
        @Nullable final LanguageImplNode node = getLanguageImplNode(id, parent);
        return removeLanguageImplNode(node);
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
        final LanguageImplNode parent = getOrAddLanguageImplNode(languageImpl);
        return getOrAddLanguageComponentNode(languageComponent, parent);
    }

    /**
     * Gets the node for the specified language component,
     * or adds it if not already present.
     *
     * @param languageComponent The language component of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language component.
     */
    public LanguageComponentNode getOrAddLanguageComponentNode(final ILanguageComponent languageComponent, final LanguageImplNode parent) {
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
        @Nullable final LanguageImplNode parent = getLanguageImplNode(languageImpl);
        return getLanguageComponentNode(languageComponent, parent);
    }

    /**
     * Gets the node for the specified language component.
     *
     * @param languageComponent The language component of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language component; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageComponentNode getLanguageComponentNode(final ILanguageComponent languageComponent, @Nullable final LanguageImplNode parent) {
        return getLanguageTreeNode(LanguageComponentNode.class, languageComponent, parent);
    }

    /**
     * Removes the node for the specified language component.
     *
     * @param languageComponent The language component of the node to remove.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageComponentNode removeLanguageComponentNode(final ILanguageComponent languageComponent, final ILanguageImpl languageImpl) {
        @Nullable final LanguageImplNode parent = getLanguageImplNode(languageImpl);
        return removeLanguageComponentNode(languageComponent, parent);
    }

    /**
     * Removes the node for the specified language component.
     *
     * @param languageComponent The language component of the node to remove.
     * @param parent The parent node under which to look.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageComponentNode removeLanguageComponentNode(final ILanguageComponent languageComponent, @Nullable final LanguageImplNode parent) {
        @Nullable final LanguageComponentNode node = getLanguageComponentNode(languageComponent, parent);
        return removeLanguageComponentNode(node);
    }

    /**
     * Removes the node for the specified language component.
     *
     * @param node The node to remove.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageComponentNode removeLanguageComponentNode(@Nullable final LanguageComponentNode node) {
        if (node != null) {
            final LanguageImplNode parent = (LanguageImplNode) node.getParent();
            removeNodeFromParent(node);
            if (parent.getChildCount() == 0) {
                removeLanguageImplNode(parent);
            }
        }
        return node;
    }




    /**
     * Gets the node for the specified language discovery request,
     * or adds it if not already present.
     *
     * @param languageRequest The language discovery request of the node to get.
     * @return The node representing the language discovery request.
     */
    public LanguageRequestNode getOrAddLanguageRequestNode(final ILanguageDiscoveryRequest languageRequest) {
        // FIXME: Config can be null if request is not available.
        final LanguageImplNode parent = getOrAddLanguageImplNode(languageRequest.config().identifier());
        return getOrAddLanguageRequestNode(languageRequest, parent);
    }

    /**
     * Gets the node for the specified language discovery request,
     * or adds it if not already present.
     *
     * @param languageRequest The language discovery request of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language discovery request.
     */
    public LanguageRequestNode getOrAddLanguageRequestNode(final ILanguageDiscoveryRequest languageRequest, final LanguageImplNode parent) {
        @Nullable LanguageRequestNode node = getLanguageRequestNode(languageRequest, parent);
        if (node == null) {
            node = new LanguageRequestNode(languageRequest);
            appendNodeInto(node, parent);
        }
        return node;
    }

    /**
     * Gets the node for the specified language discovery request.
     *
     * @param languageRequest The language discovery request of the node to get.
     * @return The node representing the language discovery request; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageRequestNode getLanguageRequestNode(final ILanguageDiscoveryRequest languageRequest) {
        // FIXME: Config can be null if request is not available.
        final LanguageImplNode parent = getLanguageImplNode(languageRequest.config().identifier());
        return getLanguageRequestNode(languageRequest, parent);
    }

    /**
     * Gets the node for the specified language discovery request.
     *
     * @param languageRequest The language discovery request of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the language discovery request; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageRequestNode getLanguageRequestNode(final ILanguageDiscoveryRequest languageRequest, @Nullable final LanguageImplNode parent) {
        return getLanguageTreeNode(LanguageRequestNode.class, languageRequest, parent);
    }

    /**
     * Removes the node for the specified language discovery request.
     *
     * @param languageRequest The language discovery request of the node to remove.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageRequestNode removeLanguageRequestNode(final ILanguageDiscoveryRequest languageRequest) {
        // FIXME: Config can be null if request is not available.
        final LanguageImplNode parent = getLanguageImplNode(languageRequest.config().identifier());
        return removeLanguageRequestNode(languageRequest, parent);
    }

    /**
     * Removes the node for the specified language discovery request.
     *
     * @param languageRequest The language discovery request of the node to remove.
     * @param parent The parent node under which to look.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageRequestNode removeLanguageRequestNode(final ILanguageDiscoveryRequest languageRequest, @Nullable final LanguageImplNode parent) {
        @Nullable final LanguageRequestNode node = getLanguageRequestNode(languageRequest, parent);
        return removeLanguageRequestNode(node);
    }

    /**
     * Removes the node for the specified language discovery request.
     *
     * @param node The node to remove.
     * @return The removed node; or <code>null</code> if not found.
     */
    @Nullable
    public LanguageRequestNode removeLanguageRequestNode(@Nullable final LanguageRequestNode node) {
        if (node != null) {
            final LanguageImplNode parent = (LanguageImplNode) node.getParent();
            removeNodeFromParent(node);
            if (parent.getChildCount() == 0) {
                removeLanguageImplNode(parent);
            }
        }
        return node;
    }


    /**
     * Gets the node for the specified value.
     *
     * @param predicate The predicate that returns true for the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the value; or <code>null</code> if not found.
     */
    @Nullable
    private <N extends ILanguageTreeNode<V>, V> N getLanguageTreeNode(final Class<N> nodeClass, final Predicate<N> predicate,
                                                                      @Nullable final TreeNode parent) {
        if (parent == null)
            return null;

        for (int i = 0; i < parent.getChildCount(); i++) {
            final TreeNode child = parent.getChildAt(i);
            if (!nodeClass.isInstance(child))
                continue;
            final N node = (N)child;
            if (predicate.test(node))
                return node;
        }
        return null;
    }

    /**
     * Gets the node for the specified value.
     *
     * @param value The value of the node to get.
     * @param parent The parent node under which to look.
     * @return The node representing the value; or <code>null</code> if not found.
     */
    @Nullable
    private <N extends ILanguageTreeNode<V>, V> N getLanguageTreeNode(final Class<N> nodeClass, @Nullable final V value,
                                                                      @Nullable final TreeNode parent) {
        return getLanguageTreeNode(nodeClass, (Predicate<N>)v -> Objects.equals(v.getValue(), value), parent);
    }

}
