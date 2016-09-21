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

package org.metaborg.intellij.idea.gui.languagesettings;

import com.google.inject.Inject;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformIcons;
import org.metaborg.core.language.*;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.filetypes.LanguageArtifactFileType;
import org.metaborg.intellij.idea.gui.languagespanel.LanguageTreeModel;
import org.metaborg.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.util.log.*;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Action to add a language from an language artifact.
 */
public class AddLanguageFromArtifactAction extends LanguagesAction {

    private IIdeaLanguageManager languageManager;
    private LanguageArtifactFileType artifactFileType;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public AddLanguageFromArtifactAction(final LanguageTreeModel model,
                                         final LanguagesSettings controller) {
        super(model, controller, "Artifact...",
                "Add a language by specifying its artifact.", PlatformIcons.JAR_ICON);
        // TODO: Remove this and use a factory instead.
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(
            final IIdeaLanguageManager languageManager,
            final LanguageArtifactFileType artifactFileType) {
        this.artifactFileType = artifactFileType;
        this.languageManager = languageManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        final FileChooserDescriptor chooserDescriptor = FileChooserDescriptorFactory
                .createSingleFileDescriptor(this.artifactFileType);
        @Nullable final VirtualFile file = FileChooser.chooseFile(chooserDescriptor, project, null);
        if (file == null)
            return;

        try {
            final Iterable<ILanguageDiscoveryRequest> requests = this.languageManager.requestFromArtifact(file);
            addRequests(requests);
        } catch (final IOException ex) {
            throw LoggerUtils2.exception(this.logger, UnhandledException.class,
                    "Unhandled exception while requesting languages from artifact: {}", ex, file);
        }
    }
}
