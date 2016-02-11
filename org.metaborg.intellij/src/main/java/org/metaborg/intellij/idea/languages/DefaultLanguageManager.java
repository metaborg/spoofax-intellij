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
import com.intellij.lang.*;
import com.intellij.lang.Language;
import com.intellij.psi.tree.*;
import javassist.util.proxy.*;
import org.apache.commons.lang.*;
import org.metaborg.core.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.extensions.*;
import org.metaborg.intellij.idea.filetypes.*;
import org.metaborg.intellij.idea.parsing.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Default implementation of the {@link ILanguageManager} interface.
 *
 * This implementation is thread-safe.
 */
@Singleton
public final class DefaultLanguageManager implements ILanguageManager, ILanguageBindingManager {

    // FIXME: The ILanguageService and ILanguageDiscoveryService implementations should be thread-safe too.

    private final Object objectLock = new Object();
    private final ProxyFactory proxyFactory;
    private final ILanguageDiscoveryService discoveryService;
    private final ILanguageService languageService;
    private final IFileElementTypeFactory fileElementTypeFactory;
    private final IParserDefinitionFactory parserDefinitionFactory;
    private final Provider<SpoofaxSyntaxHighlighterFactory> syntaxHighlighterFactoryProvider;
    private final ConcurrentMap<ILanguage, LanguageBindings> loadedLanguages = new ConcurrentHashMap<>();
    private final ConcurrentMap<ILanguageImpl, LanguageImplBindings> loadedLanguageImpls = new ConcurrentHashMap<>();
    private final Cache<ILanguage, LanguageBindings> ideaLanguageCache = CacheBuilder.newBuilder().weakKeys().build();
    private final Cache<ILanguageImpl, LanguageImplBindings> ideaLanguageImplCache = CacheBuilder.newBuilder().weakKeys().build();

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link DefaultLanguageManager} class.
     */
    @Inject
    public DefaultLanguageManager(final ILanguageService languageService,
                                  final ILanguageDiscoveryService discoveryService,
                                  final IFileElementTypeFactory fileElementTypeFactory,
                                  final IParserDefinitionFactory parserDefinitionFactory,
                                  final Provider<SpoofaxSyntaxHighlighterFactory> syntaxHighlighterFactoryProvider) {
        this.languageService = languageService;
        this.discoveryService = discoveryService;
        this.fileElementTypeFactory = fileElementTypeFactory;
        this.parserDefinitionFactory = parserDefinitionFactory;
        this.syntaxHighlighterFactoryProvider = syntaxHighlighterFactoryProvider;

        this.proxyFactory = new ProxyFactory();
        this.proxyFactory.setUseCache(false);
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
            final LanguageBindings activatedLanguage = getOrCreateIdeaLanguage(language);
            activateLanguage(activatedLanguage);
            for (final ILanguageImpl implementation : language.impls()) {
                final LanguageImplBindings activatedLanguageImpl = getOrCreateIdeaLanguageImpl(implementation);
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
            final LanguageBindings activatedLanguage = this.loadedLanguages.remove(language);
            for (final ILanguageImpl implementation : language.impls()) {
                final LanguageImplBindings activatedLanguageImpl = this.loadedLanguageImpls.remove(implementation);
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
        @Nullable final ILanguage language = this.languageService.getLanguage(ideaLanguage.getID());
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
    private boolean isLoadedLanguage(final ILanguage language) {
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
    private LanguageBindings getOrCreateIdeaLanguage(final ILanguage language) {
        final LanguageBindings activatedLanguage;
        try {
            activatedLanguage = this.ideaLanguageCache.get(language, () -> createLanguageBindings(language));
        } catch (final ExecutionException ex) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
                    "An unhandled checked exception was thrown from createLanguageBindings().", ex);
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
    private LanguageImplBindings getOrCreateIdeaLanguageImpl(final ILanguageImpl languageImpl) {
        final LanguageImplBindings activatedLanguageImpl;
        try {
            activatedLanguageImpl = this.ideaLanguageImplCache.get(languageImpl,
                    () -> createLanguageImplBindings(languageImpl));
        } catch (final ExecutionException ex) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
                    "An unhandled checked exception was thrown from createLanguageBindings().", ex);
        }
        return activatedLanguageImpl;
    }

    /**
     * Creates the IDEA language object associated with the specified language.
     *
     * @param language The language.
     * @return The created IDEA language.
     */
    private LanguageBindings createLanguageBindings(final ILanguage language) {
        final MetaborgIdeaLanguage ideaLanguage = createIdeaLanguage(language);
        final MetaborgLanguageFileType fileType = createFileType(ideaLanguage);
        final SpoofaxTokenTypeManager tokenTypeManager = createTokenTypeManager(ideaLanguage);
        final IFileElementType fileElementType = createFileElementType(ideaLanguage, tokenTypeManager);
        final ParserDefinition parserDefinition = createParserDefinition(fileType, fileElementType);
        final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory = createSyntaxHighlighterFactory();

        final InstanceLanguageExtensionPoint<?> parserDefinitionExtension = new InstanceLanguageExtensionPoint<>(
                ExtensionIds.ParserDefinition,
                ideaLanguage,
                parserDefinition
        );
        final InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension = new InstanceSyntaxHighlighterFactoryExtensionPoint(
                ideaLanguage,
                syntaxHighlighterFactory
        );

        return new LanguageBindings(
                tokenTypeManager,
                fileType,
                parserDefinitionExtension,
                syntaxHighlighterFactoryExtension
        );
    }

    /**
     * Creates the IDEA language implementation object associated with the specified language implementation.
     *
     * @param languageImpl The language implementation.
     * @return The created IDEA language implementation.
     */
    private LanguageImplBindings createLanguageImplBindings(final ILanguageImpl languageImpl) {
        return new LanguageImplBindings(
        );
    }

    /**
     * Activates the specified language.
     *
     * @param languageBindings The bindings of the language to activate.
     */
    private void activateLanguage(final LanguageBindings languageBindings) {
        ExtensionUtils.register(languageBindings.getParserDefinitionExtension());
        ExtensionUtils.register(languageBindings.getSyntaxHighlighterFactoryExtension());
        // TODO: Register the annotator extension point.
        FileTypeUtils.register(languageBindings.getFileType());
    }

    /**
     * Activates the specified language.
     *
     * @param languageImplBindings The bindings of the language implementation to activate.
     */
    private void activateLanguageImpl(final LanguageImplBindings languageImplBindings) {
        // TODO: Register the builder menu action group.
    }

    /**
     * Deactivates the specified language.
     *
     * @param languageBindings The bindings of the language to deactivate.
     */
    private void deactivateLanguage(final LanguageBindings languageBindings) {
        FileTypeUtils.unregister(languageBindings.getFileType());
        // TODO: Unregister the annotator extension point.
        ExtensionUtils.unregister(languageBindings.getSyntaxHighlighterFactoryExtension());
        ExtensionUtils.unregister(languageBindings.getParserDefinitionExtension());
    }

    /**
     * Deactivates the specified language.
     *
     * @param languageImplBindings The bindings of the language implementation to deactivate.
     */
    private void deactivateLanguageImpl(final LanguageImplBindings languageImplBindings) {
        // TODO: Unregister the builder menu action group.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpoofaxTokenTypeManager getTokenTypeManager(final ILanguage language) {
        return getBindings(language).getTokenTypeManager();
    }

    /**
     * Gets the bindings for the specified loaded language.
     *
     * @param language The language.
     * @return The bindings.
     */
    private LanguageBindings getBindings(final ILanguage language) {
        @Nullable final LanguageBindings bindings = this.loadedLanguages.getOrDefault(language, null);
        if (bindings == null) {
            throw LoggerUtils.exception(this.logger, IllegalArgumentException.class,
                    "The specified language is not loaded: {}", language);
        }
        return bindings;
    }

    /**
     * Gets the bindings for the specified loaded language implementation.
     *
     * @param languageImpl The language implementation.
     * @return The bindings.
     */
    private LanguageImplBindings getBindings(final ILanguageImpl languageImpl) {
        @Nullable final LanguageImplBindings bindings = this.loadedLanguageImpls.getOrDefault(languageImpl, null);
        if (bindings == null) {
            throw LoggerUtils.exception(this.logger, IllegalArgumentException.class,
                    "The specified language implementation is not loaded: {}", languageImpl);
        }
        return bindings;
    }

    /**
     * Creates a new IDEA language for a Spoofax language.
     *
     * @param language The language.
     * @return The created IDEA language.
     */
    private MetaborgIdeaLanguage createIdeaLanguage(final ILanguage language) {
        return instantiate(MetaborgIdeaLanguage.class,
                new Class<?>[]{ILanguage.class},
                language);
    }

    /**
     * Creates a new parser definition.
     *
     * @param fileType The language file type.
     * @return The created parser definition.
     */
    private ParserDefinition createParserDefinition(
            final MetaborgLanguageFileType fileType,
            final IFileElementType fileElementType) {
        return this.parserDefinitionFactory.create(fileType, fileElementType);
    }

    /**
     * Creates a new syntax highlighter factory.
     *
     * @return The syntax highlighter factory.
     */
    private SpoofaxSyntaxHighlighterFactory createSyntaxHighlighterFactory() {
        return this.syntaxHighlighterFactoryProvider.get();
    }

    /**
     * Creates a new spoofax token type manager for an IDEA language.
     *
     * @param language The IDEA language.
     * @return The created token type manager.
     */
    private SpoofaxTokenTypeManager createTokenTypeManager(final MetaborgIdeaLanguage language) {
        return new SpoofaxTokenTypeManager(language);
    }

    /**
     * Creates a new file type for an IDEA language.
     *
     * @param language The IDEA language.
     * @return The created file type.
     */
    private MetaborgLanguageFileType createFileType(final MetaborgIdeaLanguage language) {
        return instantiate(MetaborgLanguageFileType.class,
                new Class<?>[]{MetaborgIdeaLanguage.class},
                language);
    }

    /**
     * Creates a new file element type.
     *
     * @param language The language.
     * @param tokenTypesManager The token types manager.
     * @return The file element type.
     */
    private IFileElementType createFileElementType(
            final Language language,
            final SpoofaxTokenTypeManager tokenTypesManager) {
        return this.fileElementTypeFactory.create(language, tokenTypesManager);
    }

    /**
     * Instantiates an abstract class with no abstract members.
     *
     * @param clazz The type of class to instantiate.
     * @param paramTypes The parameter types.
     * @param args The arguments.
     * @param <T> The type of object.
     * @return The instantiated object.
     */
    private <T> T instantiate(final Class<T> clazz, final Class<?>[] paramTypes, final Object... args) {
        assert paramTypes.length == args.length;
        try {
            this.proxyFactory.setSuperclass(clazz);
            final T obj = (T)this.proxyFactory.create(
                    paramTypes,
                    args
            );
            SpoofaxIdeaPlugin.injector().injectMembers(obj);
            return obj;
        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class, "Unexpected unhandled exception.", e);
        }
    }
}
