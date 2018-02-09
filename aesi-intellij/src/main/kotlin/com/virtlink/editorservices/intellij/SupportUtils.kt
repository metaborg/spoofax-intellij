package com.virtlink.editorservices.intellij

/**
 * Determines whether a particular class is present.
 *
 * When using optional dependencies, a class may not be present.
 *
 * @param
 */
fun isPresent(className: String): Boolean {
    try {
        Class.forName(className)
        return true
    } catch (ex: Throwable) {
        // Class or one of its dependencies is not present...
        return false
    }
}