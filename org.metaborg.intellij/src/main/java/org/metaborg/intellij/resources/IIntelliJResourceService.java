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

package org.metaborg.intellij.resources;

import com.intellij.openapi.vfs.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.resource.*;

import javax.annotation.*;

/**
 * IntelliJ resource service.
 */
public interface IIntelliJResourceService extends IResourceService {

    /**
     * Converts an IntelliJ resource into a VFS resource.
     *
     * @param resource The IntelliJ resource to convert.
     * @return The corresponding VFS resource.
     */
    FileObject resolve(VirtualFile resource);

    /**
     * Converts a VFS resource into an IntelliJ resource, if possible.
     *
     * @param resource The VFS resource to convert.
     * @return The corresponding IntelliJ resource,
     * or <code>null</code> if it could not be converted.
     */
    @Nullable
    VirtualFile unresolve(FileObject resource);

}
