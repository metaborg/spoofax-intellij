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

package org.metaborg.intellij.idea.gui;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.dualView.TreeTableView;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryRequest;
import org.metaborg.core.language.ILanguageImpl;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class LanguagesPanel extends JPanel {

    private final TreeTableView languagesTree;
    @Nullable
    private LanguagesConfiguration controller;
    @Nullable
    private DefaultActionGroup addActionPopupGroup;

    public LanguagesPanel() {
        super(new BorderLayout());

        final LanguageTreeModel model = new LanguageTreeModel();
        this.languagesTree = new TreeTableView(model);
        this.languagesTree.setTreeCellRenderer(new LanguageTreeModel.LanguageTreeCellRenderer());
        this.languagesTree.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        final ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(this.languagesTree)
                .setAddAction(this::addLanguage)
                .setAddActionUpdater(e -> this.controller != null)
                .setRemoveAction(button -> removeLanguage(getSelectedNode()))
                .setRemoveActionUpdater(e -> this.controller != null && getSelectedNode() != null)
                .disableUpDownActions();

        add(toolbarDecorator.createPanel(), BorderLayout.CENTER);
        setBorder(IdeBorderFactory.createTitledBorder("Loaded languages", false));
    }

    /**
     * Adds a language, i.e. shows the popup menu.
     * @param button
     */
    private void addLanguage(final AnActionButton button) {
        if (this.controller == null)
            return;
        showPopupActions(button);
    }

    /**
     * Removes a language node, and depending on the type, all its children.
     * @param node
     */
    private void removeLanguage(@Nullable final LanguageTreeModel.ILanguageTreeNode<?> node) {
        if (this.controller == null || node == null)
            return;
        if (node instanceof LanguageTreeModel.LanguageComponentNode) {
            removeLanguageComponent((LanguageTreeModel.LanguageComponentNode)node);
        } else if (node instanceof LanguageTreeModel.LanguageRequestNode) {
            removeLanguageRequest((LanguageTreeModel.LanguageRequestNode)node);
        } else if (node instanceof LanguageTreeModel.LanguageImplNode) {
            removeLanguageImpl((LanguageTreeModel.LanguageImplNode)node);
        } else if (node instanceof LanguageTreeModel.LanguageNode) {
            removeLanguage((LanguageTreeModel.LanguageNode)node);
        } else {
            throw new RuntimeException("Unexpected.");
        }
    }

    /**
     * Removes a language component.
     * @param node
     */
    private void removeLanguageComponent(final LanguageTreeModel.LanguageComponentNode node) {
        final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
        @Nullable final ILanguageComponent component = node.getValue();
        if (this.controller != null && component != null) {
            this.controller.removeLanguageComponent(component);
        }
        model.removeLanguageComponentNode(node);
    }

    /**
     * Removes a language request.
     * @param node
     */
    private void removeLanguageRequest(final LanguageTreeModel.LanguageRequestNode node) {
        final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
        @Nullable final ILanguageDiscoveryRequest request = node.getValue();
        if (this.controller != null && request != null) {
            this.controller.removeLanguageRequest(request);
        }
        model.removeLanguageRequestNode(node);
    }

    /**
     * Removes a language implementation and all language requests and components under it.
     * @param node
     */
    private void removeLanguageImpl(final LanguageTreeModel.LanguageImplNode node) {
        for (int i = node.getChildCount() - 1; i >= 0; i--) {
            final TreeNode child = node.getChildAt(i);
            if (child instanceof LanguageTreeModel.LanguageRequestNode) {
                removeLanguageRequest((LanguageTreeModel.LanguageRequestNode)child);
            } else if (child instanceof LanguageTreeModel.LanguageComponentNode) {
                removeLanguageComponent((LanguageTreeModel.LanguageComponentNode)child);
            }
        }
    }

    /**
     * Removes a language and all language implementations under it.
     * @param node
     */
    private void removeLanguage(final LanguageTreeModel.LanguageNode node) {
        for (int i = node.getChildCount() - 1; i >= 0; i--) {
            final TreeNode child = node.getChildAt(i);
            if (child instanceof LanguageTreeModel.LanguageImplNode) {
                removeLanguageImpl((LanguageTreeModel.LanguageImplNode)child);
            }
        }
    }

    /**
     * Sets the languages to display in the panel.
     * @param languageImpls
     */
    public void setLanguages(final Iterable<ILanguageImpl> languageImpls) {
        final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
        final DefaultMutableTreeNode root = model.getRoot();
        root.removeAllChildren();
        model.reload();

        for (final ILanguageImpl impl : languageImpls) {
            final LanguageTreeModel.LanguageImplNode implNode = model.getOrAddLanguageImplNode(impl);
            for (final ILanguageComponent component : impl.components()) {
                model.getOrAddLanguageComponentNode(component, implNode);
            }
        }
        model.reload();
    }

    /**
     * Gets the currently selected node.
     *
     * @return The currently selected node;
     * or <code>null</code> when none is selected.
     */
    @Nullable
    public LanguageTreeModel.ILanguageTreeNode<?> getSelectedNode() {
        final List<LanguageTreeModel.ILanguageTreeNode<?>> objs = getSelectedNodes();
        if (objs.size() != 1)
            return null;
        return objs.get(0);
    }

    /**
     * Gets the currently selected nodes.
     *
     * @return A list of currently selected language tree nodes.
     */
    public List<LanguageTreeModel.ILanguageTreeNode<?>> getSelectedNodes() {
        return ((List<Object>)this.languagesTree.getSelection()).stream()
                .filter(this::isSelectableNode)
                .map(x -> (LanguageTreeModel.ILanguageTreeNode<?>)x)
                .collect(toList());
    }

    /**
     * Determines which nodes can be selected.
     * @param node The node to test.
     * @return <code>true</code> when the node can be selected;
     * otherwise, <code>false</code>.
     */
    private boolean isSelectableNode(Object node) {
        return node instanceof LanguageTreeModel.ILanguageTreeNode;
    }

    /**
     * Attaches a controller to the panel.
     *
     * @param controller The controller to attach; or <code>null</code> to detach.
     */
    public void attachController(@Nullable final LanguagesConfiguration controller) {
        this.controller = controller;

        if (controller != null) {
            final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
            initPopupActions(model, controller);
        } else {
            deinitPopupActions();
        }
    }

    /**
     * Initializes the popup-menu.
     * @param model
     * @param controller
     */
    private void initPopupActions(final LanguageTreeModel model, final LanguagesConfiguration controller) {
        this.addActionPopupGroup = new DefaultActionGroup("Add", true);
        this.addActionPopupGroup.add(new AddLanguageFromDirectoryAction(model, controller));
        this.addActionPopupGroup.add(new AddLanguageFromArtifactAction(model, controller));
    }

    /**
     * Deinitializes the popup-menu.
     */
    private void deinitPopupActions() {
        this.addActionPopupGroup = null;
    }

    /**
     * Shows the popu-menu.
     * @param button
     */
    private void showPopupActions(final AnActionButton button) {
        @Nullable final ActionGroup group = this.addActionPopupGroup;
        if (group == null)
            return;

        JBPopupFactory.getInstance().createActionGroupPopup(null, group,
                                                            DataManager.getInstance().getDataContext(button.getContextComponent()),
                                                            JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, true
        ).show(button.getPreferredPopupPoint());
    }

}
