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

import java.util.*;
import java.util.concurrent.*;

/**
 * Default implementation of the {@link ILanguageManager} interface.
 */
public final class DefaultLanguageManager implements ILanguageManager {

    private final ILanguageDiscoveryService discoveryService;
    private final ILanguageService languageService;
    private final Map<ILanguage, ActivatedLanguage> loadedLanguages = new HashMap<>();
    private final Cache<ILanguage, ActivatedLanguage> ideaLanguageCache = CacheBuilder.newBuilder().weakKeys().build();

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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canActivate(final ILanguage language) {
        return LanguageUtils2.isRealLanguage(language);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ILanguageComponent load(final ILanguageDiscoveryRequest request)
            throws LanguageLoadingFailedException
    {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void unload(final ILanguageComponent component) {
        assertComponentDoesNotContributeToActiveLanguages(component);

        this.logger.debug("Unloading language component: {}", component);
        this.languageService.remove(component);
        this.logger.info("Unloaded language component: {}", component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate(final ILanguage language) {
        if (isActive(language))
            return;

        this.logger.debug("Activating language: {}", language);
        final ActivatedLanguage activatedLanguage = getOrCreateIdeaLanguage(language);
        // TODO: Activate the language implementations.
        assert false;
        this.loadedLanguages.put(language, activatedLanguage);
        this.logger.info("Activated language: {}", language);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate(final ILanguage language) {
        if (!isActive(language))
            return;

        this.logger.debug("Deactivating language: {}", language);
        final ActivatedLanguage activatedLanguage = this.loadedLanguages.remove(language);
        // TODO: Deactivate the language implementations.
        assert false;
        this.logger.info("Deactivated language: {}", language);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ILanguage getLanguage(final MetaborgIdeaLanguage ideaLanguage) {
        for (ILanguage language : this.languageService.getAllLanguages()) {
            if (Objects.equals(language.name(), ideaLanguage.getID())) {
                return language;
            }
        }
        throw LoggerUtils.exception(this.logger, IllegalArgumentException.class,
                "There is no language associated with the specified MetaborgIdeaLanguage: {}", ideaLanguage);
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
     * Creates the IDEA language object associated with the specified language.
     *
     * @param language The language to look up.
     * @return The created IDEA language.
     */
    private ActivatedLanguage createIdeaLanguage(final ILanguage language) {
        // TODO: Implement!
        throw new UnsupportedOperationException();
    }

}
