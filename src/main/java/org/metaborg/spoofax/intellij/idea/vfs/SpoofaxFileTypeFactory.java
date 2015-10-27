package org.metaborg.spoofax.intellij.idea.vfs;

import com.intellij.ide.highlighter.ArchiveFileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for non-language-specific file types.
 */
public final class SpoofaxFileTypeFactory extends FileTypeFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public void createFileTypes(@NotNull final FileTypeConsumer consumer) {
        consumer.consume(ArchiveFileType.INSTANCE, "spoofax-language");
        // NOTE: There's also:
        //   PlainTextFileType (*.txt, *.sh),
        //   NativeFileType (*.docx, *.chm), and
        //   UnknownFileType (*.lib, *.dll).
    }
}
