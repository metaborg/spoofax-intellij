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

import org.apache.commons.vfs2.FileSystemManager;
import org.metaborg.core.editor.IEditorRegistry;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.core.syntax.IParserConfiguration;
import org.metaborg.intellij.configuration.IMetaborgApplicationConfig;
import org.metaborg.intellij.configuration.IMetaborgModuleConfig;
import org.metaborg.intellij.configuration.IMetaborgProjectConfig;
import org.metaborg.intellij.discovery.ILanguageSource;
import org.metaborg.intellij.discovery.MultiLanguageSource;
import org.metaborg.intellij.discovery.ResourceLanguageSource;
import org.metaborg.intellij.idea.actions.ActionUtils;
import org.metaborg.intellij.idea.actions.BuilderActionGroup;
import org.metaborg.intellij.idea.actions.BuilderMenuBuilder;
import org.metaborg.intellij.idea.actions.IBuilderActionGroupFactory;
import org.metaborg.intellij.idea.actions.ITransformIdeaActionFactory;
import org.metaborg.intellij.idea.actions.TransformationAction;
import org.metaborg.intellij.idea.compilation.IAfterCompileTask;
import org.metaborg.intellij.idea.compilation.IBeforeCompileTask;
import org.metaborg.intellij.idea.compilation.ReloadLanguageCompileTask;
import org.metaborg.intellij.idea.configuration.ConfigurationFileEventListener;
import org.metaborg.intellij.idea.configuration.ConfigurationUtils;
import org.metaborg.intellij.idea.editors.IdeaEditorRegistry;
import org.metaborg.intellij.idea.filetypes.LanguageArtifactFileType;
import org.metaborg.intellij.idea.graphics.DefaultIconManager;
import org.metaborg.intellij.idea.graphics.IIconManager;
import org.metaborg.intellij.idea.languages.DefaultIdeaLanguageManager;
import org.metaborg.intellij.idea.languages.DefaultLanguageProjectService;
import org.metaborg.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.intellij.idea.languages.ILanguageBindingManager;
import org.metaborg.intellij.idea.languages.ILanguageProjectService;
import org.metaborg.intellij.idea.parsing.CharacterLexer;
import org.metaborg.intellij.idea.parsing.ICharacterLexerFactory;
import org.metaborg.intellij.idea.parsing.IHighlightingLexerFactory;
import org.metaborg.intellij.idea.parsing.IParserDefinitionFactory;
import org.metaborg.intellij.idea.parsing.MetaborgParserDefinition;
import org.metaborg.intellij.idea.parsing.SpoofaxHighlightingLexer;
import org.metaborg.intellij.idea.parsing.SpoofaxSyntaxHighlighterFactory;
import org.metaborg.intellij.idea.parsing.annotations.MetaborgSourceAnnotator;
import org.metaborg.intellij.idea.parsing.elements.ATermAstElementTypeProvider;
import org.metaborg.intellij.idea.parsing.elements.DefaultMetaborgPsiElementFactory;
import org.metaborg.intellij.idea.parsing.elements.IATermAstElementTypeProviderFactory;
import org.metaborg.intellij.idea.parsing.elements.IFileElementTypeFactory;
import org.metaborg.intellij.idea.parsing.elements.IMetaborgPsiElementFactory;
import org.metaborg.intellij.idea.parsing.elements.MetaborgFileElementType;
import org.metaborg.intellij.idea.parsing.references.IMetaborgReferenceProviderFactory;
import org.metaborg.intellij.idea.parsing.references.MetaborgReferenceProvider;
import org.metaborg.intellij.idea.parsing.references.SpoofaxReferenceProvider;
import org.metaborg.intellij.idea.projects.ArtifactProjectService;
import org.metaborg.intellij.idea.projects.Compound;
import org.metaborg.intellij.idea.projects.CompoundProjectService;
import org.metaborg.intellij.idea.projects.IIdeaProjectFactory;
import org.metaborg.intellij.idea.projects.IIdeaProjectService;
import org.metaborg.intellij.idea.projects.IdeaLanguageSpecProject;
import org.metaborg.intellij.idea.projects.IdeaProjectService;
import org.metaborg.intellij.idea.projects.MetaborgModuleBuilder;
import org.metaborg.intellij.idea.projects.MetaborgModuleType;
import org.metaborg.intellij.idea.projects.newproject.INewModuleWizardStepFactory;
import org.metaborg.intellij.idea.projects.newproject.MetaborgNewModuleWizardStep;
import org.metaborg.intellij.idea.transformations.IResourceTransformer;
import org.metaborg.intellij.idea.transformations.ResourceTransformer;
import org.metaborg.intellij.injections.IntelliJModuleTypeProvider;
import org.metaborg.intellij.injections.IntelliJServiceProviderFactory;
import org.metaborg.intellij.languages.ILanguageManager;
import org.metaborg.intellij.logging.MetaborgLoggerTypeListener;
import org.metaborg.intellij.logging.Slf4JLoggerTypeListener;
import org.metaborg.intellij.projects.ProjectUtils;
import org.metaborg.intellij.resources.DefaultIntelliJResourceService;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.intellij.vfs.IIntelliJFileProviderFactory;
import org.metaborg.intellij.vfs.IntelliJFileProvider;
import org.metaborg.intellij.vfs.IntelliJFileSystemManagerProvider;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.core.syntax.JSGLRParserConfiguration;

import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IFileElementType;

/**
 * The Guice dependency injection module for the Spoofax IntelliJ IDEA plugin.
 */
