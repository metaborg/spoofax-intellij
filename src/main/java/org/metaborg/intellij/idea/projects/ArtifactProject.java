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

import com.google.common.base.Preconditions;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.config.IProjectConfig;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.Project;

import javax.annotation.Nullable;

// TODO: Move this to Spoofax core?

/**
 * A language project represented by a Spoofax artifact (*.spoofax-language).
 */
public final class ArtifactProject extends Project implements IProject {
    /**
     * Initializes a new instance of the {@link ArtifactProject} class.
     *
     * @param location The location of the artifact's archive.
     */
    public ArtifactProject(final FileObject location, @Nullable final IProjectConfig config) {
        super(location, config);
        Preconditions.checkNotNull(location);
    }
}
