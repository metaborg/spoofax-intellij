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

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public final class ArtifactProjectServiceTests {

    private static final String SDF_DEPENDENCY =
            "res:languages/org.metaborg.meta.lang.sdf-1.5.0-SNAPSHOT.spoofax-language";

    @Test
    public void getsProjectForArtifact() throws FileSystemException {
        final FileSystemManager manager = VFS.getManager();

        final ArtifactProjectService sut = new ArtifactProjectService(manager);
        final FileObject artifact = manager.resolveFile("zip:" + manager.resolveFile(SDF_DEPENDENCY).getURL());

        final IProject project = sut.get(artifact);

        assertNotNull(project);
    }

    @Test
    public void getsProjectReturnsSameProjectForSameArtifact() throws FileSystemException {
        final FileSystemManager manager = VFS.getManager();
        final ArtifactProjectService sut = new ArtifactProjectService(manager);
        final FileObject artifact = manager.resolveFile("zip:" + manager.resolveFile(SDF_DEPENDENCY).getURL());

        final IProject project1 = sut.get(artifact);
        final IProject project2 = sut.get(artifact);

        assertNotNull(project1);
        assertSame(project1, project2);
    }

}
