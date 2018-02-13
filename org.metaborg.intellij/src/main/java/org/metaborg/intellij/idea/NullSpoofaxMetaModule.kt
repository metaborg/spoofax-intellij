package org.metaborg.intellij.idea

import org.metaborg.spoofax.core.SpoofaxModule
import org.metaborg.spoofax.meta.core.SpoofaxMetaModule

/**
 * This module is used to prevent the [SpoofaxMeta] class from
 * loading its own [SpoofaxMetaModule].  We do it ourselves
 * in the AESI plugin.
 */
class NullSpoofaxMetaModule : SpoofaxMetaModule() {
    override fun configure() {
        // Null.
    }
}