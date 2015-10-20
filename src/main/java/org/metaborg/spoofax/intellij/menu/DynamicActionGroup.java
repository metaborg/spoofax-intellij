package org.metaborg.spoofax.intellij.menu;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.jetbrains.annotations.NotNull;

/**
 * A dynamic action group.
 */
public final class DynamicActionGroup implements IDynamicAction {

    @NotNull
    private final String parentID;
    @NotNull
    private final DefaultActionGroup actionGroup;

    public DynamicActionGroup(@NotNull final DefaultActionGroup actionGroup, @NotNull final String parentID) {
        this.actionGroup = actionGroup;
        this.parentID = parentID;
    }

    @Override
    public void enable(@NotNull final ActionManager manager) {
        DefaultActionGroup mainMenu = (DefaultActionGroup) manager.getAction(this.parentID);
        mainMenu.add(this.actionGroup);
        registerActions(manager, this.actionGroup);
    }

    @Override
    public void disable(@NotNull final ActionManager manager) {
        DefaultActionGroup mainMenu = (DefaultActionGroup) manager.getAction(this.parentID);
        mainMenu.remove(this.actionGroup);
        unregisterActions(manager, this.actionGroup);
    }

    /**
     * Registers all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private void registerActions(@NotNull final ActionManager manager, @NotNull final DefaultActionGroup actionGroup) {
        for (AnAction action : actionGroup.getChildActionsOrStubs()) {
            if (action instanceof AnActionWithId) {
                manager.registerAction(((AnActionWithId)action).id(), action);
            }
            if (action instanceof DefaultActionGroup) {
                registerActions(manager, (DefaultActionGroup)action);
            }
        }
    }

    /**
     * Unregisters all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private void unregisterActions(@NotNull final ActionManager manager, @NotNull final DefaultActionGroup actionGroup) {
        for (AnAction action : actionGroup.getChildActionsOrStubs()) {
            if (action instanceof AnActionWithId) {
                manager.unregisterAction(((AnActionWithId) action).id());
            }
            if (action instanceof DefaultActionGroup) {
                unregisterActions(manager, (DefaultActionGroup)action);
            }
        }
    }
}
