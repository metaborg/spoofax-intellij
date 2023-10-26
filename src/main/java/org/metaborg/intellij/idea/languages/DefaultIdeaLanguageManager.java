/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.metaborg.intellij.idea.languages;


import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.actionSystem.Anchor;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IFileElementType;
import javassist.util.proxy.ProxyFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.*;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.discovery.ILanguageSource;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.actions.ActionUtils;
import org.metaborg.intellij.idea.actions.BuilderMenuBuilder;
import org.metaborg.intellij.idea.extensions.ExtensionIds;
import org.metaborg.intellij.idea.extensions.ExtensionUtils;
import org.metaborg.intellij.idea.extensions.InstanceLanguageExtensionPoint;
import org.metaborg.intellij.idea.extensions.InstanceSyntaxHighlighterFactoryExtensionPoint;
import org.metaborg.intellij.idea.filetypes.FileTypeUtils;
import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType;
import org.metaborg.intellij.idea.parsing.IParserDefinitionFactory;
import org.metaborg.intellij.idea.parsing.SpoofaxSyntaxHighlighterFactory;
import org.metaborg.intellij.idea.parsing.annotations.MetaborgSourceAnnotator;
import org.metaborg.intellij.idea.parsing.elements.IFileElementTypeFactory;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenTypeManager;
import org.metaborg.intellij.idea.projects.IdeaLanguageSpec;
import org.metaborg.intellij.languages.DefaultLanguageManager;
import org.metaborg.intellij.languages.ILanguageManager;
import org.metaborg.intellij.languages.LanguageLoadingFailedException;
import org.metaborg.intellij.languages.LanguageUtils2;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.ILogger;

