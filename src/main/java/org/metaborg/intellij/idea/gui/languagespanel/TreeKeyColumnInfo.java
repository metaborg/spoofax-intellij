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

import com.intellij.ui.treeStructure.treetable.TreeColumnInfo;

import javax.swing.table.TableCellRenderer;

/**
 * Key column in a tree table view.
 */
public class TreeKeyColumnInfo extends TreeColumnInfo {

    /**
     * Initializes a new instance of the {@link TreeKeyColumnInfo} class.
     *
     * @param name The name of the column.
     */
    public TreeKeyColumnInfo(final String name) {
        super(name);
        if (name == null) {
          throw new NullPointerException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @org.jetbrains.annotations.Nullable
    @Override
    public final TableCellRenderer getRenderer(final Object o) {
        return super.getRenderer(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TableCellRenderer getCustomizedRenderer(final Object o, final TableCellRenderer renderer) {
        return super.getCustomizedRenderer(o, renderer);
    }

}
