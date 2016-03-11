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
