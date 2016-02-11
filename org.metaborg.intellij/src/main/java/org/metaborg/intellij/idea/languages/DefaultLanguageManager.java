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

package org.metaborg.intellij.idea.languages;

import com.google.common.cache.*;
import com.google.common.collect.*;
import com.google.inject.*;
import org.apache.commons.lang.*;
import org.metaborg.core.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Default implementation of the {@link ILanguageManager} interface.
 *
 * This implementation is thread-safe.
 */
public final class DefaultLanguageManager implements ILanguageManager {

    // TODO: The ILanguageService and ILanguageDiscoveryService implementations should be thread-safe too.

    private final Object objectLock = new Object();
    private final ILanguageDiscoveryService discoveryService;
    private final ILanguageService languageService;
    private final ConcurrentMap<ILanguage, ActivatedLanguage> loadedLanguages = new ConcurrentHashMap<>();
    private final ConcurrentMap<ILanguageImpl, ActivatedLanguageImpl> loadedLanguageImpls = new ConcurrentHashMap<>();
    private final Cache<ILanguage, ActivatedLanguage> ideaLanguageCache = CacheBuilder.newBuilder().weakKeys().build();
    private final Cache<ILanguageImpl, ActivatedLanguageImpl> ideaLanguageImplCache = CacheBuilder.newBuilder().weakKeys().build();

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link DefaultLanguageManager} class.
     */
    @Inject
    public DefaultLanguageManager(final ILanguageService languageService,
                                  final ILanguageDiscoveryService discoveryService) {
        this.languageService = languageService;
        this.discoveryService = discoveryService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ILanguage> getAllLanguages() {
        return Lists.newArrayList(this.languageService.getAllLanguages());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ILanguage> getActiveLanguages() {
        return Collections.unmodifiableSet(this.loadedLanguages.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive(final ILanguage language) {
        return this.loadedLanguages.containsKey(language);
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean canActivate(final ILanguage language) {
//        return LanguageUtils2.isRealLanguage(language);
//    }

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
    public void unload(final ILanguageComponent component) {
        synchronized(this.objectLock) {
            assertComponentDoesNotContributeToActiveLanguages(component);

            this.logger.debug("Unloading language component: {}", component);
            this.languageService.remove(component);
            this.logger.info("Unloaded language component: {}", component);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate(final ILanguage language) {
        // Check to prevent locking if there's nothing to do.
        if (!canActivate(language))
            return;

        synchronized(this.objectLock) {
            // The language may have been activated (or unloaded) before we acquired the lock.
            // So we check again, this time within the lock.
            if (!canActivate(language))
                return;

            this.logger.debug("Activating language: {}", language);
            final ActivatedLanguage activatedLanguage = getOrCreateIdeaLanguage(language);
            activateLanguage(activatedLanguage);
            for (final ILanguageImpl implementation : language.impls()) {
                final ActivatedLanguageImpl activatedLanguageImpl = getOrCreateIdeaLanguageImpl(implementation);
                activateLanguageImpl(activatedLanguageImpl);
                this.loadedLanguageImpls.put(implementation, activatedLanguageImpl);
            }

            this.loadedLanguages.put(language, activatedLanguage);
            this.logger.info("Activated language: {}", language);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate(final ILanguage language) {
        // Check to prevent locking if there's nothing to do.
        if (!canDeactivate(language))
            return;

        synchronized(this.objectLock) {
            // The language may have been activated before we acquired the lock.
            // So we check again, this time within the lock.
            if (!canDeactivate(language))
                return;

            this.logger.debug("Deactivating language: {}", language);
            final ActivatedLanguage activatedLanguage = this.loadedLanguages.remove(language);
            for (final ILanguageImpl implementation : language.impls()) {
                final ActivatedLanguageImpl activatedLanguageImpl = this.loadedLanguageImpls.remove(implementation);
                deactivateLanguageImpl(activatedLanguageImpl);
            }
            deactivateLanguage(activatedLanguage);
            this.logger.info("Deactivated language: {}", language);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ILanguage getLanguage(final MetaborgIdeaLanguage ideaLanguage) {
        @Nullable ILanguage language = this.languageService.getLanguage(ideaLanguage.getID());
        if (language == null) {
            throw LoggerUtils.exception(this.logger, IllegalArgumentException.class,
                    "There is no language associated with the specified MetaborgIdeaLanguage: {}", ideaLanguage);
        }
        return language;
    }

    /**
     * Checks whether the given language is currently loaded.
     *
     * @param language The language to check.
     * @return <code>true</code> if the language is loaded;
     * otherwise, <code>false</code>.
     */
    private boolean isLoadedLanguage(ILanguage language) {
        // HACK: Reference equality check,
        // until the ILanguageService provides an isLoaded(ILanguage) method.
        return this.languageService.getLanguage(language.name()) == language;
    }

    /**
     * Asserts that the specified component does not contribute to any currently
     * activated languages.
     *
     * @param component The component to test.
     */
    private void assertComponentDoesNotContributeToActiveLanguages(final ILanguageComponent component) {
        for (final ILanguageImpl languageImpl : component.contributesTo()) {
            if (isActive(languageImpl.belongsTo())) {
                throw LoggerUtils.exception(this.logger, UnsupportedOperationException.class,
                        "The component contributes to at least one currently active language: {}", component);
            }
        }
    }

    /**
     * Determines whether the specified language can be activated.
     *
     * @param language The language to test.
     * @return <code>true</code> when the language can be activated;
     * otherwise, <code>false</code>.
     */
    private boolean canActivate(final ILanguage language) {
        if (isActive(language)) {
            this.logger.warn("Cannot activate language. Language already active: {}", language);
            return false;
        }
        if (!LanguageUtils2.isRealLanguage(language)) {
            this.logger.warn("Cannot activate language. Language is not a real language: {}", language);
            return false;
        }
        if (!isLoadedLanguage(language)) {
            this.logger.warn("Cannot activate language. Language is not currently loaded: {}", language);
            return false;
        }
        return true;
    }

    /**
     * Determines whether the specified language can be deactivated.
     *
     * @param language The language to test.
     * @return <code>true</code> when the language can be deactivated;
     * otherwise, <code>false</code>.
     */
    private boolean canDeactivate(final ILanguage language) {
        if (!isActive(language)) {
            this.logger.warn("Cannot deactivate language. Language not active: {}", language);
            return false;
        }
        return true;
    }

    /**
     * Gets the IDEA language object associated with the specified language.
     *
     * If no such object exists, it is created.
     *
     * @param language The language to look up.
     * @return The associated IDEA language.
     */
    private ActivatedLanguage getOrCreateIdeaLanguage(final ILanguage language) {
        final ActivatedLanguage activatedLanguage;
        try {
            activatedLanguage = this.ideaLanguageCache.get(language, () -> createIdeaLanguage(language));
        } catch (final ExecutionException ex) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
                    "An unhandled checked exception was thrown from createIdeaLanguage().", ex);
        }
        return activatedLanguage;
    }

    /**
     * Gets the IDEA language implementation object associated with the specified language implementation.
     *
     * If no such object exists, it is created.
     *
     * @param languageImpl The language implementation to look up.
     * @return The associated IDEA language implementation.
     */
    private ActivatedLanguageImpl getOrCreateIdeaLanguageImpl(final ILanguageImpl languageImpl) {
        final ActivatedLanguageImpl activatedLanguageImpl;
        try {
            activatedLanguageImpl = this.ideaLanguageImplCache.get(languageImpl, () -> createIdeaLanguageImpl(languageImpl));
        } catch (final ExecutionException ex) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
                    "An unhandled checked exception was thrown from createIdeaLanguage().", ex);
        }
        return activatedLanguageImpl;
    }

    /**
     * Creates the IDEA language object associated with the specified language.
     *
     * @param language The language.
     * @return The created IDEA language.
     */
    private ActivatedLanguage createIdeaLanguage(final ILanguage language) {
        // TODO: Implement!
        throw new UnsupportedOperationException();
    }

    /**
     * Creates the IDEA language implementation object associated with the specified language implementation.
     *
     * @param languageImpl The language implementation.
     * @return The created IDEA language implementation.
     */
    private ActivatedLanguageImpl createIdeaLanguageImpl(final ILanguageImpl languageImpl) {
        // TODO: Implement!
        throw new UnsupportedOperationException();
    }

    /**
     * Activates the specified language.
     *
     * @param language The language to activate.
     */
    private void activateLanguage(ActivatedLanguage language) {
        // TODO: Register the extension points.
        // TODO: Register the file type.
        throw new NotImplementedException();
    }

    /**
     * Activates the specified language.
     *
     * @param languageImpl The language implementation to activate.
     */
    private void activateLanguageImpl(ActivatedLanguageImpl languageImpl) {
        // TODO: Register the builder menu action group.
        throw new NotImplementedException();
    }

    /**
     * Deactivates the specified language.
     *
     * @param language The language to deactivate.
     */
    private void deactivateLanguage(ActivatedLanguage language) {
        // TODO: Unregister the file type.
        // TODO: Unregister the extension points.
        throw new NotImplementedException();
    }

    /**
     * Deactivates the specified language.
     *
     * @param languageImpl The language implementation to deactivate.
     */
    private void deactivateLanguageImpl(ActivatedLanguageImpl languageImpl) {
        // TODO: Unregister the builder menu action group.
        throw new NotImplementedException();
    }

}
