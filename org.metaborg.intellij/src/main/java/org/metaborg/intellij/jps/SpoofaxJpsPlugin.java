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

import com.google.inject.*;
import com.intellij.openapi.diagnostic.*;
import org.apache.commons.lang3.concurrent.*;
import org.metaborg.core.*;
import org.metaborg.intellij.*;
import org.metaborg.spoofax.core.*;
import org.metaborg.spoofax.meta.core.*;

import java.util.*;

/**
 * JPS plugin class.
 *
 * The static member of this class are loaded when the JPS plugin is loaded.
 * They must only be used from JPS-specific classes.
 */
public final class SpoofaxJpsPlugin {

    // Static //

    private static final Logger logger = Logger.getInstance(SpoofaxJpsPlugin.class);
    private static final LazyInitializer<SpoofaxJpsPlugin> pluginLazy = new LazyInitializer<SpoofaxJpsPlugin>() {
        @Override
        protected SpoofaxJpsPlugin initialize() throws ConcurrentException {
            return new SpoofaxJpsPlugin();
        }
    };

    /**
     * Gets the injector.
     *
     * @return The current injector.
     */
    public static Injector injector() {
        return spoofaxMeta().injector;
    }

    /**
     * Gets the facade.
     *
     * @return The facade.
     */
    public static Spoofax spoofax() { return plugin().spoofax; }

    /**
     * Gets the meta facade.
     *
     * @return The meta facade.
     */
    public static SpoofaxMeta spoofaxMeta() { return plugin().spoofaxMeta; }

    /**
     * Gets the plugin.
     *
     * @return The plugin.
     */
    public static SpoofaxJpsPlugin plugin() {
        try {
            return pluginLazy.get();
        } catch (final ConcurrentException e) {
            throw new UnhandledException("An unexpected unhandled exception occurred during object creation.", e);
        }
    }

    // Instance //

    private final Spoofax spoofax;
    private final SpoofaxMeta spoofaxMeta;

    public SpoofaxJpsPlugin() {
        logger.debug("Loading Spoofax for JPS plugin using classloader: ",
                this.getClass().getClassLoader().getClass().getName());
        logger.debug("Stack trace: " + Arrays.toString(Thread.currentThread().getStackTrace()));
        try {
            this.spoofax = new Spoofax(new JpsSpoofaxModule());
            this.spoofaxMeta = new SpoofaxMeta(this.spoofax, new JpsSpoofaxMetaModule());
        } catch (final MetaborgException e) {
            throw new RuntimeException(e);
        }
        logger.info("Spoofax for JPS plugin loaded.");
    }
}
