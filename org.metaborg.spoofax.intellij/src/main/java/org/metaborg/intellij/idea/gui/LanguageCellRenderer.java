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

package org.metaborg.intellij.idea.gui;

import com.intellij.ui.ColoredTreeCellRenderer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxIcons;

import javax.swing.*;

public class LanguageCellRenderer extends ColoredTreeCellRenderer {
    @Override
    public void customizeCellRenderer(
            final JTree tree,
            final Object value,
            final boolean selected,
            final boolean expanded,
            final boolean leaf,
            final int row,
            final boolean hasFocus) {
        this.setIcon(SpoofaxIcons.INSTANCE.defaultIcon());
    }
}
