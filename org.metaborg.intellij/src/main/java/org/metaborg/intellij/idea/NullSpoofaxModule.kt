package org.metaborg.intellij.idea

import org.metaborg.spoofax.core.SpoofaxModule

/**
 * This module is used to prevent the [Spoofax] class from
 * loading its own [SpoofaxModule].  We do it ourselves
 * in the AESI plugin.
 */
class NullSpoofaxModule: SpoofaxModule() {
    override fun configure() {
        // Null.
    }
}