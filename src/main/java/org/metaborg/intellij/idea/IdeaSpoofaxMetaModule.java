/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea;

import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IFileElementType;
import org.metaborg.core.syntax.*;
import org.metaborg.intellij.idea.compilation.IAfterCompileTask;
import org.metaborg.intellij.idea.compilation.IBeforeCompileTask;
import org.metaborg.intellij.idea.compilation.ReloadLanguageCompileTask;
import org.metaborg.intellij.idea.configuration.ConfigurationFileEventListener;
import org.metaborg.intellij.idea.configuration.ConfigurationUtils;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.parsing.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.idea.parsing.references.IMetaborgReferenceProviderFactory;
import org.metaborg.intellij.idea.parsing.references.MetaborgReferenceProvider;
import org.metaborg.intellij.idea.parsing.references.SpoofaxReferenceProvider;
import org.metaborg.intellij.idea.projects.IIdeaLanguageSpecFactory;
import org.metaborg.intellij.idea.projects.IdeaLanguageSpecFactory;
import org.metaborg.intellij.idea.projects.MetaborgModuleBuilder;
import org.metaborg.intellij.idea.projects.ProjectUtils;
import org.metaborg.intellij.languages.ILanguageManager;
import org.metaborg.spoofax.core.syntax.*;
import org.metaborg.spoofax.meta.core.SpoofaxMetaModule;

/**
 * The Guice dependency injection module for the Spoofax IntelliJ IDEA meta plugin.
 */
/* package private */ final class IdeaSpoofaxMetaModule extends SpoofaxMetaModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bindLanguageProject();
        bindMetaProject();
        bindElements();
        bindReferenceResolution();
        bindLanguageManagement();
        bindBeforeCompileTasks();
        bindAfterCompileTasks();
        bindConfiguration();
        bindParsing();
    }

    @Override
    protected void bindLanguageSpec() {
        super.bindLanguageSpec();

        this.bind(IIdeaLanguageSpecFactory.class).to(IdeaLanguageSpecFactory.class).in(Singleton.class);
    }

    /**
     * Binds language project services.
     */
    protected void bindLanguageProject() {
        bind(ILanguageProjectService.class).to(DefaultLanguageProjectService.class).in(Singleton.class);
    }

    /**
     * Meta project classes.
     */
    protected void bindMetaProject() {
        bind(MetaborgModuleBuilder.class).in(Singleton.class);
    }

    /**
     * Binds token and PSI elements and related classes.
     */
    protected void bindElements() {
        bind(IMetaborgPsiElementFactory.class).to(DefaultMetaborgPsiElementFactory.class).in(Singleton.class);
        install(new FactoryModuleBuilder()
                .implement(IFileElementType.class, MetaborgFileElementType.class)
                .build(IFileElementTypeFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ATermAstElementTypeProvider.class, ATermAstElementTypeProvider.class)
                .build(IATermAstElementTypeProviderFactory.class));
    }


    /**
     * Binds reference resolution classes.
     */
    protected void bindReferenceResolution() {
        install(new FactoryModuleBuilder()
                .implement(MetaborgReferenceProvider.class, SpoofaxReferenceProvider.class)
                .build(IMetaborgReferenceProviderFactory.class));
    }

    /**
     * Binds language management.
     */
    protected void bindLanguageManagement() {
        bind(DefaultIdeaLanguageManager.class).in(Singleton.class);
        bind(ILanguageManager.class).to(DefaultIdeaLanguageManager.class).in(Singleton.class);
        bind(IIdeaLanguageManager.class).to(DefaultIdeaLanguageManager.class).in(Singleton.class);
        bind(ILanguageBindingManager.class).to(DefaultIdeaLanguageManager.class).in(Singleton.class);
    }

    /**
     * Binds the before compile tasks.
     */
    protected void bindBeforeCompileTasks() {
        final Multibinder<IBeforeCompileTask> beforeCompileTasks = Multibinder.newSetBinder(
                binder(),
                IBeforeCompileTask.class
        );
    }

    /**
     * Binds the after compile tasks.
     */
    protected void bindAfterCompileTasks() {
        final Multibinder<IAfterCompileTask> afterCompileTasks = Multibinder.newSetBinder(
                binder(),
                IAfterCompileTask.class
        );

        bind(ReloadLanguageCompileTask.class).in(Singleton.class);
        afterCompileTasks.addBinding().to(ReloadLanguageCompileTask.class);
    }

    /**
     * Binds configuration classes.
     */
    protected void bindConfiguration() {
        bind(ConfigurationUtils.class).in(Singleton.class);
        bind(ConfigurationFileEventListener.class).in(Singleton.class);
    }

    /**
     * Binds lexing and parsing.
     */
    protected void bindParsing() {
        bind(SpoofaxSyntaxHighlighterFactory.class);

        install(new FactoryModuleBuilder()
                .implement(Lexer.class, SpoofaxHighlightingLexer.class)
                .build(IHighlightingLexerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Lexer.class, CharacterLexer.class)
                .build(ICharacterLexerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ParserDefinition.class, MetaborgParserDefinition.class)
                .build(IParserDefinitionFactory.class));
    }
}
