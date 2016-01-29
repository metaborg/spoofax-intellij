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

//import org.apache.commons.vfs2.FileObject;
//import org.jetbrains.annotations.NotNull;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.ITransformGoal;
import org.metaborg.core.language.ILanguageImpl;
//import org.metaborg.spoofax.core.menu.TransformAction;

//import java.util.List;

/**
 * Executes a transformation action on resources.
 */
public interface IResourceTransformer {

    /**
     * Executes the specified action.
     *
     * @param language  The language implementation.
     * @param resources The active resources.
     * @param goal      The transformation goal.
     */
    boolean execute(
            final Iterable<TransformResource> resources, final ILanguageImpl language,
            final ITransformGoal goal)
            throws MetaborgException;
}
