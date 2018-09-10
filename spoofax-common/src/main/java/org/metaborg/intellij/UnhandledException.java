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

package org.metaborg.intellij;

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
    public UnhandledException(final Throwable exception) {
        this(DefaultMessage, exception);
    }

    /**
     * Initializes a new instance of the {@link UnhandledException} class.
     *
     * @param message   The message to display.
     * @param exception The unhandled exception.
     */
    public UnhandledException(final String message, final Throwable exception) {
        super(message, exception);
    }

}
