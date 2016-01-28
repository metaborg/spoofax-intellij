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

package org.metaborg.intellij.idea.gui2;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.dualView.TreeTableView;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.util.ui.ColumnInfo;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.intellij.idea.gui.LanguageTreeModel;
import org.metaborg.intellij.idea.gui.LanguagesConfigurable;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxIcons;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public final class LanguagesPanel extends JPanel {

    private final TreeTableView languagesTree;
    @Nullable
    private LanguagesConfigurable controller;
//    @Nullable
//    private List<PopupAction> myPopupActions = null;

    public LanguagesPanel() {
        super(new BorderLayout());

        this.languagesTree = new TreeTableView(new ListTreeTableModelOnColumns(
                new DefaultMutableTreeNode(""),
                new ColumnInfo[]{
                        new TreeKeyColumnInfo("Language"),
                        new ColumnInfo("Group ID") {
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Object valueOf(final Object obj) {
                                if (obj instanceof LanguageTreeModel.LanguageImplNode) {
                                    LanguageTreeModel.LanguageImplNode node = (LanguageTreeModel.LanguageImplNode) obj;
                                    return node.getValue().id().groupId;
                                }
                                return null;
                            }
                        },
                        new ColumnInfo("Version") {
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Object valueOf(final Object obj) {
                                if (obj instanceof LanguageTreeModel.LanguageImplNode) {
                                    LanguageTreeModel.LanguageImplNode node = (LanguageTreeModel.LanguageImplNode) obj;
                                    return node.getValue().id().version;
                                }
                                return null;
                            }

//                            @org.jetbrains.annotations.Nullable
//                            @Override
//                            public TableCellRenderer getRenderer(
//                                    final Object o) {
//                                return super.getRenderer(o);
//                            }
                        },
                }
        ));
        this.languagesTree.setTreeCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(
                    final JTree tree,
                    final Object value,
                    final boolean sel,
                    final boolean expanded,
                    final boolean leaf,
                    final int row,
                    final boolean hasFocus) {

                Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                if (value instanceof LanguageTreeModel.LanguageNode) {
                    LanguageTreeModel.LanguageNode node = (LanguageTreeModel.LanguageNode) value;
                    setText(node.getValue().name());
                    setIcon(SpoofaxIcons.INSTANCE.Default);
                }
                if (value instanceof LanguageTreeModel.LanguageImplNode) {
                    LanguageTreeModel.LanguageImplNode node = (LanguageTreeModel.LanguageImplNode) value;
                    setText(node.getValue().id().id);
                }

                return c;
            }
        });
        this.languagesTree.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(this.languagesTree)
                .setAddAction(this::addLanguage)
                .setAddActionUpdater(e -> canAddLanguage())
                .setRemoveAction(button -> removeLanguage(selectedLanguageImpl()))
                .setRemoveActionUpdater(e -> canRemoveLanguage(selectedLanguageImpl()))
//                .setEditAction(button -> editLanguage(selectedLanguageImpl()))
//                .setEditActionUpdater(e -> canEditLanguage(selectedLanguageImpl()))
                .disableUpDownActions();

        add(toolbarDecorator.createPanel(), BorderLayout.CENTER);
        setBorder(IdeBorderFactory.createTitledBorder("Loaded languages", false));
    }

    private void addLanguage(AnActionButton button) {
        if (this.controller == null || !canAddLanguage())
            return;
//        this.controller.addLanguage();
        showPopupActions(button);
    }

    private boolean canAddLanguage() {
        return this.controller != null;
    }

    private void removeLanguage(final ILanguageImpl language) {
        if (this.controller == null || !canRemoveLanguage(language))
            return;
        this.controller.removeLanguage(language);
    }

    private boolean canRemoveLanguage(final ILanguageImpl language) {
        return this.controller != null
                && language != null;
//                && this.controller.canRemoveLanguage(language);
    }
//
//    private void editLanguage(final ILanguageImpl language) {
//        if (this.controller == null || !canEditLanguage(language))
//            return;
//        this.controller.editLanguage(language);
//    }
//
//    private boolean canEditLanguage(final ILanguageImpl language) {
//        return this.controller != null
//                && language != null
//                && this.controller.canEditLanguage(language);
//    }

    public void setLanguages(Iterable<ILanguageImpl> languageImpls) {
        ListTreeTableModelOnColumns model = (ListTreeTableModelOnColumns) this.languagesTree.getTableModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
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
    public ILanguageImpl selectedLanguageImpl() {
        List<ILanguageImpl> languages = ((List<Object>) this.languagesTree.getSelection()).stream()
                .filter(x -> x instanceof LanguageTreeModel.LanguageImplNode)
                .map(x -> ((LanguageTreeModel.LanguageImplNode) x).getValue())
                .collect(toList());
        if (languages.size() != 1)
            return null;
        return languages.get(0);
    }

//    /**
//     * Sets the currently selected language.
//     *
//     * @param language The currently selected language;
//     *                 or <code>null</code> to select none.
//     */
//    public void selectLanguage(@Nullable ILanguageImpl language) {
//    }

    /**
     * Attaches a controller to the panel.
     *
     * @param controller The controller to attach; or <code>null</code> to detach.
     */
    public void attachController(@Nullable final LanguagesConfigurable controller) {
        this.controller = controller;
    }

    private void showPopupActions(AnActionButton button) {
        ActionGroup group = (ActionGroup) ActionManager.getInstance().getAction("Spoofax.AddLanguagePopup");

        JBPopupFactory.getInstance().createActionGroupPopup(null, group,
                                                            DataManager.getInstance().getDataContext(button.getContextComponent()),
                                                            JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, true
        ).show(
                button.getPreferredPopupPoint());
    }

}
