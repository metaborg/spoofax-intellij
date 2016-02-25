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

package org.metaborg.intellij.idea.facets;

import com.google.common.collect.*;
import com.google.inject.*;
import com.intellij.facet.*;
import com.intellij.facet.frameworks.*;
import com.intellij.facet.frameworks.beans.*;
import com.intellij.facet.ui.*;
import com.intellij.facet.ui.libraries.*;
import com.intellij.ide.util.frameworkSupport.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.*;
import com.intellij.openapi.vfs.*;
import com.intellij.util.download.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;

import javax.swing.*;
import java.util.*;

public class MetaborgFacetEditorTab extends FacetEditorTab {

    private JPanel mainPanel;
    private LibraryService libraryService;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgFacetEditorTab(final FacetEditorContext editorContext,
                                  final FacetValidatorsManager validatorsManager) {
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Metaborg Facet Editor";
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void onFacetInitialized(@NotNull final Facet facet) {
        this.logger.debug("Facet initializing!");
        final Collection<VirtualFile> files = this.libraryService.downloadLibraries(Lists.newArrayList(
                "http://repo1.maven.org/maven2/org/codehaus/groovy/groovy-all/2.3.9/groovy-all-2.3.9.jar"));
//        final DownloadableFileService fileService = DownloadableFileService.getInstance();
//        final String targetPath = PathManager.getPluginsPath() + "/org.metaborg.intellij/lib/";
//        final String downloadUrl = "http://repo1.maven.org/maven2/org/codehaus/groovy/groovy-all/2.3.9/";
//        final String fileName = "groovy-all-2.3.9.jar";
//        final DownloadableFileDescription fileDescription = fileService.createFileDescription(downloadUrl + fileName, fileName);
//        @Nullable final List<VirtualFile> files = fileService.createDownloader(Lists.newArrayList(fileDescription), "").downloadFilesWithProgress(targetPath, null, null);
//        boolean success = files != null && files.size() == 1;

        this.logger.info("Facet initialized!");
    }

    @Override
    public void apply()
            throws ConfigurationException {
        super.apply();
    }

    @Override
    public void reset() {

    }

    @NotNull
    @Override
    public JComponent createComponent() {
        return this.mainPanel;
    }

    @Override
    public void disposeUIResources() {

    }
}
