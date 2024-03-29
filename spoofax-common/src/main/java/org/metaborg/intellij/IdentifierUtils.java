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

import com.google.inject.Singleton;

import jakarta.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: Add to Metaborg Core?

/**
 * Utility class for identifiers.
 *
 * This class is thread-safe.
 */
@Singleton
public final class IdentifierUtils {

    private static final AtomicInteger counter = new AtomicInteger();

    // To prevent instantiation.
    private IdentifierUtils() {}

    /**
     * Returns a unique identifier.
     *
     * @return The created identifier.
     */
    public static String create() {
        return create("");
    }

    /**
     * Returns a unique identifier.
     *
     * @param prefix The identifier prefix.
     * @return The created identifier.
     */
    public static String create(@Nullable final String prefix) {
        final int value = counter.getAndIncrement();
        return ((prefix != null) ? prefix : "") + value;
    }

}
