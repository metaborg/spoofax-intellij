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

package org.metaborg.intellij.vfs;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.*;

/**
 * A file name used to refer to an IntelliJ file.
 */
@Deprecated
public final class IntelliJFileName extends AbstractFileName {

    public static final String SCHEME = "intellij";

    /**
     * Initializes a new instance of the {@link IntelliJFileName} class.
     *
     * @param absolutePath The absolute path of the file in the file system.
     * @param type The type of file.
     */
    public IntelliJFileName(final String absolutePath, final FileType type) {
        super(SCHEME, absolutePath, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileName createName(final String absolutePath, final FileType type) {
        return new IntelliJFileName(absolutePath, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void appendRootUri(final StringBuilder buffer, final boolean addPassword) {
        buffer.append(getScheme());
    }
}
