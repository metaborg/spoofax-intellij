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

package org.metaborg.spoofax.intellij;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.helpers.MessageFormatter;

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
     * @param argument0 The first argument.
     * @return The formatted message.
     */
    @NotNull
    public static final String format(@NotNull final String pattern, @NotNull final Object argument0) {
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
    public static final String format(@NotNull final String pattern, @NotNull final Object... arguments) {
        return MessageFormatter.arrayFormat(pattern, arguments).getMessage();
    }

    /**
     * Formats a string.
     *
     * @param pattern   The pattern, with placeholders for the parameters.
     * @param argument0 The first argument.
     * @param argument1 The second argument.
     * @return The formatted message.
     */
    @NotNull
    public static final String format(@NotNull final String pattern,
                                      @NotNull final Object argument0,
                                      @NotNull final Object argument1) {
        return format(pattern, new Object[]{argument0, argument1});
    }


    /**
     * Formats a string.
     *
     * @param pattern   The pattern, with placeholders for the parameters.
     * @param argument0 The first argument.
     * @param argument1 The second argument.
     * @param argument2 The third argument.
     * @return The formatted message.
     */
    @NotNull
    public static final String format(@NotNull final String pattern,
                                      @NotNull final Object argument0,
                                      @NotNull final Object argument1,
                                      @NotNull final Object argument2) {
        return format(pattern, new Object[]{argument0, argument1, argument2});
    }
}
