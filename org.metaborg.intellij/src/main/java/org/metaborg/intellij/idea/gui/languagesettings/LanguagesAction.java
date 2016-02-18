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

package org.metaborg.intellij.idea.gui.languagesettings;

import com.intellij.openapi.actionSystem.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.gui.languagespanel.*;

import javax.annotation.*;
import javax.swing.*;

/**
 * A language action.
 */
public abstract class LanguagesAction extends AnAction {

    protected final LanguageTreeModel model;
    protected final LanguagesSettings controller;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    protected LanguagesAction(final LanguageTreeModel model,
                           final LanguagesSettings controller,
                           @Nullable final String text,
                           @Nullable final String description,
                           @Nullable final Icon icon) {
        super(text, description, icon);
        this.model = model;
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void actionPerformed(final AnActionEvent e);

    /**
     * Adds the language discovery requests to the controller.
     *
     * @param requests The requests to add.
     */
    protected void addRequests(final Iterable<ILanguageDiscoveryRequest> requests) {

        for (final ILanguageDiscoveryRequest request : requests) {
            this.controller.addLanguageRequest(request);
            this.model.getOrAddLanguageRequestNode(request);
        }
        // TODO: Notify when not successful.
    }
}
