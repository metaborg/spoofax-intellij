/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.menu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * An action with an associated ID.
 */
public abstract class AnActionWithId extends AnAction {

    private final String id;

    /**
     * Initializes a new instance of the {@link AnActionWithId} class.
     *
     * @param id The ID of the action.
     */
    protected AnActionWithId(final String id) {
        this(id, null, null, null);
    }

    /**
     * Initializes a new instance of the {@link AnActionWithId} class.
     *
     * @param id          The ID of the action.
     * @param text        The text of the action; or <code>null</code>.
     * @param description The description of the action; or <code>null</code>.
     * @param icon        The icon of the action; or <code>null</code>.
     */
    protected AnActionWithId(
            final String id,
            @Nullable final String text,
            @Nullable final String description,
            @Nullable final Icon icon) {
        super(text, description, icon);
        this.id = id;
    }

    /**
     * Initializes a new instance of the {@link AnActionWithId} class.
     *
     * @param id   The ID of the action.
     * @param icon The icon of the action; or <code>null</code>.
     */
    protected AnActionWithId(final String id, final Icon icon) {
        this(id, null, null, icon);
    }

    /**
     * Initializes a new instance of the {@link AnActionWithId} class.
     *
     * @param id   The ID of the action.
     * @param text The text of the action; or <code>null</code>.
     */
    protected AnActionWithId(final String id, @Nullable final String text) {
        this(id, text, null, null);
    }

    /**
     * Gets the ID of the action.
     *
     * @return The ID of the action.
     */
    public String id() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void actionPerformed(final AnActionEvent anActionEvent);
}
