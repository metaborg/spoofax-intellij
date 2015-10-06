package org.metaborg.spoofax.intellij;

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
public final class StringFormatter {

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
     * @return The formatted message.
     */
    @NotNull
    public static final String format(@NotNull final String pattern, @NotNull final Object argument0) {
        return format(pattern, argument0);
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
        return format(pattern, argument0, argument1);
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
        return format(pattern, argument0, argument1, argument2);
    }
}
