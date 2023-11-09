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

package org.metaborg.intellij.jps.projects;

import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.*;

import jakarta.annotation.Nullable;

/**
 * A project service for JPS.
 */
public interface IJpsProjectService extends IProjectService {

    /**
     * Creates and adds a new project for the specified JPS module.
     *
     * @param module The JPS module.
     * @return The created project; or <code>null</code>.
     */
    @Nullable
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
