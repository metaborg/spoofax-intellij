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

    /**
     * Gets the ID of the action.
     *
     * @return The ID of the action.
     */
    @NotNull
    public String id() {
        return this.id;
    }

    /**
     * Initializes a new instance of the {@link AnActionWithId} class.
     *
     * @param id The ID of the action.
     */
    public AnActionWithId(@NotNull String id) {
        this(id, (String)null, (String)null, (Icon)null);
    }

    /**
     * Initializes a new instance of the {@link AnActionWithId} class.
     *
     * @param id The ID of the action.
     * @param icon The icon of the action; or <code>null</code>.
     */
    public AnActionWithId(@NotNull String id, Icon icon) {
        this(id, (String)null, (String)null, icon);
    }

    /**
     * Initializes a new instance of the {@link AnActionWithId} class.
     *
     * @param id The ID of the action.
     * @param text The text of the action; or <code>null</code>.
     */
    public AnActionWithId(@NotNull String id, @Nullable String text) {
        this(id, text, (String)null, (Icon)null);
    }

    /**
     * Initializes a new instance of the {@link AnActionWithId} class.
     *
     * @param id The ID of the action.
     * @param text The text of the action; or <code>null</code>.
     * @param description The description of the action; or <code>null</code>.
     * @param icon The icon of the action; or <code>null</code>.
     */
    public AnActionWithId(@NotNull String id, @Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void actionPerformed(final AnActionEvent anActionEvent);
}