/* package private */ final class IdeaSpoofaxModule extends SpoofaxModule {

    // TODO: Annotate singleton classes with @Singleton annotation.

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bindModule();
        bindLanguageManagement();
        bindLanguageSources();
        bindLoggerListeners();
        bindGraphics();
        bindFileTypes();
        bindParsing();
        bindElements();
        bindLanguageProject();
        bindAnnotators();
        bindNewProjectWizard();
        bindTransformations();
        bindConfiguration();
        bindBeforeCompileTasks();
        bindAfterCompileTasks();
        bindReferenceResolution();
    }

    /**
     * Module classes.
     */
    protected void bindModule() {
        bind(MetaborgModuleType.class).toProvider(new IntelliJModuleTypeProvider<>(MetaborgModuleType.ID));
    }

    /**
     * Binds listeners for injected loggers.
     */
    protected void bindLoggerListeners() {
        bindListener(Matchers.any(), new Slf4JLoggerTypeListener());
        bindListener(Matchers.any(), new MetaborgLoggerTypeListener());
    }

    /**
     * Binds graphics objects.
     */
    protected void bindGraphics() {
        bind(IIconManager.class).to(DefaultIconManager.class).in(Singleton.class);
    }

    /**
     * Binds file types.
     */
    protected void bindFileTypes() {
        bind(LanguageArtifactFileType.class).in(Singleton.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindResource() {
        bind(DefaultIntelliJResourceService.class).in(Singleton.class);
        bind(IResourceService.class).to(DefaultIntelliJResourceService.class).in(Singleton.class);
        bind(IIntelliJResourceService.class).to(DefaultIntelliJResourceService.class).in(Singleton.class);
        bind(FileSystemManager.class).toProvider(IntelliJFileSystemManagerProvider.class).in(Singleton.class);

        install(new FactoryModuleBuilder()
                .implement(IntelliJFileProvider.class, IntelliJFileProvider.class)
                .build(IIntelliJFileProviderFactory.class));
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
     * {@inheritDoc}
     */
    @Override
    protected void bindProject() {
        bind(IdeaProjectService.class).in(Singleton.class);
        bind(IProjectService.class).to(CompoundProjectService.class).in(Singleton.class);
        bind(IIdeaProjectService.class).to(IdeaProjectService.class).in(Singleton.class);
        bind(ArtifactProjectService.class).in(Singleton.class);
        bind(CompoundProjectService.class).in(Singleton.class);

        final Multibinder<IProjectService> uriBinder = Multibinder.newSetBinder(
                binder(),
                IProjectService.class,
                Compound.class
        );
        uriBinder.addBinding().to(IdeaProjectService.class);
        uriBinder.addBinding().to(ArtifactProjectService.class);
        bind(MetaborgModuleBuilder.class).in(Singleton.class);

        install(new FactoryModuleBuilder()
                .implement(IdeaLanguageSpecProject.class, IdeaLanguageSpecProject.class)
                .build(IIdeaProjectFactory.class));

        bind(ProjectUtils.class).in(Singleton.class);
    }

    /**
     * Binds language project services.
     */
    protected void bindLanguageProject() {
        bind(ILanguageProjectService.class).to(DefaultLanguageProjectService.class).in(Singleton.class);
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
     * Binds language sources.
     */
    protected void bindLanguageSources() {
        bind(ILanguageSource.class).to(MultiLanguageSource.class).in(Singleton.class);

        bind(ResourceLanguageSource.class).in(Singleton.class);

        final Multibinder<ILanguageSource> sources = Multibinder.newSetBinder(
                binder(),
                ILanguageSource.class,
                Compound.class
        );

        sources.addBinding().to(ResourceLanguageSource.class);
    }

    /**
     * Binds source annotators.INewModuleWizardStepFactory
     */
    protected void bindAnnotators() {
        bind(new TypeLiteral<MetaborgSourceAnnotator<?, ?>>() {}).in(Singleton.class);
//        bind(new TypeLiteral<MetaborgSourceAnnotator<IStrategoTerm, IStrategoTerm>>() {}).in(Singleton.class);
    }

    /**
     * Binds new project wizard classes.
     */
    protected void bindNewProjectWizard() {
        install(new FactoryModuleBuilder()
                .implement(MetaborgNewModuleWizardStep.class, MetaborgNewModuleWizardStep.class)
                .build(INewModuleWizardStepFactory.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindAction() {
        super.bindAction();

        bind(ActionUtils.class).in(Singleton.class);
        bind(BuilderMenuBuilder.class).in(Singleton.class);

        install(new FactoryModuleBuilder()
                .implement(BuilderActionGroup.class, BuilderActionGroup.class)
                .build(IBuilderActionGroupFactory.class));
    }

    /**
     * Binds transformations.
     */
    protected void bindTransformations() {
        bind(IResourceTransformer.class).to(new TypeLiteral<ResourceTransformer<?, ?, ?>>() {}).in(Singleton.class);

        install(new FactoryModuleBuilder()
                .implement(TransformationAction.class, TransformationAction.class)
                .build(ITransformIdeaActionFactory.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindEditor() {
        bind(IEditorRegistry.class).to(IdeaEditorRegistry.class).in(Singleton.class);
    }

    /**
     * Binds configuration classes.
     */
    protected void bindConfiguration() {
        bind(ConfigurationUtils.class).in(Singleton.class);
        bind(ConfigurationFileEventListener.class).in(Singleton.class);

        install(new IntelliJServiceProviderFactory().provide(IMetaborgApplicationConfig.class));
        install(new IntelliJServiceProviderFactory().provide(IMetaborgProjectConfig.class));
        install(new IntelliJServiceProviderFactory().provide(IMetaborgModuleConfig.class));
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
     * Binds reference resolution classes.
     */
    protected void bindReferenceResolution() {
        install(new FactoryModuleBuilder()
                .implement(MetaborgReferenceProvider.class, SpoofaxReferenceProvider.class)
                .build(IMetaborgReferenceProviderFactory.class));
    }
}
