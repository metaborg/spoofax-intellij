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

package org.metaborg.spoofax.intellij.languages;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.UnhandledException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages languages.
 */
@Singleton
public final class LanguageManager {

    @NotNull
    private final ILanguageService languageService;
    @NotNull
    private final ILanguageDiscoveryService discoveryService;
    @NotNull
    private final IIntelliJResourceService resourceService;
    @InjectLogger
    private Logger logger;

    @Inject
    private LanguageManager(
            @NotNull final ILanguageService languageService,
            @NotNull final ILanguageDiscoveryService discoveryService,
            @NotNull final IIntelliJResourceService resourceService) {
        this.languageService = languageService;
        this.discoveryService = discoveryService;
        this.resourceService = resourceService;
    }

    /**
     * Loads the meta languages.
     */
    public final void loadMetaLanguages() {
        loadLanguage("org.metaborg.meta.lang.esv-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.nabl-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.sdf-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.stratego-1.5.0-baseline-20150917-172646");
        loadLanguage("org.metaborg.meta.lang.template-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.ts-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lib.analysis-1.5.0-SNAPSHOT");

        loadLanguage("org.metaborg.meta.lang.esv-1.5.0-baseline-20150905-200051");
        loadLanguage("org.metaborg.meta.lang.sdf-1.5.0-baseline-20150905-200051");
    }

    /**
     * Loads the language with the specified ID.
     *
     * @param id The ID.
     */
    private void loadLanguage(@NotNull final String id) {
        final URL url = this.getClass().getClassLoader().getResource("meta-languages/" + id + ".spoofax-language");
        if (url == null) {
            logger.error("Meta language '" + id + "' could not be resolved to a class path.");
            return;
        }

        final FileObject file = this.resourceService.resolve(url.getPath());

        try {
            loadLanguageFromArtifact(file);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }

//        final String zipUri = "zip://" + url.getPath();
//        final FileObject file = this.resourceService.resolve(zipUri);
//        try {
//            if (!file.exists()) {
//                logger.error("Meta language '" + id + "' does not exist in classpath at: " + file.toString());
//                return;
//            }
//        } catch (FileSystemException e) {
//            e.printStackTrace();
//        }
//        try {
//            final Iterable<ILanguageComponent> discovery = this.discoveryService.discover(file);
//            final List<ILanguageImpl> lis = new ArrayList<ILanguageImpl>();
//            for (ILanguageComponent c : discovery) {
//                for (ILanguageImpl li : c.contributesTo()) {
//                    lis.add(li);
//                }
//            }
//            logger.info("For '" + id + "' loaded languages: " + Joiner.on(", ").join(lis));
//        } catch (MetaborgException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * Loads a language from a language artifact.
     *
     * @param artifact The artifact file.
     * @return Whether a language was successfully loaded.
     */
    public boolean loadLanguageFromArtifact(@NotNull final VirtualFile artifact) throws
            IOException {
        Preconditions.checkNotNull(artifact);

        return loadLanguageFromArtifact(this.resourceService.resolve(artifact));
    }

    /**
     * Loads a language from a language artifact.
     *
     * @param artifact The artifact file.
     * @return Whether a language was successfully loaded.
     */
    public boolean loadLanguageFromArtifact(@NotNull final FileObject artifact) throws
            IOException {
        Preconditions.checkNotNull(artifact);

        final String zipUri;
        try {
            zipUri = "zip://" + artifact.getURL().getPath();
        } catch (FileSystemException e) {
            throw new UnhandledException(e);
        }

        final FileObject file = this.resourceService.resolve(zipUri);
//        try {
//            file.ex
//            if (!file.exists()) {
//                logger.error("Language artifact not found at: {}", file);
//
//            }
//        } catch (FileSystemException e) {
//            throw new RuntimeException("Unhandled exception", e);
//        }

        return loadLanguageFromFolder(file);
    }

    /**
     * Loads a language from a folder.
     *
     * @param folder The folder.
     * @return Whether a language was successfully loaded.
     */
    public boolean loadLanguageFromFolder(VirtualFile folder) throws
            IOException {
        Preconditions.checkNotNull(folder);

        return loadLanguageFromFolder(this.resourceService.resolve(folder));
    }

    /**
     * Loads a language from a folder.
     *
     * @param folder The folder.
     * @return Whether a language was successfully loaded.
     */
    public boolean loadLanguageFromFolder(FileObject folder) throws
            IOException {
        Preconditions.checkNotNull(folder);
        try {
            // TODO: Assert that this doesn't load the language, just discovers it.
            // Loading happens only after the user clicked OK or Apply in the settings dialog.
            final Iterable<ILanguageComponent> discovery = this.discoveryService.discover(folder);
            final List<ILanguageImpl> lis = new ArrayList<>();
            for (ILanguageComponent c : discovery) {
                for (ILanguageImpl li : c.contributesTo()) {
                    lis.add(li);
                }
            }
            logger.info("From '{}' loaded languages: {}", folder, Joiner.on(", ").join(lis));
            return discovery.iterator().hasNext();
        } catch (MetaborgException e) {
            throw new UnhandledException(e);
        }
    }
}
