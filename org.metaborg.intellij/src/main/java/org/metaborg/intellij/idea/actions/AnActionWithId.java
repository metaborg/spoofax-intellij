/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.actions;

import com.intellij.openapi.actionSystem.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

/**
 * An action with an associated ID.
 */
public abstract class AnActionWithId extends AnAction {

    private final String id;

//    /**
//     * Initializes a new instance of the {@link AnActionWithId} class.
//     *
//     * @param id The ID of the action.
//     */
//    protected AnActionWithId(final String id) {
//        this(id, null, null, null);
//    }

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

//    /**
//     * Initializes a new instance of the {@link AnActionWithId} class.
//     *
//     * @param id   The ID of the action.
//     * @param icon The icon of the action; or <code>null</code>.
//     */
//    protected AnActionWithId(final String id, final Icon icon) {
//        this(id, null, null, icon);
//    }
//
//    /**
//     * Initializes a new instance of the {@link AnActionWithId} class.
//     *
//     * @param id   The ID of the action.
//     * @param text The text of the action; or <code>null</code>.
//     */
//    protected AnActionWithId(final String id, @Nullable final String text) {
//        this(id, text, null, null);
//    }

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
