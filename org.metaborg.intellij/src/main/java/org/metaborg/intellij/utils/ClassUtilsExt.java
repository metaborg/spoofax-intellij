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

package org.metaborg.intellij.utils;

import com.intellij.util.*;
import org.metaborg.intellij.*;

import java.io.*;
import java.net.*;

/**
 * Utility functions for working with classes.
 */
public final class ClassUtilsExt {

    /**
     * Gets a path to the specified plugin's class.
     *
     * @param cls The class.
     * @return The resulting URL.
     */
    public static URL getPluginPath(final Class cls) {
        return getPluginRelativePath(cls, ".");
    }

    /**
     * Gets a path to something relative to the specified plugin's class.
     *
     * @param cls The class.
     * @param relativePath The relative path.
     * @return The resulting URL.
     */
    public static URL getPluginRelativePath(final Class cls, final String relativePath) {

        try {
            return new File(new File(PathUtil.getJarPathForClass(cls)).getParent())
                    .toURI()
                    .resolve(relativePath)
                    .toURL();
        } catch (final MalformedURLException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Gets a path to the specified class.
     *
     * @param cls The class.
     * @return The resulting URL.
     */
    private static URL getClassPath(final Class cls) {
        return getClassRelativePath(cls, ".");
    }

    /**
     * Gets a path to something relative to the specified class.
     *
     * @param cls The class.
     * @param relativePath The relative path.
     * @return The resulting URL.
     */
    private static URL getClassRelativePath(final Class cls, final String relativePath) {
        try {
            return cls
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .resolve(relativePath)
                    .toURL();
        } catch (final MalformedURLException | URISyntaxException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * To prevent instantiations.
     */
    private ClassUtilsExt() {}

}
