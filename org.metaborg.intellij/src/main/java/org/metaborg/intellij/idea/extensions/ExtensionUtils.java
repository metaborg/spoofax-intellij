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

package org.metaborg.intellij.idea.extensions;

import com.intellij.openapi.extensions.*;

/**
 * Utility functions for working with IntelliJ IDEA extension points.
 */
public final class ExtensionUtils {

    /**
     * Registers the given extension point value with IntelliJ IDEA.
     *
     * @param value The extension point value to register.
     */
    public static void register(final IExtensionPointValue value) {
        final ExtensionPoint<Object> extensionPoint = Extensions.getRootArea().getExtensionPoint(value.getId());
        extensionPoint.registerExtension(value);
    }

    /**
     * Unregisters the given extension point from IntelliJ IDEA.
     *
     * @param value The extension point value to unregister.
     */
    public static void unregister(final IExtensionPointValue value) {
        final ExtensionPoint<Object> extensionPoint = Extensions.getRootArea().getExtensionPoint(value.getId());
        extensionPoint.unregisterExtension(value);
    }

    private ExtensionUtils() { /* Prevent instantiation. */ }

}
