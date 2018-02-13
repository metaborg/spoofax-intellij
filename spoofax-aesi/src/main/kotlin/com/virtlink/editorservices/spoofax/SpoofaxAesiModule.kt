package com.virtlink.editorservices.spoofax

import com.google.inject.AbstractModule

/**
 * Spoofax for AESI dependency injection bindings.
 */
class SpoofaxAesiModule: AbstractModule() {

    override fun configure() {
        install(AesiModule())
        install(SpoofaxCoreModule())
        install(SpoofaxCoreMetaModule())
    }

}