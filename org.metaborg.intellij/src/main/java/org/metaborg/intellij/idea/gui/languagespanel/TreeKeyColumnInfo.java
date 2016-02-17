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

import com.google.common.base.*;
import com.intellij.ui.treeStructure.treetable.*;

import javax.swing.table.*;

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
        Preconditions.checkNotNull(name);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public final TableCellRenderer getRenderer(final Object o) {
        return super.getRenderer(o);
    }

    @Override
    public final TableCellRenderer getCustomizedRenderer(final Object o, final TableCellRenderer renderer) {
        return super.getCustomizedRenderer(o, renderer);
    }

}
