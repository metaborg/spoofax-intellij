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

package org.metaborg.spoofax.intellij.project;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;

// TODO: Move this to Spoofax core?

/**
 * A language project represented by a Spoofax artifact (*.spoofax-language).
 */
public final class ArtifactProject implements IProject {

    @NotNull
    private final FileObject location;

    /**
     * Initializes a new instance of the {@link ArtifactProject} class.
     *
     * @param location The location of the artifact's archive.
     */
    public ArtifactProject(@NotNull final FileObject location) {
        this.location = location;
    }

    @Override
    @NotNull
    public FileObject location() {
        return this.location;
    }
}
