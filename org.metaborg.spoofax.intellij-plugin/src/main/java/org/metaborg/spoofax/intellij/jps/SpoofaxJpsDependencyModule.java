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

package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.core.project.IProjectService;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.intellij.jps.targetbuilders.SpoofaxPostBuilder;
import org.metaborg.spoofax.intellij.jps.targetbuilders.SpoofaxPostTargetType;
import org.metaborg.spoofax.intellij.jps.targetbuilders.SpoofaxPreBuilder;
import org.metaborg.spoofax.intellij.jps.targetbuilders.SpoofaxPreTargetType;
import org.metaborg.spoofax.meta.core.ant.AntRunnerService;
import org.metaborg.spoofax.meta.core.ant.IAntRunnerService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * The Guice dependency injection module for the Spoofax JPS plugin.
 */
public final class SpoofaxJpsDependencyModule extends SpoofaxIntelliJDependencyModule {

    /**
     * Initializes a new instance of the {@link SpoofaxJpsDependencyModule} class.
     */
    public SpoofaxJpsDependencyModule() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bind(SpoofaxPreTargetType.class).in(Singleton.class);
        bind(SpoofaxPostTargetType.class).in(Singleton.class);

        bind(SpoofaxPreBuilder.class).in(Singleton.class);
        bind(SpoofaxPostBuilder.class).in(Singleton.class);

        bind(IJpsProjectService.class).to(JpsProjectService.class).in(Singleton.class);


        bind(IAntRunnerService.class).to(AntRunnerService.class).in(Singleton.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void bindProject() {
        bind(IProjectService.class).to(JpsProjectService.class).in(Singleton.class);
    }

    @Singleton
    @Provides
    @Inject
    @NotNull
    public final Collection<BuildTargetType<?>> provideTargetTypes(
            SpoofaxPreTargetType preTargetType,
            SpoofaxPostTargetType postTargetType) {
        return Arrays.asList(preTargetType, postTargetType);
    }

    @Singleton
    @Provides
    @Inject
    @NotNull
    public final Collection<TargetBuilder<?, ?>> provideTargetBuilders(
            SpoofaxPreBuilder preBuilder,
            SpoofaxPostBuilder postBuilder) {
        return Arrays.asList(preBuilder, postBuilder);
    }

    @Singleton
    @Provides
    @Inject
    @NotNull
    public final Collection<ModuleLevelBuilder> provideModuleLevelBuilders() {
        return Collections.emptyList();
    }
}

