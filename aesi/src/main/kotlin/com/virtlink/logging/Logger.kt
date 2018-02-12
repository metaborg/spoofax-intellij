package com.virtlink.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.helpers.MessageFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Returns a logger for the current class, lazily.
 *
 * @return The logger.
 */
fun <T: Any> T.logger(): Lazy<Logger> = lazy { logger(this.javaClass) }

///**
// * Returns a logger for the current class.
// *
// * @return The logger.
// */
//fun <T: Any> T.logger(): Logger = logger(this.javaClass)

/**
 * Returns a logger for the specified Java class.
 *
 * @param forClass The Java class.
 * @return The logger.
 */
fun <T: Any> logger(forClass: Class<T>): Logger {
    return LoggerFactory.getLogger(getCompanionClass(forClass).name)
}

/**
 * Returns a logger for the specified Kotlin class.
 *
 * @param forClass The Kotlin class.
 * @return The logger.
 */
fun <T: Any> logger(forClass: KClass<T>): Logger = logger(forClass.java)

/**
 * Returns the companion class of the specified Java class.
 *
 * @param ofClass The Java class.
 * @return The companion Java class, or the original class when it has no companion class.
 */
private fun <T: Any> getCompanionClass(ofClass: Class<T>): Class<*> {
    return if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) {
        ofClass.enclosingClass
    } else {
        ofClass
    }
}

/**
 * Formats the specified message and arguments
 * according to the logger formatting convention.
 *
 * @param message The message to format, with `{}` as the placeholders.
 * @param arg1 The first argument.
 * @return The formatted message.
 */
fun Logger.format(message: String, arg1: Any): String
        = MessageFormatter.format(message, arg1).message

/**
 * Formats the specified message and arguments
 * according to the logger formatting convention.
 *
 * @param message The message to format, with `{}` as the placeholders.
 * @param arg1 The first argument.
 * @param arg2 The second argument.
 * @return The formatted message.
 */
fun Logger.format(message: String, arg1: Any, arg2: Any): String
        = MessageFormatter.format(message, arg1, arg2).message

/**
 * Formats the specified message and arguments
 * according to the logger formatting convention.
 *
 * @param message The message to format, with `{}` as the placeholders.
 * @param args The arguments.
 * @return The formatted message.
 */
fun Logger.format(message: String, vararg args: Any): String
        = MessageFormatter.arrayFormat(message, args).message

fun Logger.isLogEnabled(logLevel: LogLevel): Boolean {
    return when (logLevel) {
        LogLevel.Trace -> this.isTraceEnabled
        LogLevel.Debug -> this.isDebugEnabled
        LogLevel.Info -> this.isInfoEnabled
        LogLevel.Warning -> this.isWarnEnabled
        LogLevel.Error -> this.isErrorEnabled
    }
}