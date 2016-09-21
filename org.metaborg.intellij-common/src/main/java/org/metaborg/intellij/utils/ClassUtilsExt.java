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

package org.metaborg.intellij.utils;

import com.intellij.util.PathUtil;
import org.metaborg.intellij.UnhandledException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

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
