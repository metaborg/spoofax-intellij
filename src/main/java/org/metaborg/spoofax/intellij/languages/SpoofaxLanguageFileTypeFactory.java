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
 * Factory for a single Spoofax language file type.
 */
public final class SpoofaxLanguageFileTypeFactory extends FileTypeFactory {

    private SpoofaxFileType fileType;

    public SpoofaxLanguageFileTypeFactory(@NotNull final SpoofaxFileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public final void createFileTypes(@NotNull final FileTypeConsumer consumer) {
        String extensionsStr = Joiner.on(FileTypeConsumer.EXTENSION_DELIMITER).join(fileType.getExtensions());
        consumer.consume(fileType, extensionsStr);
    }
}
