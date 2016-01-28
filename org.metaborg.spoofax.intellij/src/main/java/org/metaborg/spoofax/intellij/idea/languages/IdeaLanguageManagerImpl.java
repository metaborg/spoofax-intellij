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

package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.idea.InstanceLanguageExtensionPoint;
import org.metaborg.spoofax.intellij.idea.InstanceSyntaxHighlighterFactoryExtensionPoint;
import org.metaborg.spoofax.intellij.idea.vfs.SpoofaxFileType;
import org.metaborg.spoofax.intellij.languages.LanguageUtils;
import org.metaborg.spoofax.intellij.menu.AnActionWithId;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@inheritDoc}
 */
@Singleton
public final class IdeaLanguageManagerImpl implements IIdeaLanguageManager {

    @NotNull
    private final static String PARSER_DEFINITION_EXTENSION = "com.intellij.lang.parserDefinition";
    @NotNull
    private final static String SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION = "com.intellij.lang.syntaxHighlighterFactory";
    @NotNull
    private final static String EXTERNAL_ANNOTATOR_EXTENSION = "com.intellij.externalAnnotator";
    @NotNull
    private final IIdeaAttachmentManager objectManager;
    @NotNull
    private final Map<ILanguage, RegisteredIdeaLanguageObject> loadedLanguages = new HashMap<>();
    @InjectLogger
    private Logger logger;

