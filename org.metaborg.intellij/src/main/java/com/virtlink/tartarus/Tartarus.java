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

package com.virtlink.tartarus;

import org.slf4j.*;

import javax.annotation.*;
import java.net.*;
import java.util.*;

/**
 * The deep abyss that is used as a dungeon of torment and suffering for the wicked and as the prison for those
 * who dare to cause JAR version conflicts.
 */
public class Tartarus {

    private final Logger logger;

    /**
     * Initializes a new instance of the {@link Tartarus} class.
     *
     * @param logger The logger to use.
     */
    public Tartarus(final Logger logger) {
        this.logger = logger;
    }

    public void logClassUrls(final String className) {
        final List<URL> classUrls = findClassUrls(className);

        this.logger.info("Class {} found at:", className);
        for (final URL classUrl : classUrls) {
            this.logger.info("- {}", classUrl);
        }
        this.logger.info("  ({} urls)", classUrls.size());
    }

    private List<URL> findClassUrls(final String className) {
        final List<ClassLoader> loaders = findAllClassLoaders();
        final List<URL> classUrls = new ArrayList<>();
        for (final ClassLoader loader : loaders) {
            if (loader instanceof URLClassLoader) {
                final URLClassLoader urlLoader = (URLClassLoader)loader;

                final String path = className.replace('.', '/').concat(".class");
                @Nullable final URL url = urlLoader.getResource(path);
                if (url != null) {
                    classUrls.add(url);
                }
            }
        }
        return classUrls;
    }

    private List<ClassLoader> findAllClassLoaders() {

        ClassLoader loader = getClass().getClassLoader();

        final List<ClassLoader> classLoaders = new ArrayList<>();

        while (loader != null) {
            classLoaders.add(loader);
            loader = loader.getParent();
        }

        return classLoaders;
    }
}
