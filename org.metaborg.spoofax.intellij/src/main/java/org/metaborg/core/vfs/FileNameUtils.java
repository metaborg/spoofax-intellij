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

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.provider.LayeredFileName;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Utility functions for working with Apache {@link FileName} objects.
 */
@Singleton
public final class FileNameUtils {
    // To prevent instantiation.
    private FileNameUtils() {}

    /**
     * Gets the outer file name of the specified file name.
     * <p>
     * For example, if the specified file name is <code>zip:file:///dir/archive.zip!/document.txt</code>,
     * then the outer file name is <code>file:///dir/archive.zip</code>.
     *
     * @param fileName The file name.
     * @return The outer file name; or <code>null</code> when there is none.
     */
    @Nullable
    public static FileName getOuterFileName(@NotNull final FileName fileName) {
        Preconditions.checkNotNull(fileName);

        if (fileName instanceof LayeredFileName) {
            return ((LayeredFileName)fileName).getOuterName();
        } else {
            return null;
        }
    }
}
