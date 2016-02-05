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

import com.google.inject.Inject;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import org.metaborg.core.UnhandledException;
import org.metaborg.core.language.*;
import org.metaborg.spoofax.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.spoofax.intellij.idea.vfs.SpoofaxArtifactFileType;
import org.metaborg.spoofax.intellij.languages.LanguageManager;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.Collections;

//import org.metaborg.core.language.ILanguageDiscoveryService;


public abstract class LanguagesAction extends AnAction {

    private ILanguageService languageService;
    private IIdeaLanguageManager ideaLanguageManager;
    private ILanguageDiscoveryService discoveryService;
    private SpoofaxArtifactFileType artifactFileType;
    private LanguageManager languageManager;
    private final ListTreeTableModelOnColumns model;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public LanguagesAction(final ListTreeTableModelOnColumns model, @Nullable final String text, @Nullable final String description, @Nullable final Icon icon/*,
                           final LanguageManager languageManager, final ILanguageDiscoveryService discoveryService,
                           final ILanguageService languageService, final IIdeaLanguageManager ideaLanguageManager,
                           final SpoofaxArtifactFileType artifactFileType*/) {
        super(text, description, icon);
        this.model = model;
//        this.languageService = languageService;
//        this.ideaLanguageManager = ideaLanguageManager;
//        this.artifactFileType = artifactFileType;
//        this.discoveryService = discoveryService;
//        this.languageManager = languageManager;
    }
//
//    @Inject
//    @SuppressWarnings("unused")
//    private void inject(
//            final LanguageManager languageManager, final ILanguageDiscoveryService discoveryService,
//            final ILanguageService languageService, final IIdeaLanguageManager ideaLanguageManager,
//            final SpoofaxArtifactFileType artifactFileType) {
//        this.languageService = languageService;
//        this.ideaLanguageManager = ideaLanguageManager;
//        this.artifactFileType = artifactFileType;
//        this.discoveryService = discoveryService;
//        this.languageManager = languageManager;
//    }

//    @Nullable
//    protected LanguageTreeModel.LanguageNode getOrCreateLanguageNode(ILanguage language) {
//        final DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
//
//        final BreathFirstIterable<TreeNode> iterator = new BreathFirstIterable<TreeNode>(root) {
//            @Override
//            protected Iterable getChildren(final TreeNode node) {
//                return Collections.list(node.children());
//            }
//        };
//
//        for (TreeNode node : iterator) {
//            if (node instanceof LanguageTreeModel.LanguageNode) {
//                LanguageTreeModel.LanguageNode languageNode = (LanguageTreeModel.LanguageNode)node;
//                if (languageNode.getValue().equals(language))
//                    return languageNode;
//            }
//        }
//
//        return null;
//    }



    @Nullable
    protected LanguageTreeModel.LanguageNode getOrCreateLanguageNode(final ILanguage language) {
        @Nullable final LanguageTreeModel.LanguageNode node = getNode(LanguageTreeModel.LanguageNode.class, language);
        if (node == null) {
            // TODO: Create node.
        }
        // TODO
        return null;
    }

    @Nullable
    protected <N extends LanguageTreeModel.ILanguageTreeNode<V>, V> N getNode(
            final Class<N> clazz, final V value) {
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.model.getRoot();

        final BreathFirstIterable<TreeNode> iterator = new BreathFirstIterable<TreeNode>(root) {
            @Override
            protected Iterable getChildren(final TreeNode node) {
                return Collections.list(node.children());
            }
        };

        for (final TreeNode node : iterator) {
            if (node.getClass().equals(clazz)) {
                final N languageNode = (N)node;
                @Nullable final V languageNodeValue = languageNode.getValue();
                if (languageNodeValue != null && languageNodeValue.equals(value))
                    return languageNode;
            }
        }

        return null;
    }

//    @Nullable
//    protected LanguageTreeModel.LanguageNode getLanguageNode(ILanguage language) {
//        final DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
//        return getLanguageNode(language, root);
//    }
//
//    @Nullable
//    private LanguageTreeModel.LanguageNode getLanguageNode(ILanguage language, TreeNode parent) {
//        for (int i = 0; i < parent.getChildCount(); i++) {
//            final TreeNode child = parent.getChildAt(i);
//            if (child instanceof LanguageTreeModel.LanguageNode) {
//                LanguageTreeModel.LanguageNode languageChild = (LanguageTreeModel.LanguageNode)child;
//                if (languageChild.getValue().equals(language))
//                    return languageChild;
//            }
//        }
//        return null;
//    }
//
//    @Nullable
//    protected LanguageTreeModel.LanguageImplNode getLanguageImplNode(ILanguageImpl languageImpl) {
//        final DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
//        for (int i = 0; i < root.getChildCount(); i++) {
//            final TreeNode child = root.getChildAt(i);
//            @Nullable final LanguageTreeModel.LanguageImplNode languageImplNode = getLanguageImplNode(languageImpl, child);
//            if (languageImplNode != null)
//                return languageImplNode;
//        }
//        return null;
//    }
//
//    @Nullable
//    private LanguageTreeModel.LanguageImplNode getLanguageImplNode(ILanguageImpl languageImpl, TreeNode parent) {
//        for (int i = 0; i < parent.getChildCount(); i++) {
//            final TreeNode child = parent.getChildAt(i);
//            if (child instanceof LanguageTreeModel.LanguageImplNode) {
//                @Nullable LanguageTreeModel.LanguageImplNode languageChild = (LanguageTreeModel.LanguageImplNode)child;
//                if (languageChild.getValue().equals(languageImpl))
//                    return languageChild;
//            }
//        }
//        return null;
//    }

    @Override
    public abstract void actionPerformed(final AnActionEvent e);
}
