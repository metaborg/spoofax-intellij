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

package org.metaborg.intellij.resources;

import com.google.inject.Inject;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.download.DownloadableFileDescription;
import com.intellij.util.download.DownloadableFileService;
import intellij.org.apache.commons.io.*;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.util.log.*;

import java.util.Collection;
import java.util.List;

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
            throw LoggerUtils2.exception(this.logger, RuntimeException.class, "Not all files were downloaded!");
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
