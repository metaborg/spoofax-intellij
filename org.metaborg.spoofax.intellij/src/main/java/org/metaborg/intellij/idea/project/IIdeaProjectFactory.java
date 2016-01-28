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

package org.metaborg.intellij.idea.project;

import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for IntelliJ IDEA projects.
 */
public interface IIdeaProjectFactory {

    /**
     * Creates a new project.
     *
     * @param ideaModule The IntelliJ IDEA module.
     * @param rootFolder Root folder of the module.
     * @return The created project.
     */
    @NotNull
    IdeaLanguageSpecProject create(@NotNull Module ideaModule, @NotNull FileObject rootFolder);

}
