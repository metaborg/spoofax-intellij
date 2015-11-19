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

import com.google.common.collect.Lists;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.dualView.TreeTableView;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModel;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeColumnInfo;
import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.util.ui.ColumnInfo;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.idea.gui.LanguageTreeModel;
import org.metaborg.idea.gui.LanguageTreeTableView;
import org.metaborg.idea.gui.LanguagesConfigurable;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class LanguagesPanel extends JPanel {

    private final TreeTable languagesTree;
    @Nullable
    private LanguagesConfigurable controller;

    public LanguagesPanel() {
        super(new BorderLayout());

        this.languagesTree = new TreeTableView(new ListTreeTableModelOnColumns(new DefaultMutableTreeNode(""), new ColumnInfo[]{
                new TreeColumnInfo("Language"),
                new ColumnInfo("Version") {
                    @org.jetbrains.annotations.Nullable
                    @Override
                    public Object valueOf(final Object obj) {
                        if (obj instanceof LanguageTreeModel.LanguageNode) {
                            LanguageTreeModel.LanguageNode node = (LanguageTreeModel.LanguageNode)obj;
                            return null;
//                            return node.getValue().name();
                        }
                        if (obj instanceof LanguageTreeModel.LanguageImplNode) {
                            LanguageTreeModel.LanguageImplNode node = (LanguageTreeModel.LanguageImplNode)obj;
                            return node.getValue().id().version;
                        }
                        return null;
                    }
                },
        }));
        this.languagesTree.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(this.languagesTree)
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
        ListTreeTableModelOnColumns model = (ListTreeTableModelOnColumns)this.languagesTree.getTableModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        root.removeAllChildren();
        model.reload();

        Map<ILanguage, LanguageTreeModel.LanguageNode> languageNodes = new HashMap<>();
        for (ILanguageImpl impl : languageImpls) {
            ILanguage language = impl.belongsTo();
            LanguageTreeModel.LanguageNode languageNode = languageNodes.get(language);
            if (languageNode == null) {
                languageNode = new LanguageTreeModel.LanguageNode(language);
                languageNodes.put(language, languageNode);
                model.insertNodeInto(languageNode, root, root.getChildCount());
            }
            LanguageTreeModel.LanguageImplNode languageImplNode = new LanguageTreeModel.LanguageImplNode(impl);
            model.insertNodeInto(languageImplNode, languageNode, languageNode.getChildCount());
        }
        model.reload();
    }

    /**
     * Gets the currently selected language.
     *
     * @return The currently selected language;
     * or <code>null</code> when none is selected.
     */
    @Nullable
    public ILanguage selectedLanguage() {
        return null;
    }

    /**
     * Sets the currently selected language.
     *
     * @param language The currently selected language;
     * or <code>null</code> to select none.
     */
    public void selectLanguage(@Nullable ILanguage language) {
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
