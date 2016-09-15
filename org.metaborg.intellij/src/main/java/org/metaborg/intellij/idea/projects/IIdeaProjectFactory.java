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

import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.config.*;

import javax.annotation.Nullable;

/**
 * Factory for IntelliJ IDEA projects.
 */
public interface IIdeaProjectFactory {

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
    @Nullable IdeaProject create(Module module,
                                 FileObject rootFolder,
                                 @Nullable IProjectConfig config);

}
