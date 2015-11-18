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

package org.metaborg.idea.gui;

import com.intellij.ui.LayeredIcon;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.util.ui.EmptyIcon;
import org.metaborg.core.language.ILanguage;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxIcons;

import javax.swing.*;

/**
 * Renderer for {@link ILanguage} objects in a list.
 */
public final class LanguageRenderer extends ListCellRendererWrapper<ILanguage> {

    private static final Icon EMPTY_ICON = EmptyIcon.ICON_18;

    public LanguageRenderer() {

    }

    @Override
    public void customize(
            final JList list,
            final ILanguage language,
            final int index,
            final boolean selected,
            final boolean hasFocus) {
        setIcon(getIcon(language));
        setText(language.name());
    }

    /**
     * Gets the icon to display for the specified language.
     *
     * @param language The language.
     * @return The icon.
     */
    private Icon getIcon(final ILanguage language) {
        LayeredIcon layeredIcon = new LayeredIcon(2);
        layeredIcon.setIcon(EMPTY_ICON, 0);
        final Icon icon = SpoofaxIcons.INSTANCE.Default;
        if (icon != null) {
            layeredIcon.setIcon(icon, 1, (- icon.getIconWidth() + EMPTY_ICON.getIconWidth())/2, (EMPTY_ICON.getIconHeight() - icon.getIconHeight())/2);
        }
        return layeredIcon;
    }
}
