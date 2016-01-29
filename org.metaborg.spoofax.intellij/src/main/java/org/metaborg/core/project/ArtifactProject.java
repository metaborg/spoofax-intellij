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

package org.metaborg.core.project;

import com.google.common.base.Preconditions;
import org.apache.commons.vfs2.FileObject;

// TODO: Move this to Spoofax core?

/**
 * A language project represented by a Spoofax artifact (*.spoofax-language).
 */
public final class ArtifactProject implements IProject {

    private final FileObject location;

    /**
     * Initializes a new instance of the {@link ArtifactProject} class.
     *
     * @param location The location of the artifact's archive.
     */
    public ArtifactProject(final FileObject location) {
        Preconditions.checkNotNull(location);

        this.location = location;
    }

    @Override
    public FileObject location() {
        return this.location;
    }
}
