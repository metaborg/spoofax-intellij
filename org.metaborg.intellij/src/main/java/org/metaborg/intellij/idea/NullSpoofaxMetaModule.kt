package org.metaborg.intellij.idea

import org.metaborg.spoofax.core.SpoofaxModule
import org.metaborg.spoofax.meta.core.SpoofaxMetaModule

class NullSpoofaxMetaModule : SpoofaxMetaModule() {
    override fun configure() {
        // Null.
    }
}