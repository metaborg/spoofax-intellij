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

package org.metaborg.intellij.languages;

import org.metaborg.core.*;

import javax.annotation.*;

/**
 * Exception thrown when language loading fails.
 */
public class LanguageLoadingFailedException extends MetaborgException {

    private static final String DEFAULT_MESSAGE = "Loading a language failed.";

    /**
     * Initializes a new instance of the {@link LanguageLoadingFailedException} class.
     *
     * @param message The message; or <code>null</code>.
     */
    public LanguageLoadingFailedException(@Nullable final String message) {
        this(message, null);
    }

    /**
     * Initializes a new instance of the {@link LanguageLoadingFailedException} class.
     *
     * @param message The message; or <code>null</code>.
     * @param cause   The cause; or <code>null</code>.
     */
    public LanguageLoadingFailedException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }

    /**
     * Initializes a new instance of the {@link LanguageLoadingFailedException} class.
     *
     * @param cause The cause; or <code>null</code>.
     */
    public LanguageLoadingFailedException(@Nullable final Throwable cause) {
        this(null, cause);
    }

    /**
     * Initializes a new instance of the {@link LanguageLoadingFailedException} class.
     */
    public LanguageLoadingFailedException() {
        this(null, null);
    }
}
