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

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.google.inject.matcher.*;
import com.google.inject.multibindings.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.editor.*;
import org.metaborg.core.project.*;
import org.metaborg.core.resource.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.discovery.*;
import org.metaborg.intellij.idea.actions.*;
import org.metaborg.intellij.idea.editors.*;
import org.metaborg.intellij.idea.facets.*;
import org.metaborg.intellij.idea.filetypes.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.idea.parsing.annotations.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.idea.projects.newproject.*;
import org.metaborg.intellij.idea.transformations.*;
import org.metaborg.intellij.injections.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.projects.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.intellij.vfs.*;
import org.metaborg.spoofax.core.*;

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
     * {@inheritDoc}
     */
    @Override
    protected void bindProject() {
        this.bind(IIdeaProjectFactory.class).to(IdeaProjectFactory.class).in(Singleton.class);
        this.bind(IArtifactProjectFactory.class).to(ArtifactProjectFactory.class).in(Singleton.class);

        bind(IdeaProjectService.class).in(Singleton.class);
        bind(IIdeaProjectService.class).to(IdeaProjectService.class).in(Singleton.class);
        bind(IProjectService.class).to(CompoundProjectService.class).in(Singleton.class);
        bind(ArtifactProjectService.class).in(Singleton.class);
        bind(CompoundProjectService.class).in(Singleton.class);

        final Multibinder<IProjectService> uriBinder = Multibinder.newSetBinder(
                binder(),
                IProjectService.class,
                Compound.class
        );
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
}
