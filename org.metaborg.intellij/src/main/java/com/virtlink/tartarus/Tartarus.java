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
