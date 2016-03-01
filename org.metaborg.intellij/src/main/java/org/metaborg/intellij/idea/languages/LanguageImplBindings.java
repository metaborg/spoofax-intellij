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

package org.metaborg.intellij.idea.languages;

import com.intellij.openapi.actionSystem.*;

/**
 * IntelliJ IDEA objects bound to a language implementation.
 */
/* package private */ final class LanguageImplBindings {

    private final DefaultActionGroup buildActionGroup;

    public DefaultActionGroup getBuildActionGroup() { return this.buildActionGroup; }

    /**
     * Initializes a new instance of the {@link LanguageImplBindings} class.
     *
     * @param buildActionGroup The build action group.
     */
    public LanguageImplBindings(final DefaultActionGroup buildActionGroup) {
        this.buildActionGroup = buildActionGroup;
    }
}
