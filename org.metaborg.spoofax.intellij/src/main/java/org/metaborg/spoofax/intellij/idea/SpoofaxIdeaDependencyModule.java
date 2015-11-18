/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.psi.tree.IFileElementType;
import org.metaborg.core.project.ArtifactProjectService;
import org.metaborg.core.project.Compound;
import org.metaborg.core.project.CompoundProjectService;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.syntax.IParserConfiguration;
import org.metaborg.idea.gui.DefaultLanguageListItemsProvider;
import org.metaborg.idea.gui.ILanguageListItemsProvider;
import org.metaborg.idea.gui.ILanguageListItemsProviderFactory;
import org.metaborg.idea.psi.IMetaborgReferenceProviderFactory;
import org.metaborg.idea.psi.MetaborgReferenceProvider;
import org.metaborg.idea.spoofax.psi.SpoofaxReferenceProvider;
import org.metaborg.spoofax.core.syntax.JSGLRParserConfiguration;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.factories.*;
import org.metaborg.spoofax.intellij.idea.languages.*;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxModuleBuilder;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxModuleType;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplEditor;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplPanel;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplTableModel;
import org.metaborg.spoofax.intellij.idea.psi.ATermAstElementTypeProvider;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxAnnotator;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxFileElementType;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxPsiElementFactory;
import org.metaborg.spoofax.intellij.menu.*;
import org.metaborg.spoofax.intellij.project.IIntelliJProjectService;
import org.metaborg.spoofax.intellij.project.IntelliJProject;
import org.metaborg.spoofax.intellij.project.IntelliJProjectService;
import org.metaborg.spoofax.intellij.sdk.SpoofaxSdkType;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * The Guice dependency injection module for the Spoofax IDEA plugin.
 */
public final class SpoofaxIdeaDependencyModule extends SpoofaxIntelliJDependencyModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bind(IIdeaAttachmentManager.class).to(IdeaAttachmentManager.class).in(Singleton.class);
        bind(IIdeaLanguageManager.class).to(IdeaLanguageManagerImpl.class).in(Singleton.class);
        bind(SpoofaxModuleBuilder.class).in(Singleton.class);
        bind(ILexerParserManager.class).to(LexerParserManager.class).in(Singleton.class);
        bind(BuilderMenuBuilder.class).in(Singleton.class);
        bind(ActionHelper.class).in(Singleton.class);
        bind(StrategoResourceTransformer.class).in(Singleton.class);
        bind(IResourceTransformer.class).to(StrategoResourceTransformer.class);
        bind(new TypeLiteral<ResourceTransformer<?, ?, ?>>() {}).to(StrategoResourceTransformer.class);
        bind(new TypeLiteral<ResourceTransformer<IStrategoTerm, IStrategoTerm, IStrategoTerm>>() {}).to(
                StrategoResourceTransformer.class);
        bind(SpoofaxAnnotator.class).in(Singleton.class);
        bind(ISpoofaxPsiElementFactory.class).to(SpoofaxPsiElementFactory.class).in(Singleton.class);

        bind(SpoofaxSyntaxHighlighterFactory.class);
        bind(IParserConfiguration.class).toInstance(new JSGLRParserConfiguration(
            /* implode    */ true,
            /* recovery   */ true,
            /* completion */ false,
            /* timeout    */ 30000));

        install(new FactoryModuleBuilder()
                        .implement(Lexer.class, SpoofaxLexer.class)
                        .build(IHighlightingLexerFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(Lexer.class, CharacterLexer.class)
                        .build(ICharacterLexerFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(ParserDefinition.class, SpoofaxParserDefinition.class)
                        .build(IParserDefinitionFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(IFileElementType.class, SpoofaxFileElementType.class)
                        .build(IFileElementTypeFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(IntelliJProject.class, IntelliJProject.class)
                        .build(IProjectFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(LanguageImplTableModel.class, LanguageImplTableModel.class)
                        .build(ILanguageImplTableModelFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(LanguageImplPanel.class, LanguageImplPanel.class)
                        .build(ILanguageImplPanelFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(LanguageImplEditor.class, LanguageImplEditor.class)
                        .build(ILanguageImplEditorFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(TransformationAction.class, TransformationAction.class)
                        .build(ITransformIdeaActionFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(BuilderActionGroup.class, BuilderActionGroup.class)
                        .build(IBuilderActionGroupFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(ATermAstElementTypeProvider.class, ATermAstElementTypeProvider.class)
                        .build(IATermAstElementTypeProviderFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(MetaborgReferenceProvider.class, SpoofaxReferenceProvider.class)
                        .build(IMetaborgReferenceProviderFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(ILanguageListItemsProvider.class, DefaultLanguageListItemsProvider.class)
                        .build(ILanguageListItemsProviderFactory.class));

        install(new IntelliJExtensionProviderFactory().provide(SpoofaxSdkType.class, SdkType.EP_NAME.getName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindProject() {
        bind(IntelliJProjectService.class).in(Singleton.class);
        bind(IProjectService.class).to(CompoundProjectService.class).in(Singleton.class);
//        bind(IProjectService.class).to(IntelliJProjectService.class).in(Singleton.class);
        bind(IIntelliJProjectService.class).to(IntelliJProjectService.class).in(Singleton.class);
        bind(ArtifactProjectService.class).in(Singleton.class);
        bind(CompoundProjectService.class).in(Singleton.class);

        Multibinder<IProjectService> uriBinder = Multibinder.newSetBinder(binder(),
                                                                          IProjectService.class,
                                                                          Compound.class);
        uriBinder.addBinding().to(IntelliJProjectService.class);
        uriBinder.addBinding().to(ArtifactProjectService.class);
    }

    @Provides
    @Singleton
    private SpoofaxModuleType provideModuleType() {
        return (SpoofaxModuleType) ModuleTypeManager.getInstance().findByID(SpoofaxModuleType.ID);
    }
}
