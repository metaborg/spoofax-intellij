package org.metaborg.spoofax.intellij.idea.model;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class SpoofaxIcons {
    public static final SpoofaxIcons INSTANCE = new SpoofaxIcons();

    public Icon Default;

    private SpoofaxIcons() {
        this.Default = IconLoader.getIcon("/SpoofaxDefault.png");
    }
}