/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.intellij.idea;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.google.inject.multibindings.*;
import com.intellij.lang.*;
import com.intellij.lexer.*;
import com.intellij.psi.tree.*;
import org.metaborg.core.syntax.*;
import org.metaborg.intellij.idea.compilation.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.parsing.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.idea.parsing.references.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.languages.*;
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

        install(new FactoryModuleBuilder()
                .implement(IdeaLanguageSpec.class, IdeaLanguageSpec.class)
                .build(IIdeaLanguageSpecFactory.class));
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

        bind(IParserConfiguration.class).toInstance(new JSGLRParserConfiguration(
            /* implode    */ true,
            /* recovery   */ true,
            /* completion */ false,
            /* timeout    */ 30000
        ));
    }
}
