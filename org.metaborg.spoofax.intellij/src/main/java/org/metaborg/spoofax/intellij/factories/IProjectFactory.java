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

import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.project.IntelliJProject;

/**
 * Factory for projects.
 */
public interface IProjectFactory {

    /**
     * Creates a new project.
     *
     * @param intellijModule The IntelliJ module.
     * @param contentPath    Where the module lives.
     * @return The created project.
     */
    @NotNull
    IntelliJProject create(@NotNull Module intellijModule, @NotNull FileObject contentPath);

}
