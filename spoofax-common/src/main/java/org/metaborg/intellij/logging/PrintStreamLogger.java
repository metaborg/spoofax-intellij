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

import org.metaborg.util.log.AbstractLogger;
import org.slf4j.helpers.MessageFormatter;

import jakarta.annotation.Nullable;
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