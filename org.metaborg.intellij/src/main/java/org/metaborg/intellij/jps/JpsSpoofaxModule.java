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

package org.metaborg.intellij.jps;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.metaborg.core.editor.IEditorRegistry;
import org.metaborg.core.editor.NullEditorRegistry;
import org.metaborg.core.project.IProjectService;
import org.metaborg.intellij.discovery.ILanguageSource;
import org.metaborg.intellij.discovery.MultiLanguageSource;
import org.metaborg.intellij.discovery.ResourceLanguageSource;
import org.metaborg.intellij.idea.projects.Compound;
import org.metaborg.intellij.jps.builders.BuilderMessageFormatter;
import org.metaborg.intellij.jps.builders.SpoofaxPostTargetType;
import org.metaborg.intellij.jps.builders.SpoofaxPreTargetType;
import org.metaborg.intellij.jps.configuration.DefaultMetaborgConfigService;
import org.metaborg.intellij.jps.configuration.IJpsMetaborgApplicationConfigFactory;
import org.metaborg.intellij.jps.configuration.IJpsMetaborgModuleConfigFactory;
import org.metaborg.intellij.jps.configuration.IJpsMetaborgProjectConfigFactory;
import org.metaborg.intellij.jps.configuration.IMetaborgConfigService;
import org.metaborg.intellij.jps.configuration.JpsMetaborgApplicationConfig;
import org.metaborg.intellij.jps.configuration.JpsMetaborgModuleConfig;
import org.metaborg.intellij.jps.configuration.JpsMetaborgProjectConfig;
import org.metaborg.intellij.jps.configuration.MetaborgApplicationConfigDeserializer;
import org.metaborg.intellij.jps.configuration.MetaborgModuleConfigDeserializer;
import org.metaborg.intellij.jps.configuration.MetaborgProjectConfigDeserializer;
import org.metaborg.intellij.jps.projects.IJpsProjectService;
import org.metaborg.intellij.jps.projects.JpsProjectService;
import org.metaborg.intellij.languages.DefaultLanguageManager;
import org.metaborg.intellij.languages.ILanguageManager;
import org.metaborg.intellij.logging.MetaborgLoggerTypeListener;
import org.metaborg.intellij.logging.Slf4JLoggerTypeListener;
import org.metaborg.intellij.projects.ProjectUtils;
import org.metaborg.spoofax.core.SpoofaxModule;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;

/**
 * The Guice dependency injection module for the Spoofax JPS plugin.
 */
/* package private */ final class JpsSpoofaxModule extends SpoofaxModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bindLoggerListeners();
        bindTargetTypes();
        bindMessageFormatter();
        bindConfig();
        bindModuleType();
        bindLanguageSources();
        bindLanguageManagement();
    }

    /**
     * Binds listeners for injected loggers.
     */
    protected void bindLoggerListeners() {
        // FIXME: Are these the loggers we want to use for JPS plugins?
        bindListener(Matchers.any(), new Slf4JLoggerTypeListener());
        bindListener(Matchers.any(), new MetaborgLoggerTypeListener());
    }

    /**
     * Binds the target types.
     */
    protected void bindTargetTypes() {
        bind(SpoofaxPreTargetType.class).in(Singleton.class);
        bind(SpoofaxPostTargetType.class).in(Singleton.class);
    }

    /**
     * Binds the message formatter.
     */
    protected void bindMessageFormatter() {
        bind(BuilderMessageFormatter.class).in(Singleton.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindProject() {
        bind(JpsProjectService.class).in(Singleton.class);
        bind(IProjectService.class).to(JpsProjectService.class).in(Singleton.class);
        bind(IJpsProjectService.class).to(JpsProjectService.class).in(Singleton.class);
        bind(ProjectUtils.class).in(Singleton.class);
    }

    /**
     * Binds the configuration classes.
     */
    protected void bindConfig() {
        bind(JpsMetaborgApplicationConfig.class).in(Singleton.class);
        bind(MetaborgApplicationConfigDeserializer.class).in(Singleton.class);
        bind(MetaborgProjectConfigDeserializer.class).in(Singleton.class);
        bind(MetaborgModuleConfigDeserializer.class).in(Singleton.class);
        bind(IMetaborgConfigService.class).to(DefaultMetaborgConfigService.class).in(Singleton.class);

        install(new FactoryModuleBuilder()
                .implement(JpsMetaborgApplicationConfig.class, JpsMetaborgApplicationConfig.class)
                .build(IJpsMetaborgApplicationConfigFactory.class));

        install(new FactoryModuleBuilder()
                .implement(JpsMetaborgProjectConfig.class, JpsMetaborgProjectConfig.class)
                .build(IJpsMetaborgProjectConfigFactory.class));

        install(new FactoryModuleBuilder()
                .implement(JpsMetaborgModuleConfig.class, JpsMetaborgModuleConfig.class)
                .build(IJpsMetaborgModuleConfigFactory.class));
    }

    /**
     * Binds the module type.
     */
    protected void bindModuleType() {
        bind(JpsMetaborgModuleType.class).in(Singleton.class);
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
     * Binds language management.
     */
    protected void bindLanguageManagement() {
        bind(DefaultLanguageManager.class).in(Singleton.class);
        bind(ILanguageManager.class).to(DefaultLanguageManager.class).in(Singleton.class);
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void bindResource() {
////        bind(DefaultIntelliJResourceService.class).in(Singleton.class);
////        bind(IResourceService.class).to(DefaultIntelliJResourceService.class).in(Singleton.class);
////        bind(IIntelliJResourceService.class).to(DefaultIntelliJResourceService.class).in(Singleton.class);
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindEditor() {
        bind(IEditorRegistry.class).to(NullEditorRegistry.class).in(Singleton.class);
    }

    @SuppressWarnings("unused")
    @Singleton
    @Provides
    @Inject
    public final Collection<BuildTargetType<?>> provideTargetTypes(
            final SpoofaxPreTargetType preTargetType,
            final SpoofaxPostTargetType postTargetType) {
        return Arrays.asList(preTargetType, postTargetType);
    }

    @SuppressWarnings("unused")
    @Singleton
    @Provides
    @Inject
    public final Collection<ModuleLevelBuilder> provideModuleLevelBuilders() {
        return Collections.emptyList();
    }
}
