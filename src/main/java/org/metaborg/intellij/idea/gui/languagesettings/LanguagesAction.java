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

package org.metaborg.intellij.idea.gui.languagesettings;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.gui.languagespanel.LanguageTreeModel;

import jakarta.annotation.Nullable;
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
