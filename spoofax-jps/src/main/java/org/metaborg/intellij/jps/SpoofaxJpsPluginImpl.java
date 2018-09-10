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

import com.google.inject.*;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang3.concurrent.*;
import org.metaborg.core.*;
import org.metaborg.spoofax.core.*;
import org.metaborg.spoofax.meta.core.*;

/**
 * JPS plugin class.
 *
 * The static member of this class are loaded when the JPS plugin is loaded.
 * They must only be used from JPS-specific classes.
 */
public final class SpoofaxJpsPluginImpl implements SpoofaxJpsPlugin {

//    // Static //
//
    private static final Logger logger = Logger.getInstance(SpoofaxJpsPluginImpl.class);
//    private static final LazyInitializer<SpoofaxJpsPluginImpl> pluginLazy = new LazyInitializer<SpoofaxJpsPluginImpl>() {
//        @Override
//        protected SpoofaxJpsPluginImpl initialize() throws ConcurrentException {
//            return new SpoofaxJpsPluginImpl();
//        }
//    };
//
//    /**
//     * Gets the plugin.
//     *
//     * @return The plugin.
//     */
//    public static SpoofaxJpsPlugin plugin() {
//        try {
//            return pluginLazy.get();
//        } catch (final ConcurrentException e) {
//            throw new UnhandledException("An unexpected unhandled exception occurred during object creation.", e);
//        }
//    }

    // Instance //

    private final Spoofax spoofax;
    private final SpoofaxMeta spoofaxMeta;

    /**
     * Gets the injector.
     *
     * @return The current injector.
     */
    public Injector injector() {
        return this.spoofaxMeta.injector;
    }

    /**
     * Gets the facade.
     *
     * @return The facade.
     */
    public Spoofax spoofax() { return this.spoofax; }

    /**
     * Gets the meta facade.
     *
     * @return The meta facade.
     */
    public SpoofaxMeta spoofaxMeta() { return this.spoofaxMeta; }

    /**
     * Initializes a new instance of the {@link SpoofaxJpsPluginImpl} class.
     *
     * Note: This class is instantiated by invoking the class loader manually.
     */
    @SuppressWarnings("unused")
    public SpoofaxJpsPluginImpl() {
        logger.debug("Loading Spoofax for JPS plugin using classloader: ",
                this.getClass().getClassLoader().getClass().getName());

        try {
            this.spoofax = new Spoofax(new JpsSpoofaxModule());
            this.spoofaxMeta = new SpoofaxMeta(this.spoofax, new JpsSpoofaxMetaModule());
        } catch (final MetaborgException e) {
            throw new RuntimeException(e);
        }
        logger.info("Spoofax for JPS plugin loaded.");
    }

    /**
     * Injects the members of the specified object.
     *
     * @param obj The object whose members to inject.
     */
    public void injectMembers(Object obj) {
        this.injector().injectMembers(obj);
    }

    /**
     * Cleans up the plugin.
     */
    public void close() {
        this.spoofaxMeta.close();
        this.spoofax.close();
    }
}
