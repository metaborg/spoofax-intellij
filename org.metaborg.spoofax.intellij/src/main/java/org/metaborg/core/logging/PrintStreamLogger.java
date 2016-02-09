/*
 * Copyright © 2015-2016
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

package org.metaborg.core.logging;

import org.metaborg.util.log.AbstractLogger;
import org.metaborg.util.log.ILogger;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;
import java.io.PrintStream;

// TODO: Move to Metaborg Core.
/**
 * Logger that logs to the console.
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public final class PrintStreamLogger extends AbstractLogger {

    @Nullable private PrintStream traceStream = null;
    @Nullable private PrintStream debugStream = System.out;
    @Nullable private PrintStream infoStream = System.out;
    @Nullable private PrintStream warnStream = System.err;
    @Nullable private PrintStream errorStream = System.err;

    /**
     * Initializes a new instance of the {@link PrintStreamLogger} class.
     */
    public PrintStreamLogger() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(final String s, @Nullable final Throwable throwable) {
        if (traceEnabled() && this.traceStream != null) {
            this.traceStream.println("TRACE: " + s);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean traceEnabled() {
        return this.traceStream != null;
    }

    /**
     * Sets the stream to which trace logs are written.
     *
     * @param stream The stream to which to log; or <code>null</code> to disable.
     */
    public void setTraceStream(@Nullable final PrintStream stream) {
        this.traceStream = stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String s, @Nullable final Throwable throwable) {
        if (debugEnabled() && this.debugStream != null) {
            this.debugStream.println("DEBUG: " + s);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean debugEnabled() {
        return this.debugStream != null;
    }

    /**
     * Sets the stream to which debug logs are written.
     *
     * @param stream The stream to which to log; or <code>null</code> to disable.
     */
    public void setDebugStream(@Nullable final PrintStream stream) {
        this.debugStream = stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String s, @Nullable final Throwable throwable) {
        if (infoEnabled() && this.infoStream != null) {
            this.infoStream.println("INFO : " + s);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean infoEnabled() {
        return this.infoStream != null;
    }

    /**
     * Sets the stream to which info logs are written.
     *
     * @param stream The stream to which to log; or <code>null</code> to disable.
     */
    public void setInfoStream(@Nullable final PrintStream stream) {
        this.infoStream = stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final String s, @Nullable final Throwable throwable) {
        if (warnEnabled() && this.warnStream != null) {
            this.warnStream.println("WARNG: " + s);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean warnEnabled() {
        return this.warnStream != null;
    }

    /**
     * Sets the stream to which warning logs are written.
     *
     * @param stream The stream to which to log; or <code>null</code> to disable.
     */
    public void setWarnStream(@Nullable final PrintStream stream) {
        this.warnStream = stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String s, @Nullable final Throwable throwable) {
        if (errorEnabled() && this.errorStream != null) {
            this.errorStream.println("ERROR: " + s);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean errorEnabled() {
        return this.errorStream != null;
    }

    /**
     * Sets the stream to which error logs are written.
     *
     * @param stream The stream to which to log; or <code>null</code> to disable.
     */
    public void setErrorStream(@Nullable final PrintStream stream) {
        this.errorStream = stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(final String s, final Object... objects) {
        return MessageFormatter.arrayFormat(s, objects).getMessage();
    }
}