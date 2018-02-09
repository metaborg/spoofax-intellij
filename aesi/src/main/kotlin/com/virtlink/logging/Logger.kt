package com.virtlink.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Creates a logger for the current class.
 *
 * Usage:
 *
 *     val LOG by logger()
 *
 */
fun <T : Any> T.logger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this.javaClass.name) }
}


fun Logger.isLogEnabled(logLevel: LogLevel): Boolean {
    return when (logLevel) {
        LogLevel.Trace -> this.isTraceEnabled
        LogLevel.Debug -> this.isDebugEnabled
        LogLevel.Info -> this.isInfoEnabled
        LogLevel.Warning -> this.isWarnEnabled
        LogLevel.Error -> this.isErrorEnabled
    }
}