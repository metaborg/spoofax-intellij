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
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.wm.impl.IdeMenuBar;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.idea.InstanceLanguageExtensionPoint;
import org.metaborg.spoofax.intellij.idea.InstanceSyntaxHighlighterFactoryExtensionPoint;
import org.metaborg.spoofax.intellij.idea.vfs.SpoofaxFileType;
import org.metaborg.spoofax.intellij.languages.LanguageUtils;
import org.metaborg.spoofax.intellij.menu.AnActionWithId;
import org.metaborg.util.log.ILogger;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@inheritDoc}
 */
@Singleton
public final class IdeaLanguageManagerImpl implements IIdeaLanguageManager {

    private static final String PARSER_DEFINITION_EXTENSION = "com.intellij.lang.parserDefinition";
    private static final String SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION = "com.intellij.lang.syntaxHighlighterFactory";
    private static final String EXTERNAL_ANNOTATOR_EXTENSION = "com.intellij.externalAnnotator";
    private final IIdeaAttachmentManager objectManager;
    private final Map<ILanguage, RegisteredIdeaLanguageObject> loadedLanguages = new HashMap<>();
    @InjectLogger
    private ILogger logger;

    @Inject
    private IdeaLanguageManagerImpl(final IIdeaAttachmentManager objectManager) {
        this.objectManager = objectManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(final ILanguage language) {
        if (isLoaded(language))
            throw new IllegalArgumentException("Language '" + language + "' is already loaded.");
        if (!canLoad(language))
            throw new IllegalArgumentException("Language '" + language + "' is not loadable.");

        final RegisteredIdeaLanguageObject obj = new RegisteredIdeaLanguageObject(this.objectManager.get(language));

        installLanguage(obj);
        for (final ILanguageImpl implementation : language.impls()) {
            installLanguageImplementation(this.objectManager.get(implementation));
        }

        this.loadedLanguages.put(language, obj);

        this.logger.info("Loaded language {}", language);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unload(final ILanguage language) {
        if (!isLoaded(language))
            return false;
        final RegisteredIdeaLanguageObject obj = this.loadedLanguages.remove(language);

        for (final ILanguageImpl implementation : language.impls()) {
            uninstallLanguageImplementation(this.objectManager.get(implementation));
        }
        uninstallLanguage(obj);

        this.logger.info("Unloaded language {}", language);

        return obj != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLoaded(final ILanguage language) {
        return this.loadedLanguages.containsKey(language);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canLoad(final ILanguage language) {
        return LanguageUtils.isRealLanguage(language);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ILanguage> getLoaded() {
        return Collections.unmodifiableSet(this.loadedLanguages.keySet());
    }

    /**
     * Installs a language into IntelliJ.
     *
     * @param obj The language to install.
     */
    private void installLanguage(final RegisteredIdeaLanguageObject obj) {
        obj.parserDefinitionExtension = new InstanceLanguageExtensionPoint<>(
                obj.languageObject.ideaLanguage(),
                obj.languageObject.parserDefinition()
        );
        obj.externalAnnotatorExtension = new InstanceLanguageExtensionPoint<>(
                obj.languageObject.ideaLanguage(),
                obj.languageObject.spoofaxAnnotator()
        );
        obj.syntaxHighlighterFactoryExtension = new InstanceSyntaxHighlighterFactoryExtensionPoint(
                obj.languageObject.ideaLanguage(),
                obj.languageObject.syntaxHighlighterFactory()
        );

        registerExtension(PARSER_DEFINITION_EXTENSION, obj.parserDefinitionExtension);
        registerExtension(EXTERNAL_ANNOTATOR_EXTENSION, obj.externalAnnotatorExtension);
        registerExtension(SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION, obj.syntaxHighlighterFactoryExtension);
        registerFileType(obj.languageObject.fileType());
    }

    /**
     * Installs a language implementation into IntelliJ.
     *
     * @param obj The language implementation's attachment.
     */
    private void installLanguageImplementation(final IdeaLanguageImplAttachment obj) {
        addAndRegisterActionGroup(obj.buildActionGroup(), IdeActions.GROUP_MAIN_MENU, "ToolsMenu", Anchor.AFTER);
    }

    /**
     * Registers an extension.
     *
     * @param extensionPointName The extension point name.
     * @param value              The extension to register.
     */
    private void registerExtension(final String extensionPointName, final Object value) {
        final ExtensionPoint<Object> extensionPoint = Extensions.getRootArea().getExtensionPoint(extensionPointName);
        extensionPoint.registerExtension(value);
    }

    /**
     * Registers a file type.
     *
     * @param fileType The file type to register.
     */
    private void registerFileType(final SpoofaxFileType fileType) {
        FileTypeManagerEx.getInstanceEx().registerFileType(fileType);
        final FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        for (final String ext : fileType.getExtensions()) {
            final FileNameMatcher matcher = new ExtensionFileNameMatcher(ext);
            fileTypeManager.associate(fileType, matcher);
        }
    }

    /**
     * Adds an action(group) to a parent and registers all its children.
     *
     * @param action   The action to add.
     * @param parentID The parent ID.
     */
    private void addAndRegisterActionGroup(final AnAction action, final String parentID, @Nullable final String relativeToActionId, @Nullable final Anchor anchor) {
        final ActionManager manager = ActionManager.getInstance();
        final DefaultActionGroup parent = (DefaultActionGroup)manager.getAction(parentID);
        parent.add(action, getActionConstraints(relativeToActionId, anchor));
        registerActions(manager, action);
    }

    /**
     * Gets an object that specifies where the action is positioned.
     *
     * @param relativeToActionId The action ID relative to which to position the action;
     *                           or <code>null</code> to position the action at the start or end.
     * @param anchor The anchor indicating where to position the action;
     *               or <code>null</code> to position the action after or at the end.
     * @return The {@link Constraints}.
     */
    private Constraints getActionConstraints(@Nullable final String relativeToActionId, @Nullable final Anchor anchor) {
        if (relativeToActionId != null && anchor != null) {
            return new Constraints(anchor, relativeToActionId);
        } else if (relativeToActionId != null) {
            return new Constraints(Anchor.AFTER, relativeToActionId);
        } else if (anchor == Anchor.BEFORE || anchor == Anchor.FIRST) {
            return Constraints.FIRST;
        } else {
            return Constraints.LAST;
        }
    }

    /**
     * Registers the action and its children.
     *
     * @param action The action.
     */
    private void registerActions(final ActionManager manager, final AnAction action) {
        if (action instanceof AnActionWithId) {
            manager.registerAction(((AnActionWithId)action).id(), action);
        }
        if (action instanceof DefaultActionGroup) {
            registerActions(manager, (DefaultActionGroup)action);
        }
    }

    /**
     * Registers all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private void registerActions(final ActionManager manager, final DefaultActionGroup actionGroup) {
        for (final AnAction action : actionGroup.getChildActionsOrStubs()) {
            registerActions(manager, action);
        }
    }


    /**
     * Removes a language from IntelliJ.
     *
     * @param obj The language to remove.
     */
    private void uninstallLanguage(final RegisteredIdeaLanguageObject obj) {
        unregisterFileType(obj.languageObject.fileType());
        unregisterExtension(SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION, obj.syntaxHighlighterFactoryExtension);
        unregisterExtension(EXTERNAL_ANNOTATOR_EXTENSION, obj.externalAnnotatorExtension);
        unregisterExtension(PARSER_DEFINITION_EXTENSION, obj.parserDefinitionExtension);
    }

    /**
     * Uninstalls a language implementation from IntelliJ.
     *
     * @param obj The language implementation's attachment.
     */
    private void uninstallLanguageImplementation(final IdeaLanguageImplAttachment obj) {
        removeAndUnregisterActionGroup(obj.buildActionGroup(), IdeActions.GROUP_MAIN_MENU);
    }

    /**
     * Unregisters a file type.
     *
     * @param fileType The file type to unregister.
     */
    private void unregisterFileType(final SpoofaxFileType fileType) {
        FileTypeManagerEx.getInstanceEx().unregisterFileType(fileType);
    }

    /**
     * Unregisters an extension.
     *
     * @param extensionPointName The extension point name.
     * @param value              The extension to unregister.
     */
    private void unregisterExtension(final String extensionPointName, final Object value) {
        Extensions.getRootArea().getExtensionPoint(extensionPointName).unregisterExtension(value);
    }

    /**
     * Removes an action(group) from a parent and unregisters all its children.
     *
     * @param action   The action to remove.
     * @param parentID The parent ID.
     */
    private void removeAndUnregisterActionGroup(final AnAction action, final String parentID) {
        final ActionManager manager = ActionManager.getInstance();
        final DefaultActionGroup parent = (DefaultActionGroup)manager.getAction(parentID);
        parent.remove(action);
        unregisterActions(manager, action);
    }

    /**
     * Unregisters the action and its children.
     *
     * @param action The action.
     */
    private void unregisterActions(final ActionManager manager, final AnAction action) {
        if (action instanceof AnActionWithId) {
            manager.unregisterAction(((AnActionWithId)action).id());
        }
        if (action instanceof DefaultActionGroup) {
            unregisterActions(manager, (DefaultActionGroup)action);
        }
    }

    /**
     * Unregisters all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private void unregisterActions(
            final ActionManager manager,
            final DefaultActionGroup actionGroup) {
        for (final AnAction action : actionGroup.getChildActionsOrStubs()) {
            unregisterActions(manager, action);
        }
    }

    /**
     * A registered IDEA language object.
     */
    private final class RegisteredIdeaLanguageObject {

        private final IdeaLanguageAttachment languageObject;
        private InstanceLanguageExtensionPoint<?> parserDefinitionExtension;
        private InstanceLanguageExtensionPoint<?> externalAnnotatorExtension;
        private InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension;


        public IdeaLanguageAttachment languageObject() { return this.languageObject; }

        public InstanceLanguageExtensionPoint<?> parserDefinitionExtension() { return this.parserDefinitionExtension; }

        public void setParserDefinitionExtension(final InstanceLanguageExtensionPoint<?> parserDefinitionExtension) { this.parserDefinitionExtension = parserDefinitionExtension; }

        public InstanceLanguageExtensionPoint<?> externalAnnotatorExtension() { return this.externalAnnotatorExtension; }

        public void setExternalAnnotatorExtension(final InstanceLanguageExtensionPoint<?> externalAnnotatorExtension) { this.externalAnnotatorExtension = externalAnnotatorExtension; }

        public InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension() { return this.syntaxHighlighterFactoryExtension; }

        public void setSyntaxHighlighterFactoryExtension(final InstanceSyntaxHighlighterFactoryExtensionPoint syntaxHighlighterFactoryExtension) { this.syntaxHighlighterFactoryExtension = syntaxHighlighterFactoryExtension; }

        public RegisteredIdeaLanguageObject(final IdeaLanguageAttachment languageObject) {
            this.languageObject = languageObject;
        }

    }
}
