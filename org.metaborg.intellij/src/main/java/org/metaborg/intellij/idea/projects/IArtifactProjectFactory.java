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

import org.apache.commons.vfs2.*;
import org.metaborg.core.config.*;

import javax.annotation.*;

/**
 * Factory for IntelliJ IDEA language specification projects.
 */
public interface IArtifactProjectFactory {

    /**
     * Creates a new artifact project.
     *
     * @param artifactRoot The artifact root.
     * @param config The configuration of the artifact;
     *               or <code>null</code> to get it from the root folder.
     * @return The created project; or <code>null</code> when no project
     * could be created for the specified artifact.
     */
    @Nullable ArtifactProject create(FileObject artifactRoot,
                            @Nullable IProjectConfig config);

}
