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
    protected final LanguageTreeModel model;
    protected final LanguagesConfigurable controller;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public LanguagesAction(final LanguageTreeModel model, final LanguagesConfigurable controller, @Nullable final String text, @Nullable final String description, @Nullable final Icon icon/*,
                           final LanguageManager languageManager, final ILanguageDiscoveryService discoveryService,
                           final ILanguageService languageService, final IIdeaLanguageManager ideaLanguageManager,
                           final SpoofaxArtifactFileType artifactFileType*/) {
        super(text, description, icon);
        this.model = model;
        this.controller = controller;
//        this.languageService = languageService;
//        this.ideaLanguageManager = ideaLanguageManager;
//        this.artifactFileType = artifactFileType;
//        this.discoveryService = discoveryService;
//        this.languageManager = languageManager;
    }

    @Override
    public abstract void actionPerformed(final AnActionEvent e);

    protected void addRequests(Iterable<ILanguageDiscoveryRequest> requests) {

        for (final ILanguageDiscoveryRequest request : requests) {
            this.controller.addLanguageRequest(request);
            this.model.getOrAddLanguageRequestNode(request);
        }

        // TODO: 'Discover' any requests in the tree upon OK.
        // TODO: Notify when not successful.
    }
}
