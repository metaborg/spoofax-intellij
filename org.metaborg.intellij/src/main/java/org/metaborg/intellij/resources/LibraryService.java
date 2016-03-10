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

package org.metaborg.intellij.resources;

import com.google.common.collect.*;
import com.google.inject.*;
import com.intellij.facet.ui.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.vfs.*;
import com.intellij.util.containers.*;
import com.intellij.util.download.*;
import intellij.org.apache.commons.io.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * Library service.
 */
public final class LibraryService {

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link LibraryService} class.
     */
    @Inject
    public LibraryService() {
    }

    /**
     * Gets the plugin's <code>lib/</code> folder.
     *
     * @return The plugin's <code>lib/</code> folder.
     */
    public String getPluginLibPath() {
        return PathManager.getPluginsPath() + "/org.metaborg.intellij/lib/";
    }

    /**
     * Download libraries from the specified URLs.
     *
     * @param urls The URLs of the libraries.
     * @return The downloaded libraries.
     */
    public Collection<VirtualFile> downloadLibraries(final Collection<String> urls) {
        return downloadFiles(urls, getPluginLibPath());
    }

    /**
     * Download files from the specified URLs.
     *
     * @param urls The URLs of the files.
     * @param targetPath The path to download the file to.
     * @return The downloaded files.
     */
    private Collection<VirtualFile> downloadFiles(final Collection<String> urls, final String targetPath) {

        final DownloadableFileService fileService = DownloadableFileService.getInstance();

        final List<DownloadableFileDescription> fileDescriptions
                = ContainerUtil.map(urls, (u) -> toFileDescription(fileService, u));

        @Nullable final List<VirtualFile> files = fileService
                .createDownloader(fileDescriptions, "")
                .downloadFilesWithProgress(targetPath, null, null);
        if (files == null || files.size() != fileDescriptions.size()) {
            throw LoggerUtils.exception(this.logger, RuntimeException.class, "Not all files were downloaded!");
        }
        return files;
    }

    /**
     * Gets the file description of the specified URL.
     *
     * @param fileService The file service.
     * @param url The URL.
     * @return The file description.
     */
    private DownloadableFileDescription toFileDescription(final DownloadableFileService fileService, final String url) {
        final String filename = FilenameUtils.getName(url);
        return fileService.createFileDescription(url, filename);
    }

}
