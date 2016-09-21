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

package org.metaborg.intellij.jps;

import com.intellij.openapi.diagnostic.Logger;
import sun.misc.CompoundEnumeration;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * URL class loader that gives its own URLs priority over
 * the parent class loader (i.e. a parent-last class loader).
 *
 * Instantiate and use the class loader like you would any URL class loader,
 * or call the {@link #createWithBaseUrl} static method to create an instance
 * that inherits the parent class loader's URLs, but loads classes with the
 * specified base URL before asking the parent. This can be used, for example,
 * to prioritize loading classes from your project's folder before loading
 * them from other locations.
 */
public final class PriorityURLClassLoader extends URLClassLoader {

    private static final Logger logger = Logger.getInstance(PriorityURLClassLoader.class);
    private final ClassLoader parent;
    private final Set<String> exceptions;

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     * @param urls The URLs from which to load classes and resources.
     * @param factory The factory used to create URLs.
     */
    public PriorityURLClassLoader(final ClassLoader parent, final URL[] urls, final URLStreamHandlerFactory factory, String[] exceptions) {
        super(urls, parent, factory);
        this.parent = parent;
        this.exceptions = new HashSet<String>(Arrays.asList(exceptions));
    }

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     * @param urls The URLs from which to load classes and resources.
     */
    public PriorityURLClassLoader(final ClassLoader parent, final URL[] urls, String[] exceptions) {
        super(urls, parent);

        this.parent = parent;
        this.exceptions = new HashSet<String>(Arrays.asList(exceptions));
    }

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     */
    public PriorityURLClassLoader(final ClassLoader parent, String[] exceptions) {
        this(parent, new URL[0], exceptions);
    }

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     * @param urls The URLs from which to load classes and resources.
     * @param factory The factory used to create URLs.
     */
    public PriorityURLClassLoader(final ClassLoader parent, final URL[] urls, final URLStreamHandlerFactory factory) {
        this(parent, urls, factory, new String[0]);
    }

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     * @param urls The URLs from which to load classes and resources.
     */
    public PriorityURLClassLoader(final ClassLoader parent, final URL[] urls) {
        this(parent, urls, new String[0]);
    }

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     */
    public PriorityURLClassLoader(final ClassLoader parent) {
        this(parent, new String[0]);
    }

    /**
     * Creates a new instance of the {@link PriorityURLClassLoader} that includes
     * all URLs from the parent that share the specified base URL.
     *
     * @param parent The parent class loader.
     * @param baseUrl The base URL.
     * @return The created instance.
     */
    public static PriorityURLClassLoader createWithBaseUrl(final URLClassLoader parent, final URL baseUrl) {
        final List<URL> urls = new ArrayList<>();
        for (final URL url : parent.getURLs()) {
            if (isDescendant(baseUrl, url)) {
                urls.add(url);
            }
        }
        return new PriorityURLClassLoader(parent, urls.toArray(new URL[urls.size()]));
    }

    /**
     * Determines whether one URL is a descendant of another URL.
     *
     * @param base The base URL.
     * @param descendant The possible descendant.
     * @return <code>true</code> when the descendant URL is actually a descendant
     * of the base URL; otherwise, <code>false</code>.
     */
    private static boolean isDescendant(final URL base, final URL descendant) {

        try {
            final URI baseUri = base.toURI();
            final URI descendantUri = descendant.toURI();
            return !baseUri.relativize(descendantUri).isAbsolute();
        } catch (final URISyntaxException e) {
            // Ignore.
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> loadClass(final String name, final boolean resolve)
            throws ClassNotFoundException {
        // Let's find us a class.
        synchronized (this.getClassLoadingLock(name)) {
            // Maybe we already loaded the class?
            @Nullable Class<?> cls = findLoadedClass(name);
            if (this.exceptions.contains(name)) {
                // Parent-first
                if (cls == null) {
                    // Class was not loaded yet.
                    // Ask our parent.
                    cls = findClassAtParent(name);
                }
                if (cls == null) {
                    // Class was still not found.
                    // Try to find it ourselves.
                    cls = findClassHere(name);
                }
            } else {
                // Parent-last
                if (cls == null) {
                    // Class was not loaded yet.
                    // Instead of asking the parent,
                    // we first try to find it ourselves.
                    cls = findClassHere(name);
                }
                if (cls == null) {
                    // Class was still not found.
                    // Ask our parent.
                    cls = findClassAtParent(name);
                }
            }
            if (cls == null) {
                // Class is still nowhere to be found. We give up.
                throw new ClassNotFoundException("Class was not found: " + name);
            }
            if (resolve) {
                resolveClass(cls);
            }
            // Yay! Return class and make our parent proud.
            return cls;
        }
    }

    @Nullable
    private Class<?> findClassHere(final String name) {
        @Nullable Class<?> cls = null;
        try {
            cls = findClass(name);
        } catch (final ClassNotFoundException ex) {
            // Ignored.
        }
        if (cls != null)
            logger.debug("WE HAV loaded class:" + name);
        return cls;
    }

    @Nullable
    private Class<?> findClassAtParent(final String name) {
        @Nullable Class<?> cls = null;
        try {
            cls = this.parent.loadClass(name);
        } catch (final ClassNotFoundException ex) {
            // Ignored.
        }
        if (cls != null)
            logger.debug("PARENT loaded class:" + name);
        return cls;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getResource(final String name) {
        // Let's find us a resource.

        // Instead of asking our parent first,
        // we'll see if we can find it on our own.

        @Nullable URL url = findResource(name);
        if (url == null) {
            // Not yet found. Let's ask our parent.
            url = this.parent.getResource(name);
        }
        // Maybe or maybe not found. Let's return what we have,
        // which may be null.
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<URL> getResources(final String name) throws IOException {
        // Let's find us all resources.
        @SuppressWarnings("unchecked")
        final Enumeration<URL>[] resources = (Enumeration<URL>[]) new Enumeration<?>[2];

        // Instead of returning our parent's resources first,
        // we'll return our own resources first.
        resources[0] = findResources(name);
        resources[1] = this.parent.getResources(name);

        // TODO: Replace by our own implementation. It's simple!
        // First return all elements from the enumeration at index 0,
        // then all elements from the enumeration at index 1,
        // etc.. Or generalize to take an enumeration of enumerations
        // and flatten them?
        return new CompoundEnumeration<>(resources);
    }
}
