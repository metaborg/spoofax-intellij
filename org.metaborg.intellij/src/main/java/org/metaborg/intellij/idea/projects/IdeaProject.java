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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.config.IProjectConfig;
import org.metaborg.core.project.Project;

import javax.annotation.Nullable;

/**
 * An IntelliJ IDEA project.
 */
public class IdeaProject extends Project {

    private final Module module;

    @Inject
    /* package private */ IdeaProject(
            @Assisted final Module module,
            @Assisted final FileObject location,
            @Assisted @Nullable final IProjectConfig config) {
        super(location, config);
        this.module = module;
    }

    /**
     * Gets the IDE-specific module.
     *
     * @return The module.
     */
    public final Module getModule() {
        return this.module;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.module.getName();
    }
}