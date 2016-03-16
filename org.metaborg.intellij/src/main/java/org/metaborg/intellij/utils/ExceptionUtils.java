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

package org.metaborg.intellij.utils;

import org.metaborg.util.log.*;
import org.metaborg.util.log.LoggerUtils;

import javax.annotation.*;
import java.lang.reflect.*;

// TODO: Move this to Metaborg Core?
// TODO: Add special static functions/constructors to Metaborg exceptions that uses these functions?

/**
 * Utility functions for working with exceptions.
 */
public final class ExceptionUtils {

    private static final ILogger logger = LoggerUtils.logger(ExceptionUtils.class);

    /**
     * Formats a message.
     *
     * @param msg The message.
     * @param args The arguments.
     * @return The formatted message.
     */
    public static String format(@Nullable final String msg, @Nullable final Object... args) {
        if (msg != null && args != null)
            return logger.format(msg, args);
        else
            return msg;
    }

    /**
     * Creates an exception.
     *
     * @param exceptionClass The class of exception to create.
     * @param msg The exception message.
     * @param t The throwable that caused the exception.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final Class<T> exceptionClass,
                                                    final String msg,
                                                    @Nullable final Throwable t) {

        @Nullable T exception;
        {
            // new T(String, Throwable);
            exception = invokeConstructor(exceptionClass, new Class<?>[]{String.class, Throwable.class}, msg, t);
        }
        if (exception == null) {
            // new T(String);
            exception = invokeConstructor(exceptionClass, new Class<?>[]{String.class}, msg);
        }
        if (exception == null) {
            // new T(Throwable);
            exception = invokeConstructor(exceptionClass, new Class<?>[]{Throwable.class}, t);
        }
        if (exception == null) {
            // new T();
            exception = invokeConstructor(exceptionClass, new Class<?>[]{});
        }
        // In extreme cases `exception` can be null here. Nothing we can do about that unfortunately.
        // Let's assert that's it's not null.
        assert exception != null;

        // Clean the stack trace. Remove clutter from this utility class.
        exception.setStackTrace(removeLoggerUtilsStackTraceElements(exception.getStackTrace()));

        return exception;
    }

    /**
     * Creates an exception.
     *
     * @param exceptionClass The class of exception to create.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final Class<T> exceptionClass) {
        return exception(exceptionClass, "An exception occurred.", (Throwable)null);
    }

    /**
     * Creates an exception.
     *
     * @param exceptionClass The class of exception to create.
     * @param t The throwable that caused the exception.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final Class<T> exceptionClass,
                                                    @Nullable final Throwable t) {
        return exception(exceptionClass, "An exception occurred.", t);
    }

    /**
     * Creates an exception.
     *
     * @param exceptionClass The class of exception to create.
     * @param msg The exception message.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final Class<T> exceptionClass, final String msg) {
        return exception(exceptionClass, msg, (Throwable)null);
    }

    /**
     * Creates an exception and logs it as an error.
     *
     * @param exceptionClass The class of exception to create.
     * @param msg The exception message.
     * @param args The message arguments.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final Class<T> exceptionClass,
                                                    final String msg,
                                                    final Object... args) {
        return exception(exceptionClass, format(msg, args), (Throwable)null);
    }

    /**
     * Creates an exception and logs it as an error.
     *
     * @param exceptionClass The class of exception to create.
     * @param msg The exception message.
     * @param t The throwable that caused the exception.
     * @param args The message arguments.
     * @param <T> The type of exception.
     * @return The exception object.
     */
    public static <T extends Throwable> T exception(final Class<T> exceptionClass,
                                                    final String msg,
                                                    @Nullable final Throwable t,
                                                    final Object... args) {
        return exception(exceptionClass, format(msg, args), t);
    }

    /**
     * Invokes a constructor.
     *
     * @param clazz The class on which to invoke the constructor.
     * @param paramTypes The parameter types.
     * @param args The argument types.
     * @param <T> The type of object to create.
     * @return The resulting object; or <code>null</code> if an exception occurred.
     */
    @Nullable
    private static <T> T invokeConstructor(final Class<T> clazz, final Class<?>[] paramTypes, final Object... args) {
        assert paramTypes.length == args.length;

        @Nullable T obj = null;
        try {
            if (paramTypes.length > 0) {
                final Constructor<T> constructor = clazz.getConstructor(paramTypes);
                obj = constructor.newInstance(args);
            } else {
                obj = clazz.newInstance();
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                InvocationTargetException ex) {
            // Ignore.
        }
        return obj;
    }

    /**
     * Skips the first number of elements in an array.
     *
     * @param arr The input array.
     * @param skip The number of elements to skip from the start.
     * @return The resulting array.
     */
    private static StackTraceElement[] skipStackTraceElements(final StackTraceElement[] arr, final int skip) {
        assert skip <= arr.length;
        final int take = arr.length - skip;
        final StackTraceElement[] newArr = new StackTraceElement[take];
        System.arraycopy(arr, skip, newArr, 0, take);
        return newArr;
    }

    /**
     * Removes all stack trace elements that were introduced by this utility class.
     *
     * @param arr The stack trace array.
     * @return The cleaned stack trace array.
     */
    private static StackTraceElement[] removeLoggerUtilsStackTraceElements(final StackTraceElement[] arr) {
        int skip = 0;
        // Skip all elements up to the elements introduced by this class.
        while (!arr[skip].getClassName().equals(ExceptionUtils.class.getName())) {
            skip++;
        }
        // Skip all the elements introduced by this class.
        while (arr[skip].getClassName().equals(ExceptionUtils.class.getName())) {
            skip++;
        }
        return skipStackTraceElements(arr, skip);
    }

    private ExceptionUtils() { }
}
