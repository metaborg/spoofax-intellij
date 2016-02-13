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

package org.metaborg.intellij.languages;

import com.google.common.collect.*;
import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.discovery.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

/**
 * Default implementation of the {@link ILanguageManager} interface.
 *
 * This implementation is thread-safe.
 */
@Singleton
public class DefaultLanguageManager implements ILanguageManager {

    // FIXME: The ILanguageService and ILanguageDiscoveryService implementations should be thread-safe too.

    private final Object objectLock = new Object();
    protected final ILanguageDiscoveryService discoveryService;
    protected final ILanguageService languageService;
    protected final ILanguageSource languageSource;

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link DefaultLanguageManager} class.
     */
    @Inject
    public DefaultLanguageManager(final ILanguageService languageService,
                                  final ILanguageSource languageSource,
                                  final ILanguageDiscoveryService discoveryService) {
        this.languageService = languageService;
        this.languageSource = languageSource;
        this.discoveryService = discoveryService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ILanguage> getLoadedLanguages() {
        return Lists.newArrayList(this.languageService.getAllLanguages());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ILanguageComponent> discover(final LanguageIdentifier id)
            throws LanguageLoadingFailedException {
        synchronized(this.objectLock) {
            this.logger.debug("Finding language '{}'.", id);
            @Nullable final FileObject rootLocation = this.languageSource.find(id);
            if (rootLocation == null) {
                this.logger.error("Could not find language with id '{}'.", id);
                return Collections.emptyList();
            }
            this.logger.debug("Found language '{}' at: {}", id, rootLocation);
            this.logger.debug("Requesting discovery of language '{}' at: {}", id, rootLocation);
            final Iterable<ILanguageDiscoveryRequest> requests;
            try {
                requests = this.discoveryService.request(rootLocation);
                if (Iterables.isEmpty(requests)) {
                    this.logger.error("Got no discovery requests for language '{}' at: {}", id, rootLocation);
                    return Collections.emptyList();
                }
            } catch (final MetaborgException e) {
                throw LoggerUtils.exception(this.logger, LanguageLoadingFailedException.class,
                        "Could not load language '{}' at: {}", e, id, rootLocation);
            }
            final Collection<ILanguageComponent> components = loadRange(requests);
            this.logger.debug("Discovered components language '{}': {}", id, components);
            return components;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ILanguageComponent load(final ILanguageDiscoveryRequest request)
            throws LanguageLoadingFailedException
    {
        synchronized(this.objectLock) {
            try {
                this.logger.debug("Loading language component from request: {}", request);
                final ILanguageComponent component = this.discoveryService.discover(request);
                this.logger.info("Loaded language component: {}", component);
                return component;
            } catch (final MetaborgException e) {
                throw LoggerUtils.exception(this.logger, LanguageLoadingFailedException.class,
                        "Loading language failed from request: {}", e, request);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ILanguageComponent> discoverRange(final Iterable<LanguageIdentifier> ids)
            throws LanguageLoadingFailedException {
        final List<ILanguageComponent> allComponents = new ArrayList<>();
        for (final LanguageIdentifier id : ids) {
            final Collection<ILanguageComponent> components = discover(id);
            allComponents.addAll(components);
        }
        return allComponents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ILanguageComponent> loadRange(final Iterable<ILanguageDiscoveryRequest> requests)
            throws LanguageLoadingFailedException {
        final List<ILanguageComponent> components = new ArrayList<>();
        for (final ILanguageDiscoveryRequest request : requests) {
            final ILanguageComponent component = load(request);
            components.add(component);
        }
        return components;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unload(final ILanguageComponent component) {
        synchronized(this.objectLock) {
            this.logger.debug("Unloading language component: {}", component);
            this.languageService.remove(component);
            this.logger.info("Unloaded language component: {}", component);
        }
    }

}
