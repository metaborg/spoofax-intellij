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

import com.google.common.collect.Sets;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public final class CompoundProjectServiceTests {

    private static final String SDF_DEPENDENCY = "res:languages/org.metaborg.meta.lang.sdf-1.5.0-SNAPSHOT.spoofax-language";

    @Test
    public void getsOneProjectFromMultipleProjectServices() throws FileSystemException {
        FileSystemManager manager = VFS.getManager();
        CompoundProjectService sut = new CompoundProjectService(Sets.newHashSet(
                new ProjectServiceStub(null),
                new ArtifactProjectService(manager),
                new ProjectServiceStub(null)
        ));

        FileObject artifact = manager.resolveFile("zip:" + manager.resolveFile(SDF_DEPENDENCY).getURL());

        IProject project = sut.get(artifact);

        assertNotNull(project);
    }

    @Test
    public void returnsNullWhenNoServiceReturnsAProject() throws FileSystemException {
        FileSystemManager manager = VFS.getManager();
        CompoundProjectService sut = new CompoundProjectService(Sets.newHashSet(
                new ProjectServiceStub(null),
                new ProjectServiceStub(null)
        ));

        FileObject artifact = manager.resolveFile("zip:" + manager.resolveFile(SDF_DEPENDENCY).getURL());

        IProject project = sut.get(artifact);

        assertNull(project);
    }

    @Test(expected = MultipleServicesRespondedException.class)
    public void throwsExceptionWhenMultipleProjectServicesReturnAProject() throws FileSystemException {
        FileSystemManager manager = VFS.getManager();
        CompoundProjectService sut = new CompoundProjectService(Sets.newHashSet(
                new ArtifactProjectService(manager),
                new ArtifactProjectService(manager)
        ));

        FileObject artifact = manager.resolveFile("zip:" + manager.resolveFile(SDF_DEPENDENCY).getURL());

        sut.get(artifact);
    }

}
