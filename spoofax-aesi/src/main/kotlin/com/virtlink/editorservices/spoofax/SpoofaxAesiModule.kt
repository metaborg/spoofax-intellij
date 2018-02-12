package com.virtlink.editorservices.spoofax

import com.google.inject.Singleton
import com.virtlink.editorservices.AesiBaseModule
import com.virtlink.editorservices.spoofax.syntaxcoloring.SpoofaxSyntaxColoringService
import com.virtlink.editorservices.syntaxcoloring.ISyntaxColoringService

class SpoofaxAesiModule: AesiBaseModule() {

    override fun configureSyntaxColoring() {
        bind(ISyntaxColoringService::class.java).to(SpoofaxSyntaxColoringService::class.java).`in`(Singleton::class.java)
    }

}