/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.metaborg.intellij.idea;

import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import org.apache.commons.vfs2.FileSystemManager;
import org.metaborg.core.editor.IEditorRegistry;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.intellij.Compound;
import org.metaborg.intellij.configuration.IMetaborgApplicationConfig;
import org.metaborg.intellij.configuration.IMetaborgModuleConfig;
import org.metaborg.intellij.configuration.IMetaborgProjectConfig;
import org.metaborg.intellij.discovery.ILanguageSource;
import org.metaborg.intellij.discovery.MultiLanguageSource;
import org.metaborg.intellij.discovery.ResourceLanguageSource;
import org.metaborg.intellij.idea.actions.*;
import org.metaborg.intellij.idea.editors.IdeaEditorRegistry;
import org.metaborg.intellij.idea.facets.MetaborgFacetType;
import org.metaborg.intellij.idea.filetypes.LanguageArtifactFileType;
import org.metaborg.intellij.idea.graphics.DefaultIconManager;
import org.metaborg.intellij.idea.graphics.IIconManager;
import org.metaborg.intellij.idea.parsing.annotations.MetaborgSourceAnnotator;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.idea.projects.newproject.INewModuleWizardStepFactory;
import org.metaborg.intellij.idea.projects.newproject.NewModuleWizardStep;
import org.metaborg.intellij.idea.transformations.IResourceTransformer;
import org.metaborg.intellij.idea.transformations.ResourceTransformer;
import org.metaborg.intellij.idea.utils.SimpleConfigUtil;
import org.metaborg.intellij.injections.IntelliJExtensionProviderFactory;
import org.metaborg.intellij.injections.IntelliJModuleTypeProvider;
import org.metaborg.intellij.injections.IntelliJServiceProviderFactory;
import org.metaborg.intellij.logging.MetaborgLoggerTypeListener;
import org.metaborg.intellij.logging.Slf4JLoggerTypeListener;
import org.metaborg.intellij.idea.projects.ProjectUtils;
import org.metaborg.intellij.resources.DefaultIntelliJResourceService;
import org.metaborg.intellij.resources.IIntelliJResourceService;
import org.metaborg.intellij.resources.LibraryService;
import org.metaborg.intellij.vfs.IIntelliJFileProviderFactory;
import org.metaborg.intellij.vfs.IntelliJFileProvider;
import org.metaborg.intellij.vfs.IntelliJFileSystemManagerProvider;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;

/**
 * The Guice dependency injection module for the Spoofax IntelliJ IDEA plugin.
 */
/* package private */ final class IdeaSpoofaxModule extends SpoofaxModule {

    // TODO: Annotate singleton classes with @Singleton annotation.

    /**
     * {@inheritDoc}
     */
    @Override protected void configure() {
        super.configure();

        bindModule();
        bindLanguageSources();
        bindLoggerListeners();
        bindGraphics();
        bindFileTypes();
        bindAnnotators();
        bindNewProjectWizard();
        bindTransformations();
        bindConfiguration();
        bindLibraryService();
        bindFacets();
        bindUtils();
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
    @Override protected void bindResource() {
        bind(DefaultIntelliJResourceService.class).in(Singleton.class);
        bind(IResourceService.class).to(DefaultIntelliJResourceService.class).in(Singleton.class);
        bind(IIntelliJResourceService.class).to(DefaultIntelliJResourceService.class).in(Singleton.class);
        bind(FileSystemManager.class).toProvider(IntelliJFileSystemManagerProvider.class).in(Singleton.class);

        install(new FactoryModuleBuilder().implement(IntelliJFileProvider.class, IntelliJFileProvider.class)
            .build(IIntelliJFileProviderFactory.class));
    }



    /**
     * {@inheritDoc}
     */
    @Override protected void bindProject() {
        this.bind(IIdeaProjectFactory.class).to(IdeaProjectFactory.class).in(Singleton.class);
        this.bind(IArtifactProjectFactory.class).to(ArtifactProjectFactory.class).in(Singleton.class);

        bind(IdeaProjectService.class).in(Singleton.class);
        bind(IIdeaProjectService.class).to(IdeaProjectService.class).in(Singleton.class);
        bind(IProjectService.class).to(CompoundProjectService.class).in(Singleton.class);
        bind(ArtifactProjectService.class).in(Singleton.class);
        bind(CompoundProjectService.class).in(Singleton.class);

        final Multibinder<IProjectService> uriBinder =
            Multibinder.newSetBinder(binder(), IProjectService.class, Compound.class);
        uriBinder.addBinding().to(IdeaProjectService.class);
        uriBinder.addBinding().to(ArtifactProjectService.class);


        bind(ProjectUtils.class).in(Singleton.class);
    }

    /**
     * Binds language sources.
     */
    protected void bindLanguageSources() {
        bind(ILanguageSource.class).to(MultiLanguageSource.class).in(Singleton.class);

        bind(ResourceLanguageSource.class).in(Singleton.class);

        final Multibinder<ILanguageSource> sources =
            Multibinder.newSetBinder(binder(), ILanguageSource.class, Compound.class);

        sources.addBinding().to(ResourceLanguageSource.class);
    }

    /**
     * Binds source annotators.INewModuleWizardStepFactory
     */
    protected void bindAnnotators() {
        bind(MetaborgSourceAnnotator.class).in(Singleton.class);
    }

    /**
     * Binds new project wizard classes.
     */
    protected void bindNewProjectWizard() {
        install(
            new FactoryModuleBuilder().implement(NewModuleWizardStep.class, NewModuleWizardStep.class)
                .build(INewModuleWizardStepFactory.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override protected void bindAction() {
        super.bindAction();

        bind(ActionUtils.class).in(Singleton.class);
        bind(BuilderMenuBuilder.class).in(Singleton.class);

        install(new FactoryModuleBuilder().implement(BuilderActionGroup.class, BuilderActionGroup.class)
            .build(IBuilderActionGroupFactory.class));
    }

    /**
     * Binds transformations.
     */
    protected void bindTransformations() {
        bind(IResourceTransformer.class)
            .to(new TypeLiteral<ResourceTransformer<ISpoofaxInputUnit, ISpoofaxParseUnit, ISpoofaxAnalyzeUnit, ISpoofaxTransformUnit<?>, ISpoofaxTransformUnit<ISpoofaxParseUnit>, ISpoofaxTransformUnit<ISpoofaxAnalyzeUnit>>>() {})
            .in(Singleton.class);

        install(new FactoryModuleBuilder().implement(TransformationAction.class, TransformationAction.class)
            .build(ITransformIdeaActionFactory.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override protected void bindEditor() {
        bind(IEditorRegistry.class).to(IdeaEditorRegistry.class).in(Singleton.class);
    }

    /**
     * Binds configuration classes.
     */
    protected void bindConfiguration() {
        install(new IntelliJServiceProviderFactory().provide(IMetaborgApplicationConfig.class));
        install(new IntelliJServiceProviderFactory().provide(IMetaborgProjectConfig.class));
        install(new IntelliJServiceProviderFactory().provide(IMetaborgModuleConfig.class));
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

    /**
     * Binds utility classes.
     */
    protected void bindUtils() {
        bind(SimpleConfigUtil.class).in(Singleton.class);
    }
}
