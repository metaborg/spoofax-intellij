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

package org.metaborg.intellij.logging;

import org.metaborg.intellij.utils.ExceptionUtils;
import org.metaborg.util.log.ILogger;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions for working with loggers.
 */
public final class LoggerUtils2 {

    // TODO: Move these to the ILogger interface and implementation?

    /**
     * Logs the exception.
     *
     * @param logger The logger.
     * @param throwable The exception.
     */
    private static <T extends Throwable> T logException(final ILogger logger, final T throwable) {

        logger.error(throwable.getMessage(), throwable);

        return throwable;
    }

    /**
     * Creates an exception and logs it as an error.
     *
     * @param logger The logger to use.
     * @param exceptionClass The class of exception to create.
     * @param msg The exception message.
     * @param t The throwable that caused the exception.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final ILogger logger,
                                                    final Class<T> exceptionClass,
                                                    final String msg,
                                                    @Nullable final Throwable t) {

        return logException(logger, ExceptionUtils.exception(exceptionClass, msg, t));
    }

    /**
     * Creates an exception and logs it as an error.
     *
     * @param logger The logger to use.
     * @param exceptionClass The class of exception to create.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final ILogger logger, final Class<T> exceptionClass) {

        return logException(logger, ExceptionUtils.exception(exceptionClass));
    }

    /**
     * Creates an exception and logs it as an error.
     *
     * @param logger The logger to use.
     * @param exceptionClass The class of exception to create.
     * @param t The throwable that caused the exception.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final ILogger logger,
                                                    final Class<T> exceptionClass,
                                                    @Nullable final Throwable t) {

        return logException(logger, ExceptionUtils.exception(exceptionClass, t));
    }

    /**
     * Creates an exception and logs it as an error.
     *
     * @param logger The logger to use.
     * @param exceptionClass The class of exception to create.
     * @param msg The exception message.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final ILogger logger,
                                                    final Class<T> exceptionClass,
                                                    final String msg) {

        return logException(logger, ExceptionUtils.exception(exceptionClass, msg));
    }

    /**
     * Creates an exception and logs it as an error.
     *
     * @param logger The logger to use.
     * @param exceptionClass The class of exception to create.
     * @param msg The exception message.
     * @param args The message arguments.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final ILogger logger,
                                                    final Class<T> exceptionClass,
                                                    final String msg,
                                                    final Object... args) {

        return logException(logger, ExceptionUtils.exception(exceptionClass, msg, args));
    }

    /**
     * Creates an exception and logs it as an error.
     *
     * @param logger The logger to use.
     * @param exceptionClass The class of exception to create.
     * @param msg The exception message.
     * @param t The throwable that caused the exception.
     * @param args The message arguments.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final ILogger logger,
                                                    final Class<T> exceptionClass,
                                                    final String msg,
                                                    @Nullable final Throwable t,
                                                    final Object... args) {

        return logException(logger, ExceptionUtils.exception(exceptionClass, msg, t, args));
    }

    /**
     * Injects the specified logger into the specified object.
     *
     * Fields with the @InjectLogger annotation will be injected.
     * If injection fails, the field is not injected.
     *
     * Use this function only when Guice is not available to inject the logger
     * (e.g. in unit tests).
     *
     * @param obj The object instance to inject into.
     * @param logger The logger to inject.
     * @return The object instance.
     */
    public static <T> T injectLogger(final T obj, final ILogger logger) {
        @Nullable Class<?> clazz = obj.getClass();
        while (clazz != null) {
            for (final Field loggerField : getLoggerFields(clazz)) {
                try {
                    loggerField.setAccessible(true);
                    loggerField.set(obj, logger);
                } catch (final Exception ex) {
                    // Ignore any exceptions.
                }
            }
            // Set the logger in the super class.
            clazz = clazz.getSuperclass();
        }
        return obj;
    }

    /**
     * Gets the fields annotated with @InjectLogger declared in the specified class.
     *
     * @param clazz The class to explore.
     * @return The fields with the annotation; or an empty list if not found.
     */
    private static List<Field> getLoggerFields(final Class<?> clazz) {
        final List<Field> loggerFields = new ArrayList<>();
        for (final Field field : clazz.getDeclaredFields()) {
            final InjectLogger[] annotations = field.getAnnotationsByType(InjectLogger.class);
            if (annotations.length != 0) {
                loggerFields.add(field);
            }
        }
        return loggerFields;
    }

    private LoggerUtils2() { }
}
