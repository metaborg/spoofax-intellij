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

package org.metaborg.core;

/**
 * An unhandled exception.
 */
public class UnhandledException extends RuntimeException {

    // UnhandledException had been removed from Apache Commons Lang,
    // which is why we define our own.

    private static final String DefaultMessage = "Unhandled exception.";

    /**
     * Initializes a new instance of the {@link UnhandledException} class.
     *
     * @param exception The unhandled exception.
     */
    public UnhandledException(Throwable exception) {
        this(DefaultMessage, exception);
    }

    /**
     * Initializes a new instance of the {@link UnhandledException} class.
     *
     * @param message The message to display.
     * @param exception The unhandled exception.
     */
    public UnhandledException(String message, Throwable exception) {
        super(message, exception);
    }

}
