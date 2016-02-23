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

package org.metaborg.intellij.jps;

import sun.misc.*;

import javax.annotation.*;
import java.io.*;
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

    private final ClassLoader parent;

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     * @param urls The URLs from which to load classes and resources.
     * @param factory The factory used to create URLs.
     */
    public PriorityURLClassLoader(final ClassLoader parent, final URL[] urls, final URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
        this.parent = parent;
    }

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     * @param urls The URLs from which to load classes and resources.
     */
    public PriorityURLClassLoader(final ClassLoader parent, final URL[] urls) {
        super(urls, parent);

        this.parent = parent;
    }

    /**
     * Initializes a new instance of the {@link PriorityURLClassLoader} class.
     *
     * @param parent The parent class loader.
     */
    public PriorityURLClassLoader(final ClassLoader parent) {
        this(parent, new URL[0]);
    }

    /**
     * Creates a new instance of the {@link PriorityURLClassLoader} that includes
     * all URLs from the parent that share the specified base URL.
     *
     * @param baseUrl The base URL.
     * @param parent The parent class loader.
     * @return The created instance.
     */
    public static PriorityURLClassLoader createWithBaseUrl(final URL baseUrl, final URLClassLoader parent) {
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
            if (cls == null) {
                // Class was not loaded yet.
                // Instead of asking the parent,
                // we first try to find it ourselves.
                try {
                    cls = findClass(name);
                } catch (final ClassNotFoundException ex) {
                    // Ignored.
                }

            }
            if (cls == null) {
                // Class was still not found.
                // Ask our parent.
                try {
                    cls = this.parent.loadClass(name);
                } catch (final ClassNotFoundException ex) {
                    // Ignored.
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

        return new CompoundEnumeration<>(resources);
    }
}
