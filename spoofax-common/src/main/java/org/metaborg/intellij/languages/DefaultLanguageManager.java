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

package org.metaborg.intellij.languages;


import com.google.inject.Singleton;
import jakarta.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.discovery.ILanguageSource;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.utils.ExceptionUtils;
import org.metaborg.util.iterators.Iterables2;
import org.metaborg.util.log.*;

import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public Collection<? extends ILanguage> getLoadedLanguages() {
        return Iterables2.toArrayList(this.languageService.getAllLanguages());
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
                if (Iterables2.isEmpty(requests)) {
                    this.logger.error("Got no discovery requests for language '{}' at: {}", id, rootLocation);
                    return Collections.emptyList();
                }
            } catch (final MetaborgException e) {
                throw new LanguageLoadingFailedException("Could not load language '{}' at: {}", e, id, rootLocation);
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
                throw ExceptionUtils.exception(LanguageLoadingFailedException.class,
                        "Loading language failed from request: {}", e, request);
            }
        }
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
            try {
                final ILanguageComponent component = load(request);
                components.add(component);
            } catch (final LanguageLoadingFailedException e) {
                this.logger.error("Failed to load language from request: {}", e, request);
            }
        }

        return components;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unloadComponentRange(final Iterable<ILanguageComponent> components) {
        // safety copy: unload may modify iterable
        for (final ILanguageComponent component : Iterables2.toArrayList(components)) {
            unload(component);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unloadImplRange(final Iterable<? extends ILanguageImpl> impls) {
        // safety copy: unload may modify iterable
        for (final ILanguageImpl impl : Iterables2.toArrayList(impls)) {
            unloadComponentRange(impl.components());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unloadRange(final Iterable<? extends ILanguage> languages) {
        // safety copy: unload may modify iterable
        for (final ILanguage language : Iterables2.toArrayList(languages)) {
            unloadImplRange(language.impls());
        }
    }

}
