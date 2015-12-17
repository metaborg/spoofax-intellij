/*
 * Copyright © 2015-2015
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

package org.metaborg.jps;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * JPS plugin class.
 */
public final class JpsPlugin {

    private static final Supplier<Injector> injector = Suppliers.memoize(() -> Guice.createInjector(new SpoofaxJpsDependencyModule()));
    private static final Logger logger = Logger.getInstance(JpsPlugin.class);

    static {
        logger.info("Spoofax for JPS plugin loaded.");
    }

    // Prevent instantiation.
    private JpsPlugin() {
    }

    /**
     * Gets the injector.
     *
     * @return The current injector.
     */
    public static Injector injector() {
        return injector.get();
    }

}
