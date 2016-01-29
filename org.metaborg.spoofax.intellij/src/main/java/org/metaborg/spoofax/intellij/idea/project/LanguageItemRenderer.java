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

package org.metaborg.spoofax.intellij.idea.project;

import com.intellij.openapi.roots.ui.CellAppearanceEx;
import com.intellij.openapi.roots.ui.util.SimpleTextCellAppearance;
import com.intellij.ui.ColoredTableCellRenderer;
import org.metaborg.core.language.ILanguage;

import javax.swing.*;

public final class LanguageItemRenderer extends ColoredTableCellRenderer {
    @Override
    protected void customizeCellRenderer(
            final JTable table,
            final Object value,
            final boolean selected,
            final boolean hasFocus,
            final int row,
            final int column) {
        setPaintFocusBorder(false);
        setFocusBorderAroundIcon(true);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        if (value instanceof ILanguage) {
            final ILanguage item = (ILanguage)value;
            getCellAppearance(item).customize(this);
            setToolTipText("Spoofax tooltip!");
        }
    }

    private CellAppearanceEx getCellAppearance(final ILanguage item) {
        final Icon icon = SpoofaxIcons.INSTANCE.defaultIcon();
        final String name = item.name();
        return SimpleTextCellAppearance.regular(name, icon);
    }
}
