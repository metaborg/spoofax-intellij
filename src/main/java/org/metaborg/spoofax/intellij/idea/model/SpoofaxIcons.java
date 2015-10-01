package org.metaborg.spoofax.intellij.idea.model;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class SpoofaxIcons {
    @NotNull
    public static final SpoofaxIcons INSTANCE = new SpoofaxIcons();

    @NotNull
    public final Icon Default;

    private SpoofaxIcons() {
        this.Default = IconLoader.getIcon("/SpoofaxDefault.png");
    }
}