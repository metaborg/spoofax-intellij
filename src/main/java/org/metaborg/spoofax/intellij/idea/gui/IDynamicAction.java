package org.metaborg.spoofax.intellij.idea.gui;

import com.intellij.openapi.actionSystem.ActionManager;
import org.jetbrains.annotations.NotNull;

/**
 * A dynamic action.
 */
public interface IDynamicAction {

    /**
     * Enables the action.
     *
     * @param manager The action manager.
     */
    void enable(@NotNull ActionManager manager);

    /**
     * Disables the action.
     *
     * @param manager The action manager.
     */
    void disable(@NotNull ActionManager manager);

}
