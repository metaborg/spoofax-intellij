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

package org.metaborg.intellij.idea.gui2;

import com.google.common.base.Preconditions;
import com.intellij.openapi.ui.popup.PopupStep;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An action in a popup menu.
 */
public class PopupAction implements ActionListener {

    private final String text;
    private final Icon icon;

    public PopupAction(String text, Icon icon) {
        Preconditions.checkNotNull(text);

        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return this.text;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public boolean hasSubStep() {
        return false;
    }

    @Nullable
    public PopupStep createSubStep() {
        return null;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {

    }
}
