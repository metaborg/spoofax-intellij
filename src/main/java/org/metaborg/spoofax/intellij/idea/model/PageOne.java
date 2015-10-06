package org.metaborg.spoofax.intellij.idea.model;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by daniel on 9/16/15.
 */
public final class PageOne {
    private JPanel panel;
    private JButton button1;
    private JButton button2;

    @NotNull
    public JPanel getPanel() {
        return this.panel;
    }
}
