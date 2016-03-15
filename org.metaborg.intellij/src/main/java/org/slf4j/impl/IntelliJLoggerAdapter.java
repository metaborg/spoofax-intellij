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

package org.slf4j.impl;

import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import java.io.Serializable;

/**
 * Adapter for the IntelliJ logger.
 */
/* package private */ final class IntelliJLoggerAdapter extends MarkerIgnoringBase implements LocationAwareLogger, Serializable {

    private final com.intellij.openapi.diagnostic.Logger logger;

    /**
     * Initializes a new instance of the {@link IntelliJLoggerAdapter} class.
     * <p>
     * This class is only created by the {@link IntelliJLoggerFactory}.
     *
     * @param logger The IntelliJ logger to use.
     */
    /* package private */ IntelliJLoggerAdapter(final com.intellij.openapi.diagnostic.Logger logger) {
        super();
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTraceEnabled() {
        return isDebugEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(final String msg) {
        debug(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(final String format, final Object arg) {
        debug(format, arg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(final String format, final Object arg1, final Object arg2) {
        debug(format, arg1, arg2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(final String format, final Object... arguments) {
        debug(format, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(final String msg, final Throwable throwable) {
        debug(msg, throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String msg) {
        this.logger.debug(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String format, final Object arg) {
        if (isDebugEnabled()) {
            final FormattingTuple formatted = MessageFormatter.format(format, arg);
            this.logger.debug(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String format, final Object arg1, final Object arg2) {
        if (isDebugEnabled()) {
            final FormattingTuple formatted = MessageFormatter.format(format, arg1, arg2);
            this.logger.debug(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String format, final Object... arguments) {
        if (isDebugEnabled()) {
            final FormattingTuple formatted = MessageFormatter.arrayFormat(format, arguments);
            this.logger.debug(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String msg, final Throwable throwable) {
        this.logger.debug(msg, throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String msg) {
        this.logger.info(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String format, final Object arg) {
        if (isInfoEnabled()) {
            final FormattingTuple formatted = MessageFormatter.format(format, arg);
            this.logger.info(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String format, final Object arg1, final Object arg2) {
        if (isInfoEnabled()) {
            final FormattingTuple formatted = MessageFormatter.format(format, arg1, arg2);
            this.logger.info(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String format, final Object... arguments) {
        if (isInfoEnabled()) {
            final FormattingTuple formatted = MessageFormatter.arrayFormat(format, arguments);
            this.logger.info(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String msg, final Throwable throwable) {
        this.logger.info(msg, throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final String msg) {
        this.logger.warn(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final String format, final Object arg) {
        if (isWarnEnabled()) {
            final FormattingTuple formatted = MessageFormatter.format(format, arg);
            this.logger.warn(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {
        if (isWarnEnabled()) {
            final FormattingTuple formatted = MessageFormatter.format(format, arg1, arg2);
            this.logger.warn(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final String format, final Object... arguments) {
        if (isWarnEnabled()) {
            final FormattingTuple formatted = MessageFormatter.arrayFormat(format, arguments);
            this.logger.warn(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final String msg, final Throwable throwable) {
        this.logger.warn(msg, throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String msg) {
        this.logger.error(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String format, final Object arg) {
        if (isErrorEnabled()) {
            final FormattingTuple formatted = MessageFormatter.format(format, arg);
            this.logger.error(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String format, final Object arg1, final Object arg2) {
        if (isErrorEnabled()) {
            final FormattingTuple formatted = MessageFormatter.format(format, arg1, arg2);
            this.logger.error(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String format, final Object... arguments) {
        if (isErrorEnabled()) {
            final FormattingTuple formatted = MessageFormatter.arrayFormat(format, arguments);
            this.logger.error(formatted.getMessage(), formatted.getThrowable());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String msg, final Throwable throwable) {
        this.logger.error(msg, throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(
            final Marker marker,
            final String callerFQCN,
            final int level,
            final String msg,
            final Object[] arguments,
            final Throwable throwable) {
        final Object[] newArguments = getArgumentsWithThrowable(arguments, throwable);
        switch (level) {
            case LocationAwareLogger.TRACE_INT:
                trace(msg, newArguments);
                break;
            case LocationAwareLogger.DEBUG_INT:
                debug(msg, newArguments);
                break;
            case LocationAwareLogger.INFO_INT:
                info(msg, newArguments);
                break;
            case LocationAwareLogger.WARN_INT:
                warn(msg, newArguments);
                break;
            case LocationAwareLogger.ERROR_INT:
                error(msg, newArguments);
                break;
            default:
                throw new IllegalStateException("Level number " + level + " is not recognized.");
        }
    }

    /**
     * Gets an argument list with the specified throwable appended to it.
     *
     * @param arguments The argument list.
     * @param throwable The throwable to append; or <code>null</code>.
     * @return The new argument list, with the throwable appended to it.
     */
    private static Object[] getArgumentsWithThrowable(
            final Object[] arguments,
            final Throwable throwable) {
        if (throwable == null)
            return arguments;
        final Object[] newArguments = new Object[arguments.length + 1];
        System.arraycopy(arguments, 0, newArguments, 0, arguments.length);
        newArguments[newArguments.length - 1] = throwable;
        return newArguments;
    }
}
