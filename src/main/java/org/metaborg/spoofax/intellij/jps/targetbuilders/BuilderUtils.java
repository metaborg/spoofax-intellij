package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.metaborg.core.messages.IMessage;

public final class BuilderUtils {

    /**
     * Formats a progress message.
     *
     * @param done The percentage progress.
     * @param message The message string to format.
     * @param args The message string arguments.
     * @return The formatted progress message.
     */
    @NotNull
    public static ProgressMessage formatProgress(float done, @NotNull String message, Object... args) {
        final String msgString = String.format(message, args);
        return new ProgressMessage(msgString, done);
    }

    /**
     * Formats a compiler message.
     *
     * @param builderName The name of the builder.
     * @param kind The kind of message.
     * @param message The message string to format.
     * @param args The message string arguments.
     * @return The formatted message.
     */
    @NotNull
    public static CompilerMessage formatMessage(@NotNull String builderName, @NotNull BuildMessage.Kind kind, @NotNull String message, Object... args) {
        final String msgString = String.format(message, args);
        return new CompilerMessage(builderName, kind, msgString);
    }

    /**
     * Formats a compiler message.
     *
     * @param builderName The name of the builder.
     * @param message The message to format.
     * @return The formatted message.
     */
    @NotNull
    public static CompilerMessage formatMessage(@NotNull String builderName, @NotNull IMessage message)
    {
        final BuildMessage.Kind kind;
        switch (message.severity())
        {
            case NOTE: kind = BuildMessage.Kind.INFO; break;
            case WARNING: kind = BuildMessage.Kind.WARNING; break;
            case ERROR: kind = BuildMessage.Kind.ERROR; break;
            default:
                throw new UnsupportedOperationException();
        }

        String msgString = message.message();
        if (message.exception() != null) {
            msgString += "\n" + message.exception().toString();
        }

        String sourcePath = null;
        long problemBeginOffset = -1L;
        long problemEndOffset = -1L;
        long problemLocationOffset = -1L;
        long locationLine = -1L;
        long locationColumn = -1L;

        if (message.source() != null && message.region() != null && !(message.region().startOffset() == 0 && message.region().endOffset() == 0 && message.region().startRow() == 0 && message.region().endRow() == 0 && message.region().startColumn() == 0 && message.region().endColumn() == 0)) {
            sourcePath = message.source().getName().getPath();
            problemBeginOffset = message.region().startOffset();
            problemEndOffset = message.region().endOffset();
            problemLocationOffset = message.region().startOffset();
            locationLine = message.region().startRow() + 1;
            locationColumn = message.region().startColumn() + 1;
        }

        return new CompilerMessage(builderName, kind, msgString, sourcePath, problemBeginOffset, problemEndOffset, problemLocationOffset, locationLine, locationColumn);
    }
}
