package com.virtlink.editorservices.intellij

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.virtlink.editorservices.ISessionManager
import com.virtlink.editorservices.SessionManager
import com.virtlink.editorservices.intellij.psi.*
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager
import com.virtlink.editorservices.intellij.syntaxcoloring.*
import com.virtlink.editorservices.resources.IResourceManager


class AesiIntellijModule : AbstractModule() {
    override fun configure() {
        bind(TokenScopeManager::class.java).`in`(Singleton::class.java)
        bind(IntellijResourceManager::class.java).`in`(Singleton::class.java)
        bind(IResourceManager::class.java).to(IntellijResourceManager::class.java)
        bind(SessionManager::class.java).`in`(Singleton::class.java)
        bind(ISessionManager::class.java).to(SessionManager::class.java)
        bind(IScopeNamesManager::class.java).to(ScopeNamesManager::class.java).`in`(Singleton::class.java)

        install(FactoryModuleBuilder()
                .implement(AesiLexer::class.java, AesiLexer::class.java)
                .build(AesiLexer.IFactory::class.java))
        install(FactoryModuleBuilder()
                .implement(AesiSyntaxHighlighter::class.java, AesiSyntaxHighlighter::class.java)
                .build(AesiSyntaxHighlighter.IFactory::class.java))
        install(FactoryModuleBuilder()
                .implement(AesiTokenTypeManager::class.java, AesiTokenTypeManager::class.java)
                .build(AesiTokenTypeManager.IFactory::class.java))
        install(FactoryModuleBuilder()
                .implement(AesiElementTypeManager::class.java, AesiElementTypeManager::class.java)
                .build(AesiElementTypeManager.IFactory::class.java))
        install(FactoryModuleBuilder()
                .implement(AesiAstBuilder::class.java, AesiAstBuilder::class.java)
                .build(AesiAstBuilder.IFactory::class.java))
    }
}