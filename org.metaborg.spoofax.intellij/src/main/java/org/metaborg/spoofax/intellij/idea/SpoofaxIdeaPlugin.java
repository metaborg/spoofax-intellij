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

package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.core.Spoofax;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.metaborg.spoofax.meta.core.SpoofaxMeta;

public class SpoofaxIdeaPlugin {
//    @NotNull
//    protected static final Supplier<Injector> injector = Suppliers.memoize(() -> Guice.createInjector(new SpoofaxIdeaDependencyModule()));

    // Static //

    private static final Logger logger = Logger.getInstance(SpoofaxIdeaPlugin.class);
    private static SpoofaxIdeaPlugin plugin;

    static {
        plugin = new SpoofaxIdeaPlugin();
        logger.info("Spoofax for IDEA plugin loaded.");
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
    public static SpoofaxIdeaPlugin plugin() { return plugin; }

    // Instance //

    private Spoofax spoofax;
    private SpoofaxMeta spoofaxMeta;

    private SpoofaxIdeaPlugin() {
        try {
            this.spoofax = new Spoofax(new SpoofaxIdeaModule());
            this.spoofaxMeta = new SpoofaxMeta(this.spoofax, new SpoofaxIdeaMetaModule());
        } catch (MetaborgException e) {
            throw new RuntimeException(e);
        }
    }

}
