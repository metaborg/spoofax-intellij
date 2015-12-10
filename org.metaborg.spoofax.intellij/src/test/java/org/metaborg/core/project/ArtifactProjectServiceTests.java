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

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class ArtifactProjectServiceTests {

    private static final String SDF_DEPENDENCY = "res:languages/org.metaborg.meta.lang.sdf-1.5.0-SNAPSHOT.spoofax-language";

//    @Test
//    public void testTest() throws IOException {
//        FileSystemManager manager = VFS.getManager();
//        FileObject f = manager.resolveFile("res:languages/org.metaborg.meta.lang.sdf-1.5.0-SNAPSHOT.spoofax-language");
////        FileObject f = manager.resolveFile("res:src-gen/metaborg.generated.yaml");
//        assertTrue(f.exists());
////        InputStream stream = f.getContent().getInputStream();
////        String content = IOUtils.toString(stream);
////        System.out.println(content);
//    }

    @Test
    public void getsProjectForArtifact() throws FileSystemException {
        FileSystemManager manager = VFS.getManager();

        ArtifactProjectService sut = new ArtifactProjectService(manager);
        FileObject artifact = manager.resolveFile("zip:" + manager.resolveFile(SDF_DEPENDENCY).getURL());

        IProject project = sut.get(artifact);

        assertNotNull(project);
    }

    @Test
    public void getsProjectReturnsSameProjectForSameArtifact() throws FileSystemException {
        FileSystemManager manager = VFS.getManager();
        ArtifactProjectService sut = new ArtifactProjectService(manager);
        FileObject artifact = manager.resolveFile("zip:" + manager.resolveFile(SDF_DEPENDENCY).getURL());

        IProject project1 = sut.get(artifact);
        IProject project2 = sut.get(artifact);

        assertNotNull(project1);
        assertSame(project1, project2);
    }

}
