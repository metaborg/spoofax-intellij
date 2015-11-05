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

package org.metaborg.spoofax.intellij.factories;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.core.menu.TransformAction;
import org.metaborg.spoofax.intellij.menu.TransformationAction;

/**
 * Factory for transformation actions.
 */
public interface ITransformIdeaActionFactory {

    /**
     * Creates a new transformation action.
     *
     * @param id       The ID of the action.
     * @param language The language implementation.
     * @param action   The action.
     * @return The created action.
     */
    @NotNull
    TransformationAction create(@NotNull String id, @NotNull ILanguageImpl language, @NotNull TransformAction action);

}
