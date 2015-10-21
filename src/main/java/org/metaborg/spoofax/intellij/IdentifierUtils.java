package org.metaborg.spoofax.intellij;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

// TODO: Add to Metaborg Core?

/**
 * Utility class for identifiers.
 */
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
    public static String create(@NotNull String prefix) {
        int value = counter.getAndIncrement();
        return prefix + value;
    }

}
