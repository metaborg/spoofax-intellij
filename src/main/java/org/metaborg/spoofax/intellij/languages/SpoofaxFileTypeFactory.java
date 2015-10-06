package org.metaborg.spoofax.intellij.languages;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;

/**
 * Factory for Spoofax file types.
 */
@Singleton
public final class SpoofaxFileTypeFactory extends FileTypeFactory {

    private LanguageManager languageManager;
    private IdeaLanguageManager ideaLanguageManager;
    private ILanguageService languageService;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxFileTypeFactory() {
        IdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private final void inject(@NotNull final IdeaLanguageManager ideaLanguageManager, @NotNull final LanguageManager languageManager, @NotNull final ILanguageService languageService) {
        this.ideaLanguageManager = ideaLanguageManager;
        this.languageManager = languageManager;
        this.languageService = languageService;
    }

    @Override
    public final void createFileTypes(@NotNull final FileTypeConsumer consumer) {
        loadLanguages();
        for (SpoofaxFileType fileType : this.ideaLanguageManager.fileTypes()) {
            String extensionsStr = Joiner.on(FileTypeConsumer.EXTENSION_DELIMITER).join(fileType.getExtensions());
            consumer.consume(fileType, extensionsStr);
        }
    }

    /**
     * Loads all Spoofax languages.
     */
    private void loadLanguages() {
        this.languageManager.loadMetaLanguages();
        for (ILanguage language : this.languageService.getAllLanguages()) {
            this.ideaLanguageManager.register(language);
        }
    }
}
