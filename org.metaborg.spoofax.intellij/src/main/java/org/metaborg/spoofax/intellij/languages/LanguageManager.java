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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.UnhandledException;
import org.metaborg.core.language.*;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.ILogger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;

/**
 * Manages languages.
 */
@Singleton
public final class LanguageManager {

    private final ILanguageService languageService;
    private final ILanguageDiscoveryService discoveryService;
    private final IIntelliJResourceService resourceService;
    @InjectLogger
    private ILogger logger;

    @Inject
    private LanguageManager(
            final ILanguageService languageService,
            final ILanguageDiscoveryService discoveryService,
            final IIntelliJResourceService resourceService) {
        this.languageService = languageService;
        this.discoveryService = discoveryService;
        this.resourceService = resourceService;
    }

    /**
     * Loads the meta languages.
     */
    public final void loadMetaLanguages() {
        // TODO: Let the user load language artifacts of meta languages,
        // and store them for the next session,
        // and use those instead of this fixed list.

        loadLanguage("org.metaborg.meta.lang.esv-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.nabl-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.sdf-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.stratego-1.5.0-baseline-20150917-172646");
        loadLanguage("org.metaborg.meta.lang.template-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.ts-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lib.analysis-1.5.0-SNAPSHOT");

        loadLanguage("org.metaborg.meta.lang.esv-1.5.0-baseline-20150917-172646");
        loadLanguage("org.metaborg.meta.lang.sdf-1.5.0-baseline-20150917-172646");
    }