import jakarta.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

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
    private final MetaborgSourceAnnotator metaborgSourceAnnotator;
    private final IFileElementTypeFactory fileElementTypeFactory;
    private final IParserDefinitionFactory parserDefinitionFactory;
    private final Provider<SpoofaxSyntaxHighlighterFactory> syntaxHighlighterFactoryProvider;
    private final BuilderMenuBuilder builderMenuBuilder;
    private final IIntelliJResourceService resourceService;
    private final ActionUtils actionUtils;
    private final ConcurrentMap<ILanguage, LanguageBindings> loadedLanguages = new ConcurrentHashMap<>();
    private final ConcurrentMap<ILanguageImpl, LanguageImplBindings> loadedLanguageImpls = new ConcurrentHashMap<>();
    private final WeakHashMap<ILanguage, LanguageBindings> ideaLanguageCache = new WeakHashMap<>();
    private final WeakHashMap<ILanguageImpl, LanguageImplBindings> ideaLanguageImplCache = new WeakHashMap<>();

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link DefaultIdeaLanguageManager} class.
     */
    @jakarta.inject.Inject @javax.inject.Inject
    public DefaultIdeaLanguageManager(final ILanguageService languageService,
                                      final ILanguageSource languageSource, final ILanguageDiscoveryService discoveryService,
                                      final IIntelliJResourceService resourceService, final MetaborgSourceAnnotator metaborgSourceAnnotator,
                                      final IFileElementTypeFactory fileElementTypeFactory, final IParserDefinitionFactory parserDefinitionFactory,
                                      final Provider<SpoofaxSyntaxHighlighterFactory> syntaxHighlighterFactoryProvider,
                                      final BuilderMenuBuilder builderMenuBuilder, final ActionUtils actionUtils) {
        super(languageService, languageSource, discoveryService);
        this.metaborgSourceAnnotator = metaborgSourceAnnotator;
        this.fileElementTypeFactory = fileElementTypeFactory;
        this.parserDefinitionFactory = parserDefinitionFactory;
        this.syntaxHighlighterFactoryProvider = syntaxHighlighterFactoryProvider;
        this.builderMenuBuilder = builderMenuBuilder;
        this.actionUtils = actionUtils;
        this.resourceService = resourceService;

        this.proxyFactory = new ProxyFactory();
        this.proxyFactory.setUseCache(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override public Collection<ILanguage> getActiveLanguages() {
        return Collections.unmodifiableSet(this.loadedLanguages.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override public boolean isActive(final ILanguage language) {
        return this.loadedLanguages.containsKey(language);
    }

    /**
     * {@inheritDoc}
     */
    @Override public void unload(final ILanguageComponent component) {
        synchronized(this.objectLock) {
            assertComponentDoesNotContributeToActiveLanguages(component);

            super.unload(component);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override public void activate(final ILanguage language) {
        // Check to prevent locking if there's nothing to do.
        if(!canActivate(language))
            return;

        synchronized(this.objectLock) {
            // The language may have been activated (or unloaded) before we acquired the lock.
            // So we check again, this time within the lock.
            if(!canActivate(language))
                return;

            this.logger.debug("Activating language: {}", language);

            // Ensure all bindings are created.
            final LanguageBindings activatedLanguage = getOrCreateIdeaLanguage(language);
            this.loadedLanguages.put(language, activatedLanguage);
            for(final ILanguageImpl implementation : language.impls()) {
                final LanguageImplBindings activatedLanguageImpl = getOrCreateIdeaLanguageImpl(implementation);
                this.loadedLanguageImpls.put(implementation, activatedLanguageImpl);
            }

            // Activate all languages.
            activateLanguage(getBindings(language));
            for(final ILanguageImpl implementation : language.impls()) {
                activateLanguageImpl(getBindings(implementation));
            }

            this.logger.info("Activated language: {}", language);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override public void deactivate(final ILanguage language) {
        // Check to prevent locking if there's nothing to do.
        if(!canDeactivate(language))
            return;

        synchronized(this.objectLock) {
            // The language may have been activated before we acquired the lock.
            // So we check again, this time within the lock.
            if(!canDeactivate(language))
                return;

            this.logger.debug("Deactivating language: {}", language);

            // Deactivate language and remove bindings.
            final LanguageBindings activatedLanguage = this.loadedLanguages.remove(language);
            for(final ILanguageImpl implementation : language.impls()) {
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
    @Override public void activateRange(final Iterable<ILanguage> languages) {
        synchronized(this.objectLock) {
            for(final ILanguage language : languages) {
                activate(language);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override public void deactivateRange(final Iterable<ILanguage> languages) {
        synchronized(this.objectLock) {
            for(final ILanguage language : languages) {
                deactivate(language);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override public ILanguage getLanguage(final MetaborgIdeaLanguage ideaLanguage) {
        @Nullable final ILanguage language = this.languageService.getLanguage(ideaLanguage.getID());
        if(language == null) {
            throw LoggerUtils2.exception(this.logger, IllegalArgumentException.class,
                "There is no language associated with the specified MetaborgIdeaLanguage: {}", ideaLanguage);
        }
        return language;
    }

    /**
     * Checks whether the given language is currently loaded.
     *
     * @param language
     *            The language to check.
     * @return <code>true</code> if the language is loaded; otherwise, <code>false</code>.
     */
    private boolean isLoadedLanguage(final ILanguage language) {
        // HACK: Reference equality check,
        // until the ILanguageService provides an isLoaded(ILanguage) method.
        return this.languageService.getLanguage(language.name()) == language;
    }

    /**
     * Asserts that the specified component does not contribute to any currently activated languages.
     *
     * @param component
     *            The component to test.
     */
    private void assertComponentDoesNotContributeToActiveLanguages(final ILanguageComponent component) {
        for(final ILanguageImpl languageImpl : component.contributesTo()) {
            if(isActive(languageImpl.belongsTo())) {
                throw LoggerUtils2.exception(this.logger, UnsupportedOperationException.class,
                    "The component contributes to at least one currently active language: {}", component);
            }
        }
    }

    /**
     * Determines whether the specified language can be activated.
     *
     * @param language
     *            The language to test.
     * @return <code>true</code> when the language can be activated; otherwise, <code>false</code>.
     */
    private boolean canActivate(final ILanguage language) {
        if(isActive(language)) {
            this.logger.info("Cannot activate language. Language already active: {}", language);
            return false;
        }
        if(!LanguageUtils2.isRealLanguage(language)) {
            this.logger.warn("Cannot activate language. Language is not a real language: {}", language);
            return false;
        }
        if(!isLoadedLanguage(language)) {
            this.logger.warn("Cannot activate language. Language is not currently loaded: {}", language);
            return false;
        }
        return true;
    }

    /**
     * Determines whether the specified language can be deactivated.
     *
     * @param language
     *            The language to test.
     * @return <code>true</code> when the language can be deactivated; otherwise, <code>false</code>.
     */
    private boolean canDeactivate(final ILanguage language) {
        if(!isActive(language)) {
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
     * @param language
     *            The language to look up.
     * @return The associated IDEA language.
     */
    private LanguageBindings getOrCreateIdeaLanguage(final ILanguage language) {
        return this.ideaLanguageCache.computeIfAbsent(language, k -> createLanguageBindings(language));
    }

    /**
     * Gets the IDEA language implementation object associated with the specified language implementation.
     *
     * If no such object exists, it is created.
     *
     * @param languageImpl
     *            The language implementation to look up.
     * @return The associated IDEA language implementation.
     */
    private LanguageImplBindings getOrCreateIdeaLanguageImpl(final ILanguageImpl languageImpl) {
        return this.ideaLanguageImplCache.computeIfAbsent(languageImpl, k -> createLanguageImplBindings(languageImpl));
    }

    /**
     * Creates the IDEA language object associated with the specified language.
     *
     * @param language
     *            The language.
     * @return The created IDEA language.
     */
    private LanguageBindings createLanguageBindings(final ILanguage language) {
        final MetaborgIdeaLanguage ideaLanguage = createIdeaLanguage(language);
        final MetaborgLanguageFileType fileType = createFileType(ideaLanguage);
        final SpoofaxTokenTypeManager tokenTypeManager = createTokenTypeManager(ideaLanguage);
        final IFileElementType fileElementType = createFileElementType(ideaLanguage, tokenTypeManager);
        final ParserDefinition parserDefinition = createParserDefinition(fileType, fileElementType);
        final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory = createSyntaxHighlighterFactory();

        final InstanceLanguageExtensionPoint<?> parserDefinitionExtension =
            new InstanceLanguageExtensionPoint<>(ExtensionIds.ParserDefinition, ideaLanguage, parserDefinition);
        final InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension =
            new InstanceSyntaxHighlighterFactoryExtensionPoint(ideaLanguage, syntaxHighlighterFactory);
        final InstanceLanguageExtensionPoint<?> externalAnnotatorExtension = new InstanceLanguageExtensionPoint<>(
            ExtensionIds.ExternalAnnotator, ideaLanguage, this.metaborgSourceAnnotator);

        return new LanguageBindings(tokenTypeManager, fileType, parserDefinitionExtension,
            syntaxHighlighterFactoryExtension, externalAnnotatorExtension);
    }

    /**
     * Creates the IDEA language implementation object associated with the specified language implementation.
     *
     * @param languageImpl
     *            The language implementation.
     * @return The created IDEA language implementation.
     */
    private LanguageImplBindings createLanguageImplBindings(final ILanguageImpl languageImpl) {
        final DefaultActionGroup buildActionGroup = createBuildActionGroup(languageImpl);

        return new LanguageImplBindings(buildActionGroup);
    }

    /**
     * Activates the specified language.
     *
     * @param languageBindings
     *            The bindings of the language to activate.
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
     * @param languageImplBindings
     *            The bindings of the language implementation to activate.
     */
    private void activateLanguageImpl(final LanguageImplBindings languageImplBindings) {
        this.actionUtils.addAndRegisterActionGroup(languageImplBindings.getBuildActionGroup(),
            IdeActions.GROUP_MAIN_MENU, "ToolsMenu", Anchor.AFTER);
    }

    /**
     * Deactivates the specified language.
     *
     * @param languageBindings
     *            The bindings of the language to deactivate.
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
     * @param languageImplBindings
     *            The bindings of the language implementation to deactivate.
     */
    private void deactivateLanguageImpl(final LanguageImplBindings languageImplBindings) {
        this.actionUtils.removeAndUnregisterActionGroup(languageImplBindings.getBuildActionGroup(),
            IdeActions.GROUP_MAIN_MENU);
    }

    /**
     * {@inheritDoc}
     */
    @Override public SpoofaxTokenTypeManager getTokenTypeManager(final ILanguage language) {
        return getBindings(language).getTokenTypeManager();
    }

    /**
     * Gets the bindings for the specified loaded language.
     *
     * @param language
     *            The language.
     * @return The bindings.
     */
    private LanguageBindings getBindings(final ILanguage language) {
        @Nullable final LanguageBindings bindings = this.loadedLanguages.getOrDefault(language, null);
        if(bindings == null) {
            throw LoggerUtils2.exception(this.logger, IllegalArgumentException.class,
                "The specified language is not loaded: {}", language);
        }
        return bindings;
    }

    /**
     * Gets the bindings for the specified loaded language implementation.
     *
     * @param languageImpl
     *            The language implementation.
     * @return The bindings.
     */
    private LanguageImplBindings getBindings(final ILanguageImpl languageImpl) {
        @Nullable final LanguageImplBindings bindings = this.loadedLanguageImpls.getOrDefault(languageImpl, null);
        if(bindings == null) {
            throw LoggerUtils2.exception(this.logger, IllegalArgumentException.class,
                "The specified language implementation is not loaded: {}", languageImpl);
        }
        return bindings;
    }

    /**
     * Creates a new IDEA language for a Spoofax language.
     *
     * @param language
     *            The language.
     * @return The created IDEA language.
     */
    private MetaborgIdeaLanguage createIdeaLanguage(final ILanguage language) {
        return instantiate(MetaborgIdeaLanguage.class, new Class<?>[] { ILanguage.class }, language);
    }

    /**
     * Creates a new parser definition.
     *
     * @param fileType
     *            The language file type.
     * @return The created parser definition.
     */
    private ParserDefinition createParserDefinition(final MetaborgLanguageFileType fileType,
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
     * @param language
     *            The IDEA language.
     * @return The created token type manager.
     */
    private SpoofaxTokenTypeManager createTokenTypeManager(final MetaborgIdeaLanguage language) {
        return new SpoofaxTokenTypeManager(language);
    }

    /**
     * Creates a new file type for an IDEA language.
     *
     * @param language
     *            The IDEA language.
     * @return The created file type.
     */
    private MetaborgLanguageFileType createFileType(final MetaborgIdeaLanguage language) {
        return instantiate(MetaborgLanguageFileType.class, new Class<?>[] { MetaborgIdeaLanguage.class }, language);
    }

    /**
     * Creates a new file element type.
     *
     * @param language
     *            The language.
     * @param tokenTypesManager
     *            The token types manager.
     * @return The file element type.
     */
    private IFileElementType createFileElementType(final Language language,
                                                   final SpoofaxTokenTypeManager tokenTypesManager) {
        return this.fileElementTypeFactory.create(language, tokenTypesManager);
    }

    /**
     * Creates the builder menu action group.
     *
     * @param languageImpl
     *            The language implementation.
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
     * @param clazz
     *            The type of class to instantiate.
     * @param paramTypes
     *            The parameter types.
     * @param args
     *            The arguments.
     * @param <T>
     *            The type of object.
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
     * @param clazz
     *            The type of class to instantiate.
     * @param inject
     *            Whether to inject the object.
     * @param paramTypes
     *            The parameter types.
     * @param args
     *            The arguments.
     * @param <T>
     *            The type of object.
     * @return The instantiated object.
     */
    private <T> T instantiate(final Class<T> clazz, final boolean inject, final Class<?>[] paramTypes,
        final Object... args) {
        assert paramTypes.length == args.length;
        try {
            this.proxyFactory.setSuperclass(clazz);
            final T obj = (T) this.proxyFactory.create(paramTypes, args);
            if(inject) {
                SpoofaxIdeaPlugin.injector().injectMembers(obj);
            }
            return obj;
        } catch(NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException
            | InvocationTargetException e) {
            throw LoggerUtils2.exception(this.logger, UnhandledException.class, "Unexpected unhandled exception.", e);
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override public Iterable<ILanguageDiscoveryRequest> requestFromArtifact(final VirtualFile artifact)
        throws IOException {

        return requestFromArtifact(this.resourceService.resolve(artifact));
    }

    /**
     * {@inheritDoc}
     */
    @Override public Iterable<ILanguageDiscoveryRequest> requestFromArtifact(final FileObject artifact)
        throws IOException {

        final String zipUri;
        try {
            zipUri = "zip://" + artifact.getURL().getPath();
        } catch(final FileSystemException e) {
            throw LoggerUtils2.exception(this.logger, UnhandledException.class,
                "Unhandled exception while requesting languages from artifact: {}", e, artifact);
        }

        final FileObject file = this.resourceService.resolve(zipUri);
        return requestFromFolder(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override public Iterable<ILanguageDiscoveryRequest> requestFromFolder(final VirtualFile folder)
        throws IOException {

        return requestFromFolder(this.resourceService.resolve(folder));
    }

    /**
     * {@inheritDoc}
     */
    @Override public Iterable<ILanguageDiscoveryRequest> requestFromFolder(final FileObject folder) throws IOException {

        try {
            // Loading happens only after the user clicked OK or Apply in the settings dialog.
            return this.discoveryService.request(folder);
        } catch(final MetaborgException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override public void reloadLanguageSpec(final IdeaLanguageSpec project) {
        unloadLanguageSpec(project);
        loadLanguageSpec(project);
    }

    /**
     * {@inheritDoc}
     */
    @Override public void unloadLanguageSpec(final IdeaLanguageSpec project) {
        final Collection<ILanguageComponent> oldComponents = project.getComponents();

        deactivateRange(LanguageUtils2.getLanguagesOfComponents(oldComponents));
        unloadComponentRange(oldComponents);
        project.setComponents(Collections.emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override public void loadLanguageSpec(final IdeaLanguageSpec project) {
        final Iterable<ILanguageDiscoveryRequest> requests;
        try {
            requests = this.discoveryService.request(project.location());
        } catch(final MetaborgException e) {
            this.logger.error("Language discovery failed after compilation for project: {}", e, project);
            return;
        }

        final Collection<ILanguageComponent> newComponents;
        try {
            newComponents = loadRange(requests);
        } catch(final LanguageLoadingFailedException e) {
            this.logger.error("Language loading failed after compilation for project: {}", e, project);
            return;
        }

        project.setComponents(newComponents);
        activateRange(LanguageUtils2.getLanguagesOfComponents(newComponents));
    }
}
