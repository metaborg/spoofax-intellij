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
import com.google.inject.*;
import com.intellij.lang.*;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.psi.tree.*;
import javassist.util.proxy.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.actions.*;
import org.metaborg.intellij.discovery.*;
import org.metaborg.intellij.idea.extensions.*;
import org.metaborg.intellij.idea.filetypes.*;
import org.metaborg.intellij.idea.parsing.*;
import org.metaborg.intellij.idea.parsing.annotations.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.languages.*;
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
public final class DefaultIdeaLanguageManager extends DefaultLanguageManager
        implements IIdeaLanguageManager, ILanguageBindingManager {

    // FIXME: The ILanguageService and ILanguageDiscoveryService implementations should be thread-safe too.

    private final Object objectLock = new Object();
    private final ProxyFactory proxyFactory;
    private final MetaborgSourceAnnotator<?, ?> metaborgSourceAnnotator;
    private final IFileElementTypeFactory fileElementTypeFactory;
    private final IParserDefinitionFactory parserDefinitionFactory;
    private final Provider<SpoofaxSyntaxHighlighterFactory> syntaxHighlighterFactoryProvider;
    private final BuilderMenuBuilder builderMenuBuilder;
    private final ActionUtils actionUtils;
    private final ConcurrentMap<ILanguage, LanguageBindings> loadedLanguages = new ConcurrentHashMap<>();
    private final ConcurrentMap<ILanguageImpl, LanguageImplBindings> loadedLanguageImpls = new ConcurrentHashMap<>();
    private final Cache<ILanguage, LanguageBindings> ideaLanguageCache = CacheBuilder.newBuilder().weakKeys().build();
    private final Cache<ILanguageImpl, LanguageImplBindings> ideaLanguageImplCache = CacheBuilder.newBuilder().weakKeys().build();

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link DefaultIdeaLanguageManager} class.
     */
    @Inject
    public DefaultIdeaLanguageManager(final ILanguageService languageService,
                                      final ILanguageSource languageSource,
                                      final ILanguageDiscoveryService discoveryService,
                                      final MetaborgSourceAnnotator<?, ?> metaborgSourceAnnotator,
                                      final IFileElementTypeFactory fileElementTypeFactory,
                                      final IParserDefinitionFactory parserDefinitionFactory,
                                      final Provider<SpoofaxSyntaxHighlighterFactory> syntaxHighlighterFactoryProvider,
                                      final BuilderMenuBuilder builderMenuBuilder,
                                      final ActionUtils actionUtils) {
        super(languageService, languageSource, discoveryService);
        this.metaborgSourceAnnotator = metaborgSourceAnnotator;
        this.fileElementTypeFactory = fileElementTypeFactory;
        this.parserDefinitionFactory = parserDefinitionFactory;
        this.syntaxHighlighterFactoryProvider = syntaxHighlighterFactoryProvider;
        this.builderMenuBuilder = builderMenuBuilder;
        this.actionUtils = actionUtils;

        this.proxyFactory = new ProxyFactory();
        this.proxyFactory.setUseCache(false);
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
    public void unload(final ILanguageComponent component) {
        synchronized(this.objectLock) {
            assertComponentDoesNotContributeToActiveLanguages(component);

            super.unload(component);
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
    public void activateRange(final Iterable<ILanguage> languages) {
        synchronized(this.objectLock) {
            for (final ILanguage language : languages) {
                activate(language);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivateRange(final Iterable<ILanguage> languages) {
        synchronized(this.objectLock) {
            for (final ILanguage language : languages) {
                deactivate(language);
            }
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
        final InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension =
                new InstanceSyntaxHighlighterFactoryExtensionPoint(
                ideaLanguage,
                syntaxHighlighterFactory
        );
        final InstanceLanguageExtensionPoint<?> externalAnnotatorExtension = new InstanceLanguageExtensionPoint<>(
                ExtensionIds.ExternalAnnotator,
                ideaLanguage,
                this.metaborgSourceAnnotator
        );

        return new LanguageBindings(
                tokenTypeManager,
                fileType,
                parserDefinitionExtension,
                syntaxHighlighterFactoryExtension,
                externalAnnotatorExtension
        );
    }

    /**
     * Creates the IDEA language implementation object associated with the specified language implementation.
     *
     * @param languageImpl The language implementation.
     * @return The created IDEA language implementation.
     */
    private LanguageImplBindings createLanguageImplBindings(final ILanguageImpl languageImpl) {
        final DefaultActionGroup buildActionGroup = createBuildActionGroup(languageImpl);

        return new LanguageImplBindings(
                buildActionGroup
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
        ExtensionUtils.register(languageBindings.getExternalAnnotatorExtension());
        FileTypeUtils.register(languageBindings.getFileType());
    }

    /**
     * Activates the specified language.
     *
     * @param languageImplBindings The bindings of the language implementation to activate.
     */
    private void activateLanguageImpl(final LanguageImplBindings languageImplBindings) {
        this.actionUtils.addAndRegisterActionGroup(languageImplBindings.getBuildActionGroup(),
                IdeActions.GROUP_MAIN_MENU, "ToolsMenu", Anchor.AFTER);
    }

    /**
     * Deactivates the specified language.
     *
     * @param languageBindings The bindings of the language to deactivate.
     */
    private void deactivateLanguage(final LanguageBindings languageBindings) {
        FileTypeUtils.unregister(languageBindings.getFileType());
        ExtensionUtils.unregister(languageBindings.getExternalAnnotatorExtension());
        ExtensionUtils.unregister(languageBindings.getSyntaxHighlighterFactoryExtension());
        ExtensionUtils.unregister(languageBindings.getParserDefinitionExtension());
    }

    /**
     * Deactivates the specified language.
     *
     * @param languageImplBindings The bindings of the language implementation to deactivate.
     */
    private void deactivateLanguageImpl(final LanguageImplBindings languageImplBindings) {
        this.actionUtils.removeAndUnregisterActionGroup(languageImplBindings.getBuildActionGroup(),
                IdeActions.GROUP_MAIN_MENU);
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
     * Creates the builder menu action group.
     *
     * @param languageImpl The language implementation.
     * @return The action group.
     */
    private DefaultActionGroup createBuildActionGroup(final ILanguageImpl languageImpl) {
        return this.builderMenuBuilder.build(languageImpl);
    }

    /**
     * Instantiates an abstract class with no abstract members.
     *
     * It is preferred that the object injects itself in the constructor.
     *
     * @param clazz The type of class to instantiate.
     * @param paramTypes The parameter types.
     * @param args The arguments.
     * @param <T> The type of object.
     * @return The instantiated object.
     */
    private <T> T instantiate(final Class<T> clazz, final Class<?>[] paramTypes, final Object... args) {
        return instantiate(clazz, false, paramTypes, args);
    }

    /**
     * Instantiates an abstract class with no abstract members.
     *
     * It is preferred that the object injects itself in the constructor.
     *
     * @param clazz The type of class to instantiate.
     * @param inject Whether to inject the object.
     * @param paramTypes The parameter types.
     * @param args The arguments.
     * @param <T> The type of object.
     * @return The instantiated object.
     */
    private <T> T instantiate(final Class<T> clazz, final boolean inject, final Class<?>[] paramTypes,
                              final Object... args) {
        assert paramTypes.length == args.length;
        try {
            this.proxyFactory.setSuperclass(clazz);
            final T obj = (T)this.proxyFactory.create(
                    paramTypes,
                    args
            );
            if (inject) {
                SpoofaxIdeaPlugin.injector().injectMembers(obj);
            }
            return obj;
        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class, "Unexpected unhandled exception.", e);
        }
    }
}
