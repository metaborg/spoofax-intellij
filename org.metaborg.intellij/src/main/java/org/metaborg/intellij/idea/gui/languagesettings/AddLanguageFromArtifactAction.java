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

package org.metaborg.intellij.idea.gui.languagesettings;

import com.google.inject.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.*;
import com.intellij.util.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.filetypes.*;
import org.metaborg.intellij.idea.gui.languagespanel.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.io.*;

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
                                         final LanguagesConfiguration controller) {
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
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
                    "Unhandled exception while requesting languages from artifact: {}", ex, file);
        }
    }
}
