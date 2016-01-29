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

package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Factory for the {@link IntelliJLoggerAdapter} class.
 */
public final class IntelliJLoggerFactory implements ILoggerFactory {

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
                    com.intellij.openapi.diagnostic.Logger.getInstance(name);
            final Logger newInstance = new IntelliJLoggerAdapter(intellijLogger);
            final Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
            return oldInstance != null ? oldInstance : newInstance;
        }
    }
}
