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

package org.metaborg.intellij.idea.gui.languagespanel;

import org.metaborg.core.language.*;

import javax.annotation.Nullable;
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