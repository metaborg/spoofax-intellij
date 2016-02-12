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

package org.metaborg.intellij;

import com.google.inject.*;

import javax.annotation.*;
import java.util.concurrent.atomic.*;

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
