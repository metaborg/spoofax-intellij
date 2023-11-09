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

package org.metaborg.intellij.languages;

import org.metaborg.core.*;
import org.metaborg.intellij.utils.ExceptionUtils;

import jakarta.annotation.Nullable;

/**
 * Exception thrown when language loading fails.
 */
public class LanguageLoadingFailedException extends MetaborgException {

    private static final String DEFAULT_MESSAGE = "Loading a language failed.";

    /**
     * Initializes a new instance of the {@link LanguageLoadingFailedException} class.
     *
     * @param message The message; or <code>null</code>.
     * @param cause   The cause; or <code>null</code>.
     * @param args    The exception arguments.
     */
    public LanguageLoadingFailedException(@Nullable final String message,
                                          @Nullable final Throwable cause,
                                          final Object... args) {
        this(ExceptionUtils.format(message, args), cause);
    }

    /**
     * Initializes a new instance of the {@link LanguageLoadingFailedException} class.
     *
     * @param message The message; or <code>null</code>.
     * @param cause   The cause; or <code>null</code>.
     */
    public LanguageLoadingFailedException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message != null ? message : DEFAULT_MESSAGE, cause);
    }

    /**
     * Initializes a new instance of the {@link LanguageLoadingFailedException} class.
     *
     * @param message The message; or <code>null</code>.
     * @param args    The exception arguments.
     */
    public LanguageLoadingFailedException(@Nullable final String message,
                                          final Object... args) {
        this(ExceptionUtils.format(message, args));
    }

    /**
     * Initializes a new instance of the {@link LanguageLoadingFailedException} class.
     *
     * @param message The message; or <code>null</code>.
     */
    public LanguageLoadingFailedException(@Nullable final String message) {
        this(message, (Throwable)null);
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
        this(null, (Throwable)null);
    }
}
