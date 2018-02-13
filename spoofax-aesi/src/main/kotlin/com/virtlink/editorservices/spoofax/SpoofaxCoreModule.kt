package com.virtlink.editorservices.spoofax

import org.metaborg.spoofax.core.SpoofaxModule

/**
 * Binds the Spoofax Core interfaces.
 */
internal class SpoofaxCoreModule: SpoofaxModule() {

    override fun bindEditor() {
        // We don't want these bindings.
    }

    override fun bindResource() {
        // We don't want these bindings.
    }

    override fun bindProject() {
        // We don't want these bindings.
    }
}