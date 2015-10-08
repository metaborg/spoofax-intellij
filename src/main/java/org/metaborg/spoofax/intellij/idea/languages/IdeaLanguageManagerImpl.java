package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.idea.InstanceKeyedExtensionPoint;
import org.metaborg.spoofax.intellij.idea.InstanceLanguageExtensionPoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public final class IdeaLanguageManagerImpl implements IIdeaLanguageManager {

    @NotNull
    private final static String PARSER_DEFINITION_EXTENSION = "com.intellij.lang.parserDefinition";
    @NotNull
    private final static String SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION = "com.intellij.lang.syntaxHighlighterFactory";

    @NotNull
    private final IdeaLanguageObjectManager objectManager;
    @NotNull
    private final Map<ILanguageImpl, RegisteredIdeaLanguageObject> loadedLanguages = new HashMap<>();

    @Inject
    private IdeaLanguageManagerImpl(@NotNull final IdeaLanguageObjectManager objectManager) {
        this.objectManager = objectManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(@NotNull final ILanguageImpl language) {
        if (isLoaded(language))
            throw new IllegalArgumentException("Language '" + language + "' is already loaded.");
        RegisteredIdeaLanguageObject obj = new RegisteredIdeaLanguageObject(this.objectManager.get(language));

        installLanguage(obj);

        loadedLanguages.put(language, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unload(@NotNull final ILanguageImpl language) {
        if (!isLoaded(language))
            return false;
        RegisteredIdeaLanguageObject obj = this.loadedLanguages.remove(language);

        uninstallLanguage(obj);

        return obj != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLoaded(@NotNull final ILanguageImpl language) {
        return this.loadedLanguages.containsKey(language);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Set<ILanguageImpl> getLoaded() {
        return Collections.unmodifiableSet(this.loadedLanguages.keySet());
    }

    /**
     * Installs a language into IntelliJ.
     *
     * @param obj The language to install.
     */
    private void installLanguage(@NotNull final RegisteredIdeaLanguageObject obj) {
        obj.parserDefinitionExtension = new InstanceLanguageExtensionPoint(
                obj.languageObject.ideaLanguage,
                obj.languageObject.parserDefinition);
        obj.syntaxHighlighterFactoryExtension = new InstanceKeyedExtensionPoint(
                obj.languageObject.ideaLanguage,
                obj.languageObject.syntaxHighlighterFactory);

        registerExtension(PARSER_DEFINITION_EXTENSION, obj.parserDefinitionExtension);
        registerExtension(SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION, obj.syntaxHighlighterFactoryExtension);
        registerFileType(obj.languageObject.fileType);
    }

    /**
     * Removes a language from IntelliJ.
     *
     * @param obj The language to remove.
     */
    private void uninstallLanguage(@NotNull final RegisteredIdeaLanguageObject obj) {
        unregisterFileType(obj.languageObject.fileType);
        unregisterExtension(SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION, obj.syntaxHighlighterFactoryExtension);
        unregisterExtension(PARSER_DEFINITION_EXTENSION, obj.parserDefinitionExtension);
    }

    /**
     * Registers an extension.
     *
     * @param extensionPointName The extension point name.
     * @param value              The extension to register.
     */
    private void registerExtension(@NotNull final String extensionPointName, @NotNull final Object value) {
        Extensions.getRootArea().getExtensionPoint(extensionPointName).registerExtension(value);
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
     * Registers a file type.
     *
     * @param fileType The file type to register.
     */
    private void registerFileType(@NotNull final SpoofaxFileType fileType) {
        FileTypeManagerEx.getInstanceEx().registerFileType(fileType);
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        for (String ext : fileType.getExtensions()) {
            fileTypeManager.associateExtension(fileType, ext);
        }
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
     * A registered IDEA language object.
     */
    private final class RegisteredIdeaLanguageObject {

        @NotNull
        public final IdeaLanguageObject languageObject;
        public InstanceLanguageExtensionPoint<?> parserDefinitionExtension;
        public InstanceKeyedExtensionPoint<?> syntaxHighlighterFactoryExtension;

        public RegisteredIdeaLanguageObject(@NotNull final IdeaLanguageObject languageObject) {
            this.languageObject = languageObject;
        }

    }
}
