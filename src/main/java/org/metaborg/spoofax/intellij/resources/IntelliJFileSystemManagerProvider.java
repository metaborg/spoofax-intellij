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

package org.metaborg.spoofax.intellij.resources;

import org.metaborg.core.resource.DefaultFileSystemManagerProvider;

/**
 * Installs the <code>idea://</code> URI schema in the VFS.
 */
public final class IntelliJFileSystemManagerProvider extends DefaultFileSystemManagerProvider {

    // NOTE: You ideally don't want to copy the `addProvider "file"` just because you changed the default provider.
    // Perhaps it's cleaner to add all providers in `addProviders`, and then return the provider you want to be
    // the default. If you don't want to change the default, just `return super.addProviders(manager)`.

    // FIXME: The `idea://` provider was the default, but this caused issues:
    // org.apache.commons.vfs2.FileSystemException: Could not determine the type of file "idea:///home/daniel/eclipse/spoofax1507/workspace/TestProject/untitled20/pom.xml".
    // Therefore, I used the default `file://` provider, and just added `idea://` without making it the default.

}