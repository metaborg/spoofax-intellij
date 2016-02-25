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
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.*;
import com.intellij.lang.*;
import com.intellij.lexer.*;
import com.intellij.psi.tree.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.editor.*;
import org.metaborg.core.project.*;
import org.metaborg.core.resource.*;
import org.metaborg.core.syntax.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.actions.*;
import org.metaborg.intellij.discovery.*;
import org.metaborg.intellij.idea.compilation.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.idea.editors.*;
import org.metaborg.intellij.idea.facets.*;
import org.metaborg.intellij.idea.filetypes.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.parsing.*;
import org.metaborg.intellij.idea.parsing.annotations.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.idea.parsing.references.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.idea.projects.newproject.*;
import org.metaborg.intellij.idea.transformations.*;
import org.metaborg.intellij.injections.*;
import org.metaborg.intellij.languages.*;
import org.metaborg.intellij.logging.MetaborgLoggerTypeListener;
import org.metaborg.intellij.logging.Slf4JLoggerTypeListener;
import org.metaborg.intellij.projects.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.intellij.vfs.*;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.core.project.*;
import org.metaborg.spoofax.core.project.NullLegacyMavenProjectService;
import org.metaborg.spoofax.core.syntax.*;

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
        bindLibraryService();
        bindFacets();
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
    protected void bindMavenProject() {
        bind(ILegacyMavenProjectService.class).to(NullLegacyMavenProjectService.class).in(Singleton.class);
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

    /**
     * Binds the library service.
     */
    protected void bindLibraryService() {
        bind(LibraryService.class).in(Singleton.class);
    }

    /**
     * Binds facets.
     */
    protected void bindFacets() {
        install(new IntelliJExtensionProviderFactory().provide(MetaborgFacetType.class, "com.intellij.facetType"));
    }
}
