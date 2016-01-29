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

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;

// TODO: Move this class to Metaborg Core?

/**
 * Formats strings.
 * <p>
 * A pattern string should contain the formatting anchor <code>{}</code> wherever
 * a parameter's value should be inserted.
 * <p>
 * You can use both <code>{</code> and <code>}</code> anywhere in the pattern string
 * as long as they don't form <code>{}</code>. If for whatever reason you need to use
 * <code>{}</code> in your string, escape it by preceding it with a backslash
 * (thus <code>\{}</code>). Note that in Java source code you might have to escape
 * the backslash itself with a backslash.
 */
@Singleton
public final class StringFormatter {

    // To prevent instantiation.
    private StringFormatter() {}

    /**
     * Formats a string.
     *
     * @param pattern   The pattern, with placeholders for the parameters.
     * @param argument0 The first argument, which may be <code>null</code>.
     * @return The formatted message.
     */
    @NotNull
    public static String format(@NotNull final String pattern, @Nullable final Object argument0) {
        Preconditions.checkNotNull(pattern);

        return format(pattern, new Object[]{argument0});
    }

    /**
     * Formats a string.
     *
     * @param pattern   The pattern, with placeholders for the parameters.
     * @param arguments The arguments.
     * @return The formatted message.
     */
    @NotNull
    public static String format(@NotNull final String pattern, @Nullable final Object... arguments) {
        Preconditions.checkNotNull(pattern);

        return MessageFormatter.arrayFormat(pattern, arguments).getMessage();
    }

    /**
     * Formats a string.
     *
     * @param pattern   The pattern, with placeholders for the parameters.
     * @param argument0 The first argument, which may be <code>null</code>.
     * @param argument1 The second argument, which may be <code>null</code>.
     * @return The formatted message.
     */
    @NotNull
    public static String format(
            @NotNull final String pattern,
            @Nullable final Object argument0,
            @Nullable final Object argument1) {
        Preconditions.checkNotNull(pattern);

        return format(pattern, new Object[]{argument0, argument1});
    }


    /**
     * Formats a string.
     *
     * @param pattern   The pattern, with placeholders for the parameters.
     * @param argument0 The first argument, which may be <code>null</code>.
     * @param argument1 The second argument, which may be <code>null</code>.
     * @param argument2 The third argument, which may be <code>null</code>.
     * @return The formatted message.
     */
    @NotNull
    public static String format(
            @NotNull final String pattern,
            @Nullable final Object argument0,
            @Nullable final Object argument1,
            @Nullable final Object argument2) {
        Preconditions.checkNotNull(pattern);

        return format(pattern, new Object[]{argument0, argument1, argument2});
    }
}
