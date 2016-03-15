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
     * @return The Spoofax project that is closed;
     * or <code>null</code> if no project could be found.
     */
    @Nullable
    IdeaProject close(Module module);

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
