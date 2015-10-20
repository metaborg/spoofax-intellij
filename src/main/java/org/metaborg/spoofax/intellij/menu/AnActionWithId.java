package org.metaborg.spoofax.intellij.menu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * An action with an associated ID.
 */
public abstract class AnActionWithId extends AnAction {

    @NotNull
    private final String id;

    @NotNull
    public String id() {
        return this.id;
    }

    public AnActionWithId(@NotNull String id) {
        this(id, (String)null, (String)null, (Icon)null);
    }

    public AnActionWithId(@NotNull String id, Icon icon) {
        this(id, (String)null, (String)null, icon);
    }

    public AnActionWithId(@NotNull String id, @Nullable String text) {
        this(id, text, (String)null, (Icon)null);
    }

    public AnActionWithId(@NotNull String id, @Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
        this.id = id;
    }

    @Override
    public abstract void actionPerformed(final AnActionEvent anActionEvent);
}
