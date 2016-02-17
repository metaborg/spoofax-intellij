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

package org.metaborg.intellij.idea.gui.languagespanel;

import org.metaborg.core.language.*;

import javax.annotation.*;
import javax.swing.*;

/**
 * A language tree node.
 * @param <V>
 */
public interface ILanguageTreeNode<V> {

    /**
     * Gets the value of the name column.
     *
     * @return The value; or <code>null</code>.
     */
    @Nullable
    String getName();

    /**
     * Gets the value of the group ID column.
     *
     * @return The value; or <code>null</code>.
     */
    @Nullable
    String getGroupId();

    /**
     * Gets the value of the version column.
     *
     * @return The value; or <code>null</code>.
     */
    @Nullable
    LanguageVersion getVersion();

    /**
     * Gets the value of the status column.
     *
     * @return The value; or <code>null</code>.
     */
    @Nullable
    LanguageStatus getStatus();

    /**
     * Gets the icon to display.
     *
     * @return The icon; or <code>null</code>.
     */
    @Nullable
    Icon getIcon();

    /**
     * Gets the underlying object.
     *
     * @return The underlying object; or <code>null</code>.
     */
    @Nullable
    V getValue();
}