    /**
     * Loads the language with the specified ID.
     *
     * @param id The ID.
     */
    private void loadLanguage(final String id) {
        final URL url = this.getClass().getClassLoader().getResource("languages/" + id + ".spoofax-language");
        if (url == null) {
            this.logger.error("Meta language '{}' could not be resolved to a class path.", id);
            return;
        }

        final FileObject file = this.resourceService.resolve(url.getPath());

        try {
            loadLanguages(requestLanguagesFromArtifact(file));
        } catch (final IOException e) {
            throw new UnhandledException(e);
        } catch (MetaborgException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Loads languages from the specified requests.
     *
     * @param requests The language discovery requests.
     * @return The loaded languages.
     * @throws MetaborgException
     */
    public Iterable<ILanguageComponent> loadLanguages(final Iterable<ILanguageDiscoveryRequest> requests) throws
            MetaborgException {
        final Iterable<ILanguageComponent> components = this.discoveryService.discover(requests);
//        this.ideaLanguageManager.unload(language);
        return components;
    }

    /**
     * Unloads the specified language components.
     *
     * @param components The components to unload.
     */
    public void unloadLanguages(final Iterable<ILanguageComponent> components) {
        for (final ILanguageComponent component : components) {
            this.languageService.remove(component);
            // this.ideaLanguageManager.unload(language);
        }
    }

    /**
     * Requests languages from a language artifact.
     *
     * @param artifact The artifact file.
     * @return The language discovery requests.
     */
    public Iterable<ILanguageDiscoveryRequest> requestLanguagesFromArtifact(final VirtualFile artifact) throws
            IOException {
        Preconditions.checkNotNull(artifact);

        return requestLanguagesFromArtifact(this.resourceService.resolve(artifact));
    }

    /**
     * Requests languages from a language artifact.
     *
     * @param artifact The artifact file.
     * @return The language discovery requests.
     */
    public Iterable<ILanguageDiscoveryRequest> requestLanguagesFromArtifact(final FileObject artifact) throws
            IOException {
        Preconditions.checkNotNull(artifact);

        final String zipUri;
        try {
            zipUri = "zip://" + artifact.getURL().getPath();
        } catch (final FileSystemException e) {
            throw new UnhandledException(e);
        }

        final FileObject file = this.resourceService.resolve(zipUri);
        return requestLanguagesFromFolder(file);
    }

    /**
     * Requests languages from a folder.
     *
     * @param folder The folder.
     * @return The language discovery requests.
     */
    public Iterable<ILanguageDiscoveryRequest> requestLanguagesFromFolder(final VirtualFile folder) throws
            IOException {
        Preconditions.checkNotNull(folder);

        return requestLanguagesFromFolder(this.resourceService.resolve(folder));
    }

    /**
     * Requests languages from a folder.
     *
     * @param folder The folder.
     * @return The language discovery requests.
     */
    public Iterable<ILanguageDiscoveryRequest> requestLanguagesFromFolder(final FileObject folder) throws
            IOException {
        Preconditions.checkNotNull(folder);

        @Nullable LanguageIdentifier id = null;
        try {
            // TODO: Assert that this doesn't load the language, just discovers it.
            // Loading happens only after the user clicked OK or Apply in the settings dialog.
            return this.discoveryService.request(folder);
        } catch (final MetaborgException e) {
            throw new UnhandledException(e);
        }
    }

//    /**
//     * Loads a language from a language artifact.
//     *
//     * @param artifact The artifact file.
//     * @return Whether a language was successfully loaded.
//     */
//    public boolean loadLanguagesFromArtifact(final VirtualFile artifact) throws
//            IOException {
//        Preconditions.checkNotNull(artifact);
//
//        return loadLanguagesFromArtifact(this.resourceService.resolve(artifact));
//    }
//
//    /**
//     * Loads a language from a language artifact.
//     *
//     * @param artifact The artifact file.
//     * @return Whether a language was successfully loaded.
//     */
//    public boolean loadLanguagesFromArtifact(final FileObject artifact) throws
//            IOException {
//        Preconditions.checkNotNull(artifact);
//
//        final String zipUri;
//        try {
//            zipUri = "zip://" + artifact.getURL().getPath();
//        } catch (final FileSystemException e) {
//            throw new UnhandledException(e);
//        }
//
//        final FileObject file = this.resourceService.resolve(zipUri);
//        return loadLanguagesFromFolder(file);
//    }
//
//    /**
//     * Loads a language from a folder.
//     *
//     * @param folder The folder.
//     * @return Whether a language was successfully loaded.
//     */
//    public boolean loadLanguagesFromFolder(final VirtualFile folder) throws
//            IOException {
//        Preconditions.checkNotNull(folder);
//
//        return loadLanguagesFromFolder(this.resourceService.resolve(folder));
//    }
//
//    /**
//     * Loads a language from a folder.
//     *
//     * @param folder The folder.
//     * @return Whether a language was successfully loaded.
//     */
//    public boolean loadLanguagesFromFolder(final FileObject folder) throws
//            IOException {
//        Preconditions.checkNotNull(folder);
//
//        @Nullable LanguageIdentifier id = null;
//        try {
//            // TODO: Assert that this doesn't load the language, just discovers it.
//            // Loading happens only after the user clicked OK or Apply in the settings dialog.
//            final Iterable<ILanguageDiscoveryRequest> requests = this.discoveryService.request(folder);
//            for (final ILanguageDiscoveryRequest request : requests) {
//                if (request.available()) {
//                    @Nullable final ILanguageComponentConfig config = request.config();
//                    assert config != null : "Configuration should not be null since the request is available.";
//
//                    if (id != null && id != config.identifier()) {
//                        // Found
//                    }
//
//                    id = config.identifier();
//                }
//            }
//            final Iterable<ILanguageComponent> discovery = this.discoveryService.discover(request);
//            final List<ILanguageImpl> lis = new ArrayList<>();
//            for (final ILanguageComponent c : discovery) {
//                for (final ILanguageImpl li : c.contributesTo()) {
//                    lis.add(li);
//                }
//            }
//            this.logger.info("From '{}' loaded languages: {}", folder, Joiner.on(", ").join(lis));
//            return discovery.iterator().hasNext();
//        } catch (final MetaborgException e) {
//            throw new UnhandledException(e);
//        }
//    }
}
