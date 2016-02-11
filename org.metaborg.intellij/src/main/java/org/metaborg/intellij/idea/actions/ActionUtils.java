/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.intellij.idea.actions;

import com.intellij.openapi.actionSystem.*;

import javax.annotation.*;

/**
 * Utility functions for working with IntelliJ IDEA actions.
 */
public final class ActionUtils {

    /**
     * Adds an action(group) to a parent and registers all its children.
     *
     * @param action   The action to add.
     * @param parentID The parent ID.
     * @param relativeToActionId The ID relative to which to position this action; or <code>null</code>.
     * @param anchor The anchor indicating where to position this action; or <code>null</code> for the default.
     */
    public static void addAndRegisterActionGroup(final AnAction action, final String parentID, @Nullable final String relativeToActionId, @Nullable final Anchor anchor) {
        final ActionManager manager = ActionManager.getInstance();
        final DefaultActionGroup parent = (DefaultActionGroup)manager.getAction(parentID);
        parent.add(action, getActionConstraints(relativeToActionId, anchor));
        registerAction(manager, action);
    }

    /**
     * Gets an object that specifies where the action is positioned.
     *
     * @param relativeToActionId The action ID relative to which to position the action;
     *                           or <code>null</code> to position the action at the start or end.
     * @param anchor The anchor indicating where to position the action;
     *               or <code>null</code> to position the action after or at the end.
     * @return The {@link Constraints}.
     */
    private static Constraints getActionConstraints(@Nullable final String relativeToActionId, @Nullable final Anchor anchor) {
        if (relativeToActionId != null && anchor != null) {
            return new Constraints(anchor, relativeToActionId);
        } else if (relativeToActionId != null) {
            return new Constraints(Anchor.AFTER, relativeToActionId);
        } else if (anchor == Anchor.BEFORE || anchor == Anchor.FIRST) {
            return Constraints.FIRST;
        } else {
            return Constraints.LAST;
        }
    }

    /**
     * Registers the action and its children.
     *
     * @param action The action.
     */
    private static void registerAction(final ActionManager manager, final AnAction action) {
        if (action instanceof AnActionWithId) {
            manager.registerAction(((AnActionWithId)action).id(), action);
        }
        if (action instanceof DefaultActionGroup) {
            registerActionGroup(manager, (DefaultActionGroup)action);
        }
    }

    /**
     * Registers all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private static void registerActionGroup(final ActionManager manager, final DefaultActionGroup actionGroup) {
        for (final AnAction action : actionGroup.getChildActionsOrStubs()) {
            registerAction(manager, action);
        }
    }

    /**
     * Removes an action(group) from a parent and unregisters all its children.
     *
     * @param action   The action to remove.
     * @param parentID The parent ID.
     */
    public static void removeAndUnregisterActionGroup(final AnAction action, final String parentID) {
        final ActionManager manager = ActionManager.getInstance();
        final DefaultActionGroup parent = (DefaultActionGroup)manager.getAction(parentID);
        parent.remove(action);
        unregisterAction(manager, action);
    }

    /**
     * Unregisters the action and its children.
     *
     * @param action The action.
     */
    private static void unregisterAction(final ActionManager manager, final AnAction action) {
        if (action instanceof AnActionWithId) {
            manager.unregisterAction(((AnActionWithId)action).id());
        }
        if (action instanceof DefaultActionGroup) {
            unregisterActionGroup(manager, (DefaultActionGroup)action);
        }
    }

    /**
     * Unregisters all actions in the specified group.
     *
     * @param actionGroup The action group.
     */
    private static void unregisterActionGroup(
            final ActionManager manager,
            final DefaultActionGroup actionGroup) {
        for (final AnAction action : actionGroup.getChildActionsOrStubs()) {
            unregisterAction(manager, action);
        }
    }

    private ActionUtils() { /* Prevent instantiation. */ }

}