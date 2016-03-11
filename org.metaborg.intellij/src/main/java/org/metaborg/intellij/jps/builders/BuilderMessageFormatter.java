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

package org.metaborg.intellij.jps.builders;

import com.google.inject.*;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.incremental.messages.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;
import org.spoofax.interpreter.terms.*;
import org.strategoxt.lang.*;

import javax.annotation.*;

/**
 * Builder message formatting functions.
 */
public final class BuilderMessageFormatter {

    @InjectLogger
    private ILogger logger;

    @Inject
    public BuilderMessageFormatter() {

    }

    /**
     * Formats a progress message.
     *
     * @param done    The percentage progress.
     * @param message The message string to format.
     * @param args    The message string arguments.
     * @return The formatted progress message.
     */
    public ProgressMessage formatProgress(
            final float done,
            final String message,
            final Object... args) {
        final String msgString = this.logger.format(message, args);
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
    public CompilerMessage formatMessage(
            final String builderName,
            final BuildMessage.Kind kind,
            final String message,
            final Object... args) {
        final String msgString = this.logger.format(message, args);
        return new CompilerMessage(builderName, kind, msgString);
    }

    /**
     * Formats a compiler message.
     *
     * @param builderName The name of the builder.
     * @param message     The message to format.
     * @return The formatted message.
     */
    public CompilerMessage formatMessage(
            final String builderName,
            final IMessage message) {
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
        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        @Nullable Throwable exception = message.exception();
        while (exception != null) {
            msgString += "\n" + exception.getMessage();
            if (exception instanceof StrategoErrorExit) {
                final StrategoErrorExit strategoException = (StrategoErrorExit)exception;
                IStrategoList trace = strategoException.getTrace();
                while (!trace.isEmpty()) {
                    msgString += "\n\t" + trace.head();
                    trace = trace.tail();
                }
            }
            exception = exception.getCause();
        }

        final FileObject source = message.source();
        final String sourcePath;
        if(source != null) {
            sourcePath = source.getName().getPath();
        } else {
            sourcePath = null;
        }
        long problemBeginOffset = -1L;
        long problemEndOffset = -1L;
        long problemLocationOffset = -1L;
        long locationLine = -1L;
        long locationColumn = -1L;

        @Nullable final ISourceRegion region = message.region();
        if (!isEmptyRegion(region)) {
            problemBeginOffset = region.startOffset();
            problemEndOffset = region.endOffset();
            problemLocationOffset = region.startOffset();
            locationLine = region.startRow() + 1;
            locationColumn = region.startColumn() + 1;
        }

        return new CompilerMessage(
                builderName,
                kind,
                msgString,
                sourcePath,
                problemBeginOffset,
                problemEndOffset,
                problemLocationOffset,
                locationLine,
                locationColumn
        );
    }

    private static boolean isEmptyRegion(@Nullable final ISourceRegion region) {
        return region == null
                || region.startOffset() == 0
                || region.endOffset() == 0
                || region.startRow() == 0
                || region.endRow() == 0
                || region.startColumn() == 0
                || region.endColumn() == 0;
    }
}
