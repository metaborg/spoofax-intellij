package org.metaborg.spoofax.intellij.logging;

import com.google.inject.MembersInjector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Injects loggers.
 *
 * @param <T> The type of object.
 */
public final class Slf4JMembersInjector<T> implements MembersInjector<T> {

    @NotNull
    private final Field field;
    @NotNull
    private final Logger logger;

    /**
     * Initializes a new instance of the {@link Slf4JMembersInjector} class.
     *
     * @param field The field to inject.
     */
    public Slf4JMembersInjector(@NotNull Field field) {
        this.field = field;
        this.logger = createLogger(field.getDeclaringClass());
        this.field.setAccessible(true);
    }

    /**
     * Creates the logger.
     *
     * @param clazz The class.
     * @return The created logger instance.
     */
    @NotNull
    private static Logger createLogger(@NotNull Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    @Override
    public void injectMembers(@NotNull T t) {
        try {
            this.field.set(t, logger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
