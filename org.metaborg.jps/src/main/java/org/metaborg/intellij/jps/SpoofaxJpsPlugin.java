/*
 * Copyright © 2015-2016
 *
 * This file is part of org.metaborg.intellij.
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

import com.google.inject.*;
import org.apache.commons.lang3.concurrent.*;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.spoofax.core.*;
import org.metaborg.spoofax.meta.core.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.intellij.openapi.diagnostic.Logger;

public interface SpoofaxJpsPlugin {

//    /**
//     * Gets the injector.
//     *
//     * @return The current injector.
//     */
//    Injector injector();

    /**
     * Gets the facade.
     *
     * @return The facade.
     */
    Spoofax spoofax();

    /**
     * Gets the meta facade.
     *
     * @return The meta facade.
     */
    SpoofaxMeta spoofaxMeta();

    /**
     * Injects the members of the specified object.
     *
     * @param obj The object whose members to inject.
     */
    void injectMembers(Object obj);

//    /**
//     * Returns an equivalent class from the correct classloader.
//     *
//     * @param cls The class.
//     * @param <T> The super type.
//     * @return The new class.
//     */
//    <T> Class<T> loadClass(Class<? extends T> cls) throws ClassNotFoundException;

    /**
     * Returns an instance for the given injection type.
     *
     * @throws ConfigurationException
     * @throws ProvisionException
     */
    <T> T getInstance(Class<T> type);

//    /**
//     * Returns an instance for the given injection type.
//     *
//     * @throws ConfigurationException
//     * @throws ProvisionException
//     */
//    <T> T getInstance(Key<T> key);


    /**
     * Cleans up the plugin.
     */
    void close();

    // Static //

    static final Logger LOG = Logger.getInstance(SpoofaxJpsPlugin.class);

    LazyInitializer<SpoofaxJpsPlugin> pluginLazy = new LazyInitializer<SpoofaxJpsPlugin>() {
        @Override
        protected SpoofaxJpsPlugin initialize() throws ConcurrentException {
//            return new SpoofaxJpsPluginImpl();
            LOG.info("Getting current classloader.");
            final ClassLoader parentClassLoader = this.getClass().getClassLoader();
            final PriorityURLClassLoader classLoader;
            final URL[] urls;
//            try {
//                urls = new URL[] {
//                        URI.create("").toURL()
//                };
//            } catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            }
            if (parentClassLoader instanceof URLClassLoader) {
                urls = ((URLClassLoader)parentClassLoader).getURLs();
            } else {
                urls = new URL[0];
            }
            final URL[] priorityUrls = Arrays.stream(urls).filter(url -> url.toString().contains("org.metaborg.intellij/lib/")).toArray(URL[]::new);
//            LOG.info("Found urls: " + String.join(", ", Arrays.stream(urls).map(URL::toString).collect(Collectors.toList())));
            classLoader = new PriorityURLClassLoader(parentClassLoader, priorityUrls, new String[] {
                    "org.metaborg.intellij.jps.SpoofaxJpsPlugin",
//                    "org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer"
            });
            LOG.info("Created class loader: " + classLoader.toString());
            for (URL u : classLoader.getURLs()) {
                LOG.info("  " + u.toString());
            }
            Thread.currentThread().setContextClassLoader(classLoader);
            LOG.info("Set context class loader.");
            try {
                @SuppressWarnings("unchecked")
                final Class<SpoofaxJpsPlugin> pluginClass = (Class<SpoofaxJpsPlugin>)classLoader.loadClass("org.metaborg.intellij.jps.SpoofaxJpsPluginImpl");
                LOG.info("Found plugin class: " + pluginClass.toString());
                final SpoofaxJpsPlugin pluginObject = pluginClass.newInstance();
                LOG.info("Created plugin instance: " + pluginObject.toString());
                LOG.info("  with class loader: " + pluginObject.getClass().getClassLoader().toString());
                return pluginObject;
            } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new UnhandledException("An unexpected unhandled exception occurred during object creation.", e);
            }
        }
    };

    /**
     * Gets the plugin.
     *
     * @return The plugin.
     */
    static SpoofaxJpsPlugin plugin() {
        try {
            return pluginLazy.get();
        } catch (final ConcurrentException e) {
            throw new UnhandledException("An unexpected unhandled exception occurred during object creation.", e);
        }
    }
}