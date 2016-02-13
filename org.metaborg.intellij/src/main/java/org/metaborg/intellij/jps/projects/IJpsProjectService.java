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

package org.metaborg.intellij.jps.projects;

import org.jetbrains.jps.model.module.*;
import org.metaborg.core.project.*;

import javax.annotation.*;

/**
 * A project service for JPS.
 */
public interface IJpsProjectService extends IProjectService {

    /**
     * Creates and adds a new project for the specified JPS module.
     *
     * @param module The JPS module.
     * @return The created project.
     */
    MetaborgJpsProject create(JpsModule module);

    /**
     * Finds the project corresponding to the specified module.
     *
     * @param module The JPS module to look for.
     * @return The project that corresponds to the JPS module;
     * or <code>null</code> when not found.
     */
    @Nullable
    MetaborgJpsProject get(JpsModule module);

}
