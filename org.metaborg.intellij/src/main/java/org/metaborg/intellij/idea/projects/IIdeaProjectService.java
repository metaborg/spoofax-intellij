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

package org.metaborg.intellij.idea.projects;

import com.intellij.openapi.module.*;
import com.intellij.psi.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.project.*;

import javax.annotation.*;

/**
 * Project service for IntelliJ IDEA.
 */
public interface IIdeaProjectService extends IProjectService {

//    /**
//     * Creates a new project.
//     *
//     * The project is not opened.
//     *
//     * @return The project.
//     */
//    IdeaProject create(Module module, final FileObject rootFolder);

    /**
     * Indicates to the project service that a Spoofax project was opened.
     *
     * @param module The module.
     * @param rootFolder The root folder of the project.
     * @return The project that was opened; or <code>null</code>.
     */
    @Nullable
    IdeaProject open(Module module, FileObject rootFolder);

    /**
     * Indicates to the project service that a Spoofax project was opened.
     *
     * @param project The Spoofax project that was opened.
     */
    void open(IdeaProject project);

    /**
     * Indicates to the project service that a Spoofax project was closed.
     *
     * @param module The IntelliJ module of the project.
     */
    void close(Module module);

    /**
     * Retrieves the Spoofax project of a given IntelliJ module.
     *
     * This method will return null when there is no {@link IdeaProject}
     * associated with the given module.
     *
     * @param module The IntelliJ module.
     * @return The corresponding Spoofax project;
     * or <code>null</code> if no project could be found.
     */
    @Nullable
    IdeaProject get(Module module);

    /**
     * Retrieves the Spoofax project of a given PSI element.
     *
     * This method will return null when there is no {@link IdeaProject}
     * associated with the IntelliJ IDEA module of the given PSI element.
     *
     * @param element The PSI element.
     * @return The corresponding Spoofax project;
     * or <code>null</code> if no project could be found.
     */
    @Nullable
    IdeaProject get(PsiElement element);

}
