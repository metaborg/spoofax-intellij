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
import com.intellij.openapi.diagnostic.*;
import org.apache.commons.lang3.concurrent.*;
import org.metaborg.intellij.*;
import org.metaborg.spoofax.core.*;
import org.metaborg.spoofax.meta.core.*;

public interface SpoofaxJpsPlugin {

    /**
     * Gets the injector.
     *
     * @return The current injector.
     */
    Injector injector();

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

    /**
     * Cleans up the plugin.
     */
    void close();

    // Static //

    LazyInitializer<SpoofaxJpsPlugin> pluginLazy = new LazyInitializer<SpoofaxJpsPlugin>() {
        @Override
        protected SpoofaxJpsPlugin initialize() throws ConcurrentException {
            return new SpoofaxJpsPluginImpl();
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