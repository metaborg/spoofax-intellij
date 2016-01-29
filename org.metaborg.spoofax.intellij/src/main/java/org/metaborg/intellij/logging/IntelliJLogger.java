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

package org.metaborg.intellij.logging;

import com.intellij.openapi.diagnostic.Logger;
import org.metaborg.util.log.AbstractLogger;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;

// TODO: Bind this logger to Metaborg's ILogger instead of the SLF4J adapter.
/**
 * Logs using IntelliJ's built-in logging framework.
 */
public final class IntelliJLogger extends AbstractLogger {

    private final Logger logger;

    /**
     * Initializes a new instance of the {@link IntelliJLogger} class.
     *
     * @param logger The IntelliJ logger.
     */
    public IntelliJLogger(final Logger logger) {
        super();
        this.logger = logger;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(final String msg, @Nullable final Throwable cause) {
        debug(msg, cause);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean traceEnabled() {
        return debugEnabled();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(final String msg, @Nullable final Throwable cause) {
        this.logger.debug(msg, cause);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean debugEnabled() {
        return this.logger.isDebugEnabled();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(final String msg, @Nullable final Throwable cause) {
        this.logger.info(msg, cause);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean infoEnabled() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(final String msg, @Nullable final Throwable cause) {
        this.logger.warn(msg, cause);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean warnEnabled() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(final String msg, @Nullable final Throwable cause) {
        this.logger.error(msg, cause);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean errorEnabled() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String format(final String msg, final Object... args) {
        return MessageFormatter.arrayFormat(msg, args).getMessage();
    }
}
