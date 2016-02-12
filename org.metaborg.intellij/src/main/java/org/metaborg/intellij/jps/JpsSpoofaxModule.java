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
import com.google.inject.matcher.Matchers;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.jps.project.*;
import org.metaborg.intellij.jps.builders.*;
import org.metaborg.intellij.jps.serialization.*;
import org.metaborg.intellij.logging.MetaborgLoggerTypeListener;
import org.metaborg.intellij.logging.Slf4JLoggerTypeListener;
import org.metaborg.spoofax.core.SpoofaxModule;

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
    }

    /**
     * Binds the configuration classes.
     */
    protected void bindConfig() {
        bind(SpoofaxGlobalConfig.class).in(Singleton.class);
        bind(SpoofaxGlobalSerializer.class).in(Singleton.class);
        bind(SpoofaxProjectSerializer.class).in(Singleton.class);
        bind(SpoofaxModuleSerializer.class).in(Singleton.class);
        bind(SpoofaxExtensionService.class).to(SpoofaxExtensionServiceImpl.class).in(Singleton.class);

        install(new FactoryModuleBuilder()
                .implement(SpoofaxGlobalConfig.class, SpoofaxGlobalConfig.class)
                .build(IJpsMetaborgApplicationConfigFactory.class));
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
