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
import org.apache.commons.vfs2.*;
import org.metaborg.spoofax.meta.core.config.*;
import org.metaborg.spoofax.meta.core.project.*;

import javax.annotation.*;

/**
 * Factory for IntelliJ IDEA language specification projects.
 */
public interface IIdeaLanguageSpecFactory {

    /**
     * Creates a new project.
     *
     * @param module The IntelliJ IDEA module.
     * @param rootFolder Root folder of the module.
     * @param config The configuration of the project;
     *               or <code>null</code> to get it from the root folder.
     * @return The created project; or <code>null</code> when no project
     * could be created for the specified module.
     */
    @Nullable IdeaLanguageSpec create(Module module,
                            FileObject rootFolder,
                            @Nullable ISpoofaxLanguageSpecConfig config);

    // TODO: Implement this factory and ensure the given module is of type MetaborgModuleType.

}
