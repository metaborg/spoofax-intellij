package com.virtlink.editorservices.spoofax

import com.google.inject.Singleton
import com.virtlink.editorservices.AesiBaseModule
import com.virtlink.editorservices.spoofax.syntaxcoloring.ISpoofaxSyntaxColoringService
import com.virtlink.editorservices.spoofax.syntaxcoloring.SpoofaxSyntaxColoringService
import com.virtlink.editorservices.syntaxcoloring.ISyntaxColoringService

/**
 * Binds the AESI interfaces to their Spoofax implementations.
 */
internal class AesiModule : AesiBaseModule() {

    override fun configure() {
        super.configure()

        configureAesiServices()
    }

    override fun configureSyntaxColoring() {
        bind(ISyntaxColoringService::class.java).to(SpoofaxSyntaxColoringService::class.java).`in`(Singleton::class.java)
        bind(ISpoofaxSyntaxColoringService::class.java).to(SpoofaxSyntaxColoringService::class.java).`in`(Singleton::class.java)
    }

    private fun configureAesiServices() {
        bind(LanguageHelper::class.java).`in`(Singleton::class.java)
    }

}