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
import com.intellij.ide.actions.SmartPopupActionGroup;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.dualView.TreeTableView;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.util.ui.ColumnInfo;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryRequest;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxIcons;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public final class LanguagesPanel extends JPanel {

    private final TreeTableView languagesTree;
    @Nullable
    private LanguagesConfigurable controller;
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
                .setAddActionUpdater(e -> canAddLanguage())
                .setRemoveAction(button -> removeLanguage(getSelectedObject()))
                .setRemoveActionUpdater(e -> canRemoveLanguage(getSelectedObject()))
                .disableUpDownActions();

        add(toolbarDecorator.createPanel(), BorderLayout.CENTER);
        setBorder(IdeBorderFactory.createTitledBorder("Loaded languages", false));
    }

    private void addLanguage(final AnActionButton button) {
        if (this.controller == null || !canAddLanguage())
            return;
        showPopupActions(button);
    }

    private boolean canAddLanguage() {
        return this.controller != null;
    }

    private void removeLanguage(@Nullable final Object obj) {
        final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
        if (this.controller == null || !canRemoveLanguage(obj))
            return;
        if (obj instanceof ILanguageImpl) {
            this.controller.removeLanguageImpl((ILanguageImpl)obj);
            model.removeLanguageImplNode((ILanguageImpl)obj);
        } else if (obj instanceof ILanguageDiscoveryRequest) {
            this.controller.removeLanguageRequest((ILanguageDiscoveryRequest)obj);
            model.removeLanguageRequestNode((ILanguageDiscoveryRequest)obj);
        }
    }

    private boolean canRemoveLanguage(@Nullable final Object obj) {
        return this.controller != null
                && obj != null
                && (obj instanceof ILanguageImpl || obj instanceof ILanguageDiscoveryRequest);
    }

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

//    /**
//     * Gets the currently selected language implementation.
//     *
//     * @return The currently selected language implementation;
//     * or <code>null</code> when none is selected.
//     */
//    @Nullable
//    public ILanguageImpl selectedLanguageImpl() {
//        final List<ILanguageImpl> languages = ((List<Object>)this.languagesTree.getSelection()).stream()
//                .filter(x -> x instanceof LanguageTreeModel.LanguageImplNode)
//                .map(x -> ((LanguageTreeModel.LanguageImplNode)x).getValue())
//                .collect(toList());
//        if (languages.size() != 1)
//            return null;
//        return languages.get(0);
//    }

    /**
     * Gets the currently selected language discovery request.
     *
     * @return The currently selected language discovery request;
     * or <code>null</code> when none is selected.
     */
    @Nullable
    public Object getSelectedObject() {
        final List<Object> objs = getSelectedObjects();
        if (objs.size() != 1)
            return null;
        return objs.get(0);
    }

    public List<Object> getSelectedObjects() {
        return ((List<Object>)this.languagesTree.getSelection()).stream()
                .filter(this::isSelectableNode)
                .map(x -> ((LanguageTreeModel.ILanguageTreeNode)x).getValue())
                .collect(toList());
    }

    private boolean isSelectableNode(Object node) {
        return node instanceof LanguageTreeModel.LanguageImplNode
            || node instanceof LanguageTreeModel.LanguageRequestNode;
    }

    /**
     * Attaches a controller to the panel.
     *
     * @param controller The controller to attach; or <code>null</code> to detach.
     */
    public void attachController(@Nullable final LanguagesConfigurable controller) {
        this.controller = controller;

        if (controller != null) {
            final LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
            initPopupActions(model, controller);
        } else {
            deinitPopupActions();
        }
    }

    private void initPopupActions(final LanguageTreeModel model, final LanguagesConfigurable controller) {
        this.addActionPopupGroup = new DefaultActionGroup("Add", true);
        this.addActionPopupGroup.add(new AddLanguageFromDirectoryAction(model, controller));
        this.addActionPopupGroup.add(new AddLanguageFromArtifactAction(model, controller));
    }

    private void deinitPopupActions() {
        this.addActionPopupGroup = null;
    }

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
