package com.virtlink.editorservices

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import com.virtlink.editorservices.syntaxcoloring.ISyntaxColoringService
import com.virtlink.editorservices.syntaxcoloring.NullSyntaxColoringService

/**
 * Base dependency module for language implementations.
 */
open class AesiBaseModule : AbstractModule() {

    override fun configure() {
        configureSyntaxColoring()
    }

    /**
     * Configures the syntax coloring service.
     *
     * The default implementation binds a null service.
     */
    protected open fun configureSyntaxColoring() {
        bind(ISyntaxColoringService::class.java).to(NullSyntaxColoringService::class.java).`in`(Singleton::class.java)
    }
}