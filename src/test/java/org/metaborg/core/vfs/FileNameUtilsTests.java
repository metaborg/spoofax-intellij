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

package org.metaborg.core.vfs;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public final class FileNameUtilsTests {
    @Test
    public void getOuterFileNameOfLayeredFileName() throws FileSystemException {
        FileSystemManager manager = VFS.getManager();
        FileName fileName = manager.resolveURI("zip:ram:///test/archive.zip!/document.txt");
        FileName outerFileName = FileNameUtils.getOuterFileName(fileName);
        assertEquals("ram:///test/archive.zip", outerFileName.getURI());
    }

    @Test
    public void getOuterFileNameOfNoneLayeredFileName() throws FileSystemException {
        FileSystemManager manager = VFS.getManager();
        FileName fileName = manager.resolveURI("ram:///test/archive.zip");
        FileName outerFileName = FileNameUtils.getOuterFileName(fileName);
        assertNull(outerFileName);
    }
}
