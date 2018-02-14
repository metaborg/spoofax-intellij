package org.metaborg.intellij.idea

import com.google.inject.Injector
import org.metaborg.intellij.idea.files.SpoofaxLanguageArtifactFileType

/**
 * Gets an instance from the injector.
 *
 * This is used when normal constructor injection is not possible,
 * usually due to the class being loaded outside of the injector's control.
 */
inline fun <reified T> Injector.getInstance(): T {
    return this.getInstance(T::class.java)
}