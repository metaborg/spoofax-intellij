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

package org.metaborg.spoofax.intellij.jps.targetbuilders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.StringFormatter;
import org.spoofax.interpreter.terms.IStrategoList;
import org.strategoxt.lang.StrategoErrorExit;

/**
 * Builder utility functions.
 */
public final class BuilderUtils {

    /**
     * Formats a progress message.
     *
     * @param done    The percentage progress.
     * @param message The message string to format.
     * @param args    The message string arguments.
     * @return The formatted progress message.
     */
    @NotNull
    public final static ProgressMessage formatProgress(final float done,
                                                       @NotNull final String message,
                                                       final Object... args) {
        final String msgString = StringFormatter.format(message, args);
        return new ProgressMessage(msgString, done);
    }

    /**
     * Formats a compiler message.
     *
     * @param builderName The name of the builder.
     * @param kind        The kind of message.
     * @param message     The message string to format.
     * @param args        The message string arguments.
     * @return The formatted message.
     */
    @NotNull
    public final static CompilerMessage formatMessage(@NotNull final String builderName,
                                                      @NotNull final BuildMessage.Kind kind,
                                                      @NotNull final String message,
                                                      final Object... args) {
        final String msgString = StringFormatter.format(message, args);
        return new CompilerMessage(builderName, kind, msgString);
    }

    /**
     * Formats a compiler message.
     *
     * @param builderName The name of the builder.
     * @param message     The message to format.
     * @return The formatted message.
     */
    @NotNull
    public final static CompilerMessage formatMessage(@NotNull final String builderName,
                                                      @NotNull final IMessage message) {
        final BuildMessage.Kind kind;
        switch (message.severity()) {
            case NOTE:
                kind = BuildMessage.Kind.INFO;
                break;
            case WARNING:
                kind = BuildMessage.Kind.WARNING;
                break;
            case ERROR:
                kind = BuildMessage.Kind.ERROR;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        String msgString = message.message();
        Throwable exception = message.exception();
        while (exception != null) {
            msgString += "\n" + exception.getMessage();
            if (exception instanceof StrategoErrorExit) {
                StrategoErrorExit strategoException = (StrategoErrorExit) exception;
                IStrategoList trace = strategoException.getTrace();
                while (!trace.isEmpty()) {
                    msgString += "\n\t" + trace.head();
                    trace = trace.tail();
                }
            }
            exception = exception.getCause();
        }

        String sourcePath = message.source().getName().getPath();
        long problemBeginOffset = -1L;
        long problemEndOffset = -1L;
        long problemLocationOffset = -1L;
        long locationLine = -1L;
        long locationColumn = -1L;

        if (message.region() != null && !(message.region().startOffset() == 0 && message.region().endOffset() == 0 && message.region().startRow() == 0 && message.region().endRow() == 0 && message.region().startColumn() == 0 && message.region().endColumn() == 0)) {
            problemBeginOffset = message.region().startOffset();
            problemEndOffset = message.region().endOffset();
            problemLocationOffset = message.region().startOffset();
            locationLine = message.region().startRow() + 1;
            locationColumn = message.region().startColumn() + 1;
        }

        return new CompilerMessage(builderName,
                                   kind,
                                   msgString,
                                   sourcePath,
                                   problemBeginOffset,
                                   problemEndOffset,
                                   problemLocationOffset,
                                   locationLine,
                                   locationColumn);
    }
}
