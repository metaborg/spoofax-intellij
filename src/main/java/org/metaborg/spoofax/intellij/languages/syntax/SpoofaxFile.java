package org.metaborg.spoofax.intellij.languages.syntax;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.spoofax.intellij.languages.SpoofaxFileType;

import javax.swing.*;

public final class SpoofaxFile extends PsiFileBase {

    @NotNull private final SpoofaxFileType fileType;

    public SpoofaxFile(@NotNull FileViewProvider viewProvider, SpoofaxFileType fileType) {
        super(viewProvider, fileType.getLanguage());
        this.fileType = fileType;
    }

    @NotNull
    public FileType getFileType() {
        return this.fileType;
    }

    // TODO
    @Override
    public String toString() {
        return "Test File!";
    }

    // TODO
    @Nullable
    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}