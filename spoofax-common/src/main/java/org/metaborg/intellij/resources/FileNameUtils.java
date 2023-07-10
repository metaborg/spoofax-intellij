/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.resources;

import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.provider.LayeredFileName;

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
    public static FileName getOuterFileName(final FileName fileName) {
        if (fileName == null) {
          throw new NullPointerException();
        }

        if (fileName instanceof LayeredFileName) {
            return ((LayeredFileName)fileName).getOuterName();
        } else {
            return null;
        }
    }
}
