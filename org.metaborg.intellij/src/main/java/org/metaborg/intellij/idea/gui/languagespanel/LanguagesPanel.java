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

import com.google.inject.*;
import com.intellij.ide.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.popup.*;
import com.intellij.ui.*;
import com.intellij.ui.dualView.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.idea.gui.languagesettings.*;

import javax.annotation.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * A languages panel.
 */
public final class LanguagesPanel extends JPanel {

    private IIconManager iconManager;
    private final TreeTableView languagesTree;
    @Nullable
    private LanguagesSettings controller;
    @Nullable
    private DefaultActionGroup addActionPopupGroup;

    /**
     * This instance is created by Swing.
     * Do not call this constructor manually.
     */
    public LanguagesPanel() {
        super(new BorderLayout());
        SpoofaxIdeaPlugin.injector().injectMembers(this);

        final LanguageTreeModel model = new LanguageTreeModel(this.iconManager);
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

    @Inject
    @SuppressWarnings("unused")
    protected void inject(final IIconManager iconManager) {
        this.iconManager = iconManager;
    }

    /**
     * Adds a language, i.e. shows the popup menu.
     *
     * @param button The button that was clicked.
     */
    private void addLanguage(final AnActionButton button) {
        if (this.controller == null)
            return;
        showPopupActions(button);
    }

    /**
     * Removes a language node, and depending on the type, all its children.
     *
     * @param node The node to remove.
     */
    private void removeLanguage(@Nullable final ILanguageTreeNode<?> node) {
        if (this.controller == null || node == null)
            return;
        if (node instanceof LanguageComponentNode) {
            removeLanguageComponent((LanguageComponentNode)node);
        } else if (node instanceof LanguageRequestNode) {
            removeLanguageRequest((LanguageRequestNode)node);
        } else if (node instanceof LanguageImplNode) {
            removeLanguageImpl((LanguageImplNode)node);
        } else if (node instanceof LanguageNode) {
            removeLanguage((LanguageNode)node);
        } else {
            throw new RuntimeException("Unexpected.");
        }
    }

    /**
     * Removes a language component.
     *
     * @param node The node to remove.
     */
    private void removeLanguageComponent(final LanguageComponentNode node) {
        final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
        @Nullable final ILanguageComponent component = node.getValue();
        if (this.controller != null && component != null) {
            this.controller.removeLanguageComponent(component);
        }
        model.removeLanguageComponentNode(node);
    }

    /**
     * Removes a language request.
     *
     * @param node The node to remove.
     */
    private void removeLanguageRequest(final LanguageRequestNode node) {
        final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
        @Nullable final ILanguageDiscoveryRequest request = node.getValue();
        if (this.controller != null && request != null) {
            this.controller.removeLanguageRequest(request);
        }
        model.removeLanguageRequestNode(node);
    }

    /**
     * Removes a language implementation and all language requests and components under it.
     *
     * @param node The node to remove.
     */
    private void removeLanguageImpl(final LanguageImplNode node) {
        for (int i = node.getChildCount() - 1; i >= 0; i--) {
            final TreeNode child = node.getChildAt(i);
            if (child instanceof LanguageRequestNode) {
                removeLanguageRequest((LanguageRequestNode)child);
            } else if (child instanceof LanguageComponentNode) {
                removeLanguageComponent((LanguageComponentNode)child);
            }
        }
    }

    /**
     * Removes a language and all language implementations under it.
     *
     * @param node The node to remove.
     */
    private void removeLanguage(final LanguageNode node) {
        for (int i = node.getChildCount() - 1; i >= 0; i--) {
            final TreeNode child = node.getChildAt(i);
            if (child instanceof LanguageImplNode) {
                removeLanguageImpl((LanguageImplNode)child);
            }
        }
    }

    /**
     * Sets the languages to display in the panel.
     *
     * @param languageImpls The language implementations to display.
     */
    public void setLanguages(final Iterable<ILanguageImpl> languageImpls) {
        final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
        final DefaultMutableTreeNode root = model.getRoot();
        root.removeAllChildren();
        model.reload();

        for (final ILanguageImpl impl : languageImpls) {
            final LanguageImplNode implNode = model.getOrAddLanguageImplNode(impl);
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
    public ILanguageTreeNode<?> getSelectedNode() {
        final List<ILanguageTreeNode<?>> objs = getSelectedNodes();
        if (objs.size() != 1)
            return null;
        return objs.get(0);
    }

    /**
     * Gets the currently selected nodes.
     *
     * @return A list of currently selected language tree nodes.
     */
    public List<ILanguageTreeNode<?>> getSelectedNodes() {
        return ((List<Object>)this.languagesTree.getSelection()).stream()
                .filter(this::isSelectableNode)
                .map(x -> (ILanguageTreeNode<?>)x)
                .collect(toList());
    }

    /**
     * Determines which nodes can be selected.
     *
     * @param node The node to test.
     * @return <code>true</code> when the node can be selected;
     * otherwise, <code>false</code>.
     */
    private boolean isSelectableNode(final Object node) {
        return node instanceof ILanguageTreeNode;
    }

    /**
     * Attaches a controller to the panel.
     *
     * @param controller The controller to attach; or <code>null</code> to detach.
     */
    public void attachController(@Nullable final LanguagesSettings controller) {
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
     *
     * @param model The language tree model.
     * @param controller The controller.
     */
    private void initPopupActions(final LanguageTreeModel model, final LanguagesSettings controller) {
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
     * Shows the popup-menu.
     *
     * @param button The button that was pressed.
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