    @Inject
    private IdeaLanguageManagerImpl(@NotNull final IIdeaAttachmentManager objectManager) {
        this.objectManager = objectManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(@NotNull final ILanguage language) {
        if (isLoaded(language))
            throw new IllegalArgumentException("Language '" + language + "' is already loaded.");
        if (!canLoad(language))
            throw new IllegalArgumentException("Language '" + language + "' is not loadable.");

        RegisteredIdeaLanguageObject obj = new RegisteredIdeaLanguageObject(this.objectManager.get(language));

        installLanguage(obj);
        for (ILanguageImpl implementation : language.impls()) {
            installLanguageImplementation(this.objectManager.get(implementation));
        }

        loadedLanguages.put(language, obj);

        logger.info("Loaded language {}", language);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unload(@NotNull final ILanguage language) {
        if (!isLoaded(language))
            return false;
        RegisteredIdeaLanguageObject obj = this.loadedLanguages.remove(language);

        for (ILanguageImpl implementation : language.impls()) {
            uninstallLanguageImplementation(this.objectManager.get(implementation));
        }
        uninstallLanguage(obj);

        logger.info("Unloaded language {}", language);

        return obj != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLoaded(@NotNull final ILanguage language) {
        return this.loadedLanguages.containsKey(language);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canLoad(@NotNull ILanguage language) {
        return LanguageUtils.isRealLanguage(language);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Set<ILanguage> getLoaded() {
        return Collections.unmodifiableSet(this.loadedLanguages.keySet());
    }

    /**
     * Installs a language into IntelliJ.
     *
     * @param obj The language to install.
     */
    private void installLanguage(@NotNull final RegisteredIdeaLanguageObject obj) {
        obj.parserDefinitionExtension = new InstanceLanguageExtensionPoint<>(
                obj.languageObject.ideaLanguage,
                obj.languageObject.parserDefinition);
        obj.externalAnnotatorExtension = new InstanceLanguageExtensionPoint<>(
                obj.languageObject.ideaLanguage,
                obj.languageObject.spoofaxAnnotator);
        obj.syntaxHighlighterFactoryExtension = new InstanceSyntaxHighlighterFactoryExtensionPoint(
                obj.languageObject.ideaLanguage,
                obj.languageObject.syntaxHighlighterFactory);

        registerExtension(PARSER_DEFINITION_EXTENSION, obj.parserDefinitionExtension);
        registerExtension(EXTERNAL_ANNOTATOR_EXTENSION, obj.externalAnnotatorExtension);
        registerExtension(SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION, obj.syntaxHighlighterFactoryExtension);
        registerFileType(obj.languageObject.fileType);
    }

    /**
     * Installs a language implementation into IntelliJ.
     *
     * @param obj The language implementation's attachment.
     */
    private void installLanguageImplementation(@NotNull final IdeaLanguageImplAttachment obj) {
        addAndRegisterActionGroup(obj.buildActionGroup, IdeActions.GROUP_MAIN_MENU);
    }

    /**
     * Registers an extension.
     *
     * @param extensionPointName The extension point name.
     * @param value              The extension to register.
     */
    private void registerExtension(@NotNull final String extensionPointName, @NotNull final Object value) {
        ExtensionPoint<Object> extensionPoint = Extensions.getRootArea().getExtensionPoint(extensionPointName);
        extensionPoint.registerExtension(value);
    }

    /**
     * Registers a file type.
     *
     * @param fileType The file type to register.
     */
    private void registerFileType(@NotNull final SpoofaxFileType fileType) {
        FileTypeManagerEx.getInstanceEx().registerFileType(fileType);
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        for (String ext : fileType.getExtensions()) {
            FileNameMatcher matcher = new ExtensionFileNameMatcher(ext);
            fileTypeManager.associate(fileType, matcher);
        }
    }

    /**
     * Adds an action(group) to a parent and registers all its children.
     *
     * @param action   The action to add.
     * @param parentID The parent ID.
     */
    private void addAndRegisterActionGroup(@NotNull final AnAction action, @NotNull String parentID) {
        ActionManager manager = ActionManager.getInstance();
        DefaultActionGroup parent = (DefaultActionGroup) manager.getAction(parentID);
        parent.add(action);
        registerActions(manager, action);
    }

    /**
     * Registers the action and its children.
     *
     * @param action The action.
     */
    private void registerActions(@NotNull final ActionManager manager, @NotNull final AnAction action) {
        if (action instanceof AnActionWithId) {
            manager.registerAction(((AnActionWithId) action).id(), action);
        }
        if (action instanceof DefaultActionGroup) {
            registerActions(manager, (DefaultActionGroup) action);
        }
    }

    /**
     * Registers all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private void registerActions(@NotNull final ActionManager manager, @NotNull final DefaultActionGroup actionGroup) {
        for (AnAction action : actionGroup.getChildActionsOrStubs()) {
            registerActions(manager, action);
        }
    }


    /**
     * Removes a language from IntelliJ.
     *
     * @param obj The language to remove.
     */
    private void uninstallLanguage(@NotNull final RegisteredIdeaLanguageObject obj) {
        unregisterFileType(obj.languageObject.fileType);
        unregisterExtension(SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION, obj.syntaxHighlighterFactoryExtension);
        unregisterExtension(EXTERNAL_ANNOTATOR_EXTENSION, obj.externalAnnotatorExtension);
        unregisterExtension(PARSER_DEFINITION_EXTENSION, obj.parserDefinitionExtension);
    }

    /**
     * Uninstalls a language implementation from IntelliJ.
     *
     * @param obj The language implementation's attachment.
     */
    private void uninstallLanguageImplementation(@NotNull final IdeaLanguageImplAttachment obj) {
        removeAndUnregisterActionGroup(obj.buildActionGroup, IdeActions.GROUP_MAIN_MENU);
    }

    /**
     * Unregisters a file type.
     *
     * @param fileType The file type to unregister.
     */
    private void unregisterFileType(@NotNull final SpoofaxFileType fileType) {
        FileTypeManagerEx.getInstanceEx().unregisterFileType(fileType);
    }

    /**
     * Unregisters an extension.
     *
     * @param extensionPointName The extension point name.
     * @param value              The extension to unregister.
     */
    private void unregisterExtension(@NotNull final String extensionPointName, @NotNull final Object value) {
        Extensions.getRootArea().getExtensionPoint(extensionPointName).unregisterExtension(value);
    }

    /**
     * Removes an action(group) from a parent and unregisters all its children.
     *
     * @param action   The action to remove.
     * @param parentID The parent ID.
     */
    private void removeAndUnregisterActionGroup(@NotNull final AnAction action, @NotNull String parentID) {
        ActionManager manager = ActionManager.getInstance();
        DefaultActionGroup parent = (DefaultActionGroup) manager.getAction(parentID);
        parent.remove(action);
        unregisterActions(manager, action);
    }

    /**
     * Unregisters the action and its children.
     *
     * @param action The action.
     */
    private void unregisterActions(@NotNull final ActionManager manager, @NotNull final AnAction action) {
        if (action instanceof AnActionWithId) {
            manager.unregisterAction(((AnActionWithId) action).id());
        }
        if (action instanceof DefaultActionGroup) {
            unregisterActions(manager, (DefaultActionGroup) action);
        }
    }

    /**
     * Unregisters all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private void unregisterActions(
            @NotNull final ActionManager manager,
            @NotNull final DefaultActionGroup actionGroup) {
        for (AnAction action : actionGroup.getChildActionsOrStubs()) {
            unregisterActions(manager, action);
        }
    }

    /**
     * A registered IDEA language object.
     */
    private final class RegisteredIdeaLanguageObject {

        @NotNull
        public final IdeaLanguageAttachment languageObject;
        public InstanceLanguageExtensionPoint<?> parserDefinitionExtension;
        public InstanceLanguageExtensionPoint<?> externalAnnotatorExtension;
        public InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension;

        public RegisteredIdeaLanguageObject(@NotNull final IdeaLanguageAttachment languageObject) {
            this.languageObject = languageObject;
        }

    }
}
