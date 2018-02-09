package com.virtlink.editorservices.intellij

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import com.intellij.psi.tree.IFileElementType
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.virtlink.editorservices.ISessionManager
import com.virtlink.editorservices.SessionManager
import com.virtlink.editorservices.intellij.psi.*
import com.virtlink.editorservices.intellij.resources.IntellijResourceManager
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiLexer
import com.virtlink.editorservices.intellij.syntaxcoloring.AesiSyntaxHighlighter
import com.virtlink.editorservices.intellij.syntaxcoloring.ScopeManager
import com.virtlink.editorservices.resources.IResourceManager


class AesiIntellijModule : AbstractModule() {
    override fun configure() {
        bind(AesiTokenTypeManager::class.java).`in`(Singleton::class.java)
        bind(AesiElementTypeManager::class.java).`in`(Singleton::class.java)
        bind(ScopeManager::class.java).`in`(Singleton::class.java)
        bind(IFileElementType::class.java).to(AesiFileElementType::class.java).`in`(Singleton::class.java)
        bind(IntellijResourceManager::class.java).`in`(Singleton::class.java)
        bind(IResourceManager::class.java).to(IntellijResourceManager::class.java)
        bind(SessionManager::class.java).`in`(Singleton::class.java)
        bind(ISessionManager::class.java).to(SessionManager::class.java)

        install(FactoryModuleBuilder()
                .implement(AesiLexer::class.java, AesiLexer::class.java)
                .build(AesiLexer.IFactory::class.java))
        install(FactoryModuleBuilder()
                .implement(AesiAstBuilder::class.java, AesiAstBuilder::class.java)
                .build(IAstBuilderFactory::class.java))
        install(FactoryModuleBuilder()
                .implement(AesiSyntaxHighlighter::class.java, AesiSyntaxHighlighter::class.java)
                .build(AesiSyntaxHighlighter.IFactory::class.java))
    }
}