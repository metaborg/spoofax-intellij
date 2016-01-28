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

package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Injector;
import com.intellij.openapi.diagnostic.Logger;
import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.core.Spoofax;
import org.metaborg.spoofax.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.spoofax.meta.core.SpoofaxMeta;

/**
 * JPS plugin class.
 */
public final class SpoofaxJpsPlugin {

//    private static final Supplier<Injector> injector = Suppliers.memoize(() -> Guice.createInjector(new SpoofaxJpsDependencyModule()));

    // Static //

    private static final Logger logger = Logger.getInstance(SpoofaxJpsPlugin.class);
    private static final SpoofaxJpsPlugin plugin;

    static {
        plugin = new SpoofaxJpsPlugin();
        logger.info("Spoofax for JPS plugin loaded.");
    }

    /**
     * Gets the injector.
     *
     * @return The current injector.
     */
    public static Injector injector() {
        return plugin.spoofaxMeta.injector;
    }

    /**
     * Gets the facade.
     *
     * @return The facade.
     */
    public static Spoofax spoofax() { return plugin.spoofax; }

    /**
     * Gets the meta facade.
     *
     * @return The meta facade.
     */
    public static SpoofaxMeta spoofaxMeta() { return plugin.spoofaxMeta; }

    /**
     * Gets the plugin.
     *
     * @return The plugin.
     */
    public static SpoofaxJpsPlugin plugin() { return plugin; }

    // Instance //


    private Spoofax spoofax;
    private SpoofaxMeta spoofaxMeta;

    private SpoofaxJpsPlugin() {
        try {
            this.spoofax = new Spoofax(new SpoofaxJpsModule());
            this.spoofaxMeta = new SpoofaxMeta(this.spoofax, new SpoofaxJpsMetaModule());
        } catch (MetaborgException e) {
            throw new RuntimeException(e);
        }
    }

}
