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

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.google.inject.matcher.*;
import com.google.inject.multibindings.*;
import org.jetbrains.jps.builders.*;
import org.metaborg.core.editor.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.discovery.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.jps.builders.*;
import org.metaborg.intellij.jps.configuration.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.intellij.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.projects.*;
import org.metaborg.spoofax.core.*;

import java.util.*;

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
        bindBuilders();
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
     * Binds the configuration classes.
     */
    protected void bindConfig() {
        bind(JpsMetaborgApplicationConfig.class).in(Singleton.class);
        bind(MetaborgApplicationConfigDeserializer.class).in(Singleton.class);
        bind(MetaborgProjectConfigDeserializer.class).in(Singleton.class);
        bind(MetaborgModuleConfigDeserializer.class).in(Singleton.class);
        bind(MetaborgFacetConfigDeserializer.class).in(Singleton.class);
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

        install(new FactoryModuleBuilder()
                .implement(JpsMetaborgModuleFacetConfig.class, JpsMetaborgModuleFacetConfig.class)
                .build(IJpsMetaborgModuleFacetConfigFactory.class));
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindEditor() {
        bind(IEditorRegistry.class).to(NullEditorRegistry.class).in(Singleton.class);
    }

    /**
     * Binds the builders.
     */
    protected void bindBuilders() {
    }
}
