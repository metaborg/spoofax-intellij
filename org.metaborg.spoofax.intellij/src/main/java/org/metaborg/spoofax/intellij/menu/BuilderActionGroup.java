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

package org.metaborg.spoofax.intellij.menu;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.metaborg.core.language.ILanguageImpl;

/**
 * Action group for a language builder.
 */
public final class BuilderActionGroup extends DefaultActionGroup {

    private final ILanguageImpl implementation;
    private final ActionHelper actionHelper;

    /**
     * Initializes a new instance of the {@link BuilderActionGroup} class.
     *
     * @param implementation The implementation to respond to.
     */
    @Inject
    /* package private */ BuilderActionGroup(
            @Assisted final ILanguageImpl implementation,
            final ActionHelper actionHelper) {
        super(implementation.belongsTo().name(), true);
        this.implementation = implementation;
        this.actionHelper = actionHelper;
    }

    @Override
    public void update(final AnActionEvent e) {
        final boolean visible = this.actionHelper.isActiveFileLanguage(e, this.implementation);
        e.getPresentation().setVisible(visible);
        super.update(e);
    }

}
