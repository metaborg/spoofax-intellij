package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.vfs.SpoofaxFileType;

/**
 * A Spoofax source file.
 */
public final class SpoofaxFile extends PsiFileBase {

    @NotNull
    private final SpoofaxFileType fileType;

    /**
     * Initializes a new instance of the {@link SpoofaxFile} class.
     *
     * @param viewProvider The file view provider.
     * @param fileType     The file type.
     */
    public SpoofaxFile(@NotNull final FileViewProvider viewProvider, @NotNull final SpoofaxFileType fileType) {
        super(viewProvider, fileType.getLanguage());
        this.fileType = fileType;
    }

    /**
     * Gets the file type of the file.
     *
     * @return The file type.
     */
    @NotNull
    public FileType getFileType() {
        return this.fileType;
    }

}