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

package org.metaborg.intellij.jps;

import com.google.inject.*;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.jps.builders.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.intellij.projects.*;
import org.metaborg.meta.core.project.*;
import org.metaborg.spoofax.meta.core.SpoofaxMetaModule;
import org.metaborg.spoofax.meta.core.project.*;

import java.util.*;

/**
 * The Guice dependency injection module for the Spoofax JPS meta plugin.
 */
/* package private */ class JpsSpoofaxMetaModule extends SpoofaxMetaModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bindBuilders();
        bindMetaProject();
        bindTargetTypes();
    }

    /**
     * Binds the builders.
     */
    protected void bindBuilders() {
        bind(JpsSpoofaxMetaBuilder.class).in(Singleton.class);

        bind(MetaborgLanguageBuilder.class).in(Singleton.class);

        bind(SpoofaxPreBuilder.class).in(Singleton.class);
        bind(SpoofaxPostBuilder.class).in(Singleton.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindLanguageSpec() {
        bind(JpsLanguageSpecService.class).in(Singleton.class);
        bind(ILanguageSpecService.class).to(JpsLanguageSpecService.class);
        bind(ISpoofaxLanguageSpecService.class).to(JpsLanguageSpecService.class);
    }

    /**
     * Bind meta project classes.
     */
    protected void bindMetaProject() {
        bind(ProjectUtils.class).in(Singleton.class);
    }

    /**
     * Binds the target types.
     */
    protected void bindTargetTypes() {
        bind(SpoofaxPreTargetType.class).in(Singleton.class);
        bind(SpoofaxPostTargetType.class).in(Singleton.class);
    }

    @SuppressWarnings("unused")
    @Singleton
    @Provides
    @Inject
    public final Collection<TargetBuilder<?, ?>> provideTargetBuilders(
            final SpoofaxPreBuilder preBuilder,
            final SpoofaxPostBuilder postBuilder) {
        return Arrays.asList(preBuilder, postBuilder);
    }


    @SuppressWarnings("unused")
    @Singleton
    @Provides
    @Inject
    public final Collection<ModuleLevelBuilder> provideModuleLevelBuilders(
            final MetaborgLanguageBuilder languageBuilder) {
        return Collections.singletonList(languageBuilder);
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
}
