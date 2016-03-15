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

package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Factory for the {@link IntelliJLoggerAdapter} class.
 */
/* package private */ final class IntelliJLoggerFactory implements ILoggerFactory {

    private final ConcurrentMap<String, Logger> loggerMap;

    /**
     * Initializes a new instance of the {@link IntelliJLoggerFactory} class.
     */
    public IntelliJLoggerFactory() {
        this.loggerMap = new ConcurrentHashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger(final String name) {
        final Logger slf4jLogger = this.loggerMap.get(name);
        if (slf4jLogger != null) {
            // Return the logger from the map.
            return slf4jLogger;
        } else {
            // Put the new logger in the map and return it,
            // unless another logger suddenly appeared in the map, which we then return instead.
            final com.intellij.openapi.diagnostic.Logger intellijLogger =
                    com.intellij.openapi.diagnostic.Logger.getInstance("#" + name);
            final Logger newInstance = new IntelliJLoggerAdapter(intellijLogger);
            final Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
            return oldInstance != null ? oldInstance : newInstance;
        }
    }
}
