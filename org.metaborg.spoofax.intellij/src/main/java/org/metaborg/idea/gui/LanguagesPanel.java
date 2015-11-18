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

package org.metaborg.idea.gui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.dualView.TreeTableView;
import com.intellij.util.ui.tree.AbstractFileTreeTable;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

public final class LanguagesPanel extends JPanel {
//    private final JBList languagesList;
    private final TreeTableView languagesTree;
    @Nullable
    private LanguagesConfigurable controller;

    public LanguagesPanel() {
        super(new BorderLayout());

        LanguageTreeModel model = new LanguageTreeModel();
//        model.getRoot().add(new LanguageTreeModel.ProjectNode("Child 1"));
//        model.getRoot().add(new LanguageTreeModel.ProjectNode("Child 2"));
        this.languagesTree = new TreeTableView(model);
        this.languagesTree.setTreeCellRenderer(new TreeModelNodeCellRenderer());
        this.languagesTree.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // See:
        // https://github.com/consulo/consulo-android/blob/4c18d652a777c1d36eea3acbce4b1ba7556f31ed/android/sdk-updates/src/com/android/tools/idea/updater/configure/PlatformComponentsPanel.java
        // https://github.com/consulo/consulo-android/blob/4c18d652a777c1d36eea3acbce4b1ba7556f31ed/android/sdk-updates/src/com/android/tools/idea/updater/configure/ToolComponentsPanel.java
        // https://github.com/liveqmock/platform-tools-idea/blob/1c4b76108add6110898a7e3f8f70b970e352d3d4/plugins/IntelliLang/java-support/org/intellij/plugins/intelliLang/inject/config/ui/MethodParameterPanel.java
        // https://github.com/JetBrains/intellij-community/blob/210e0ed138627926e10094bb9c76026319cec178/plugins/IntelliLang/java-support/org/intellij/plugins/intelliLang/inject/config/ui/MethodParameterPanel.java
//        this.languagesList = new JBList(new DefaultListModel<>());
//        this.languagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        this.languagesList.setCellRenderer(new LanguageRenderer());
//        DoubleClickListener doubleClickListener = new DoubleClickListener() {
//            @Override
//            protected boolean onDoubleClick(MouseEvent e) {
//                ILanguage language = selectedLanguage();
//                if (controller != null && controller.canEditLanguage(language)) {
//                    controller.editLanguage(language);
//                }
//                return true;
//            }
//        };
//        doubleClickListener.installOn(this.languagesList);
//
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(this.languagesTree)
//        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(this.languagesList)
                .setAddAction(button -> addLanguage())
                .setAddActionUpdater(e -> canAddLanguage())
                .setRemoveAction(button -> removeLanguage(selectedLanguage()))
                .setRemoveActionUpdater(e -> canRemoveLanguage(selectedLanguage()))
                .setEditAction(button -> editLanguage(selectedLanguage()))
                .setEditActionUpdater(e -> canEditLanguage(selectedLanguage()))
                .disableUpDownActions();

        add(toolbarDecorator.createPanel(), BorderLayout.CENTER);
        setBorder(IdeBorderFactory.createTitledBorder("Loaded languages", false));
    }

    private void addLanguage() {
        if (this.controller == null || !canAddLanguage())
            return;
        this.controller.addLanguage();
    }
    private boolean canAddLanguage() {
        return this.controller != null;
    }
    private void removeLanguage(final ILanguage language) {
        if (this.controller == null || !canRemoveLanguage(language))
            return;
        this.controller.removeLanguage(language);
    }
    private boolean canRemoveLanguage(final ILanguage language) {
        return this.controller != null
                && language != null
                && this.controller.canRemoveLanguage(language);
    }
    private void editLanguage(final ILanguage language) {
        if (this.controller == null || !canEditLanguage(language))
            return;
        this.controller.editLanguage(language);
    }
    private boolean canEditLanguage(final ILanguage language) {
        return this.controller != null
                && language != null
                && this.controller.canEditLanguage(language);
    }

    public void setLanguages(Iterable<ILanguageImpl> languageImpls) {
        LanguageTreeModel model = (LanguageTreeModel)this.languagesTree.getTableModel();
        DefaultMutableTreeNode root = model.getRoot();
        root.removeAllChildren();
        model.reload();

        Map<ILanguage, LanguageTreeModel.LanguageNode> languageNodes = new HashMap<>();
        for (ILanguageImpl impl : languageImpls) {
            ILanguage language = impl.belongsTo();
            LanguageTreeModel.LanguageNode languageNode = languageNodes.get(language);
            if (languageNode == null) {
                languageNode = new LanguageTreeModel.LanguageNode(language);
                languageNodes.put(language, languageNode);
                model.appendNodeInto(languageNode, root);
            }
            LanguageTreeModel.LanguageImplNode languageImplNode = new LanguageTreeModel.LanguageImplNode(impl);
            model.appendNodeInto(languageImplNode, languageNode);
        }
        model.reload();
//        for (ILanguage language : languages) {
//            LanguageTreeModel.LanguageNode languageNode = new LanguageTreeModel.LanguageNode(language);
//            model.appendNodeInto(languageNode, root);
////            root.add(languageNode);
//            for (ILanguageImpl impl : language.impls()) {
//                LanguageTreeModel.LanguageImplNode languageImplNode = new LanguageTreeModel.LanguageImplNode(impl);
//                model.appendNodeInto(languageImplNode, languageNode);
//            }
//        }
//        ScrollingUtil.ensureSelectionExists(this.languagesList);
    }

    /**
     * Gets the currently selected language.
     *
     * @return The currently selected language;
     * or <code>null</code> when none is selected.
     */
    @Nullable
    public ILanguage selectedLanguage() {
        java.util.List selection = this.languagesTree.getSelection();
        if (selection.size() == 1) {
            Object selected = selection.get(0);
            if (selected instanceof LanguageTreeModel.LanguageNode) {
                LanguageTreeModel.LanguageNode node = (LanguageTreeModel.LanguageNode)selected;
                return node.getValue();
            }
        }
        return null;
    }

    /**
     * Sets the currently selected language.
     *
     * @param language The currently selected language;
     * or <code>null</code> to select none.
     */
    public void selectLanguage(@Nullable ILanguage language) {
//        this.languagesList.clearSelection();
//        this.languagesList.setSelectedValue(language, true);
//        this.languagesList.requestFocus();
    }

    /**
     * Attaches a controller to the panel.
     *
     * @param controller The controller to attach; or <code>null</code> to detach.
     */
    public void attachController(@Nullable final LanguagesConfigurable controller) {
        this.controller = controller;
    }

}
