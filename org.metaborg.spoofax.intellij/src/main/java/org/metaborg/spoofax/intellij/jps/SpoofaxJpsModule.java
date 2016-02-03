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

package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.metaborg.core.project.IProjectService;
import org.metaborg.intellij.jps.project.IJpsProjectService;
import org.metaborg.intellij.jps.project.JpsProjectService;
import org.metaborg.intellij.jps.targetbuilders.BuilderMessageFormatter;
import org.metaborg.intellij.jps.targetbuilders.SpoofaxPostTargetType;
import org.metaborg.intellij.jps.targetbuilders.SpoofaxPreTargetType;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJModule;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * The Guice dependency injection module for the Spoofax JPS plugin.
 */
public final class SpoofaxJpsModule extends SpoofaxIntelliJModule {

    /**
     * Initializes a new instance of the {@link SpoofaxJpsModule} class.
     */
    public SpoofaxJpsModule() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bind(SpoofaxPreTargetType.class).in(Singleton.class);
        bind(SpoofaxPostTargetType.class).in(Singleton.class);

        bind(BuilderMessageFormatter.class).in(Singleton.class);

        bind(IJpsProjectService.class).to(JpsProjectService.class).in(Singleton.class);
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
    public final Collection<BuildTargetType<?>> provideTargetTypes(
            final SpoofaxPreTargetType preTargetType,
            final SpoofaxPostTargetType postTargetType) {
        return Arrays.asList(preTargetType, postTargetType);
    }

    @Singleton
    @Provides
    @Inject
    public final Collection<ModuleLevelBuilder> provideModuleLevelBuilders() {
        return Collections.emptyList();
    }
}

