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

package org.metaborg.intellij.jps.builders;

import com.google.inject.*;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.metaborg.intellij.jps.projects.IJpsProjectService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.spoofax.meta.core.build.*;
import org.metaborg.spoofax.meta.core.project.*;
import org.metaborg.util.log.*;

/**
 * Builder executed after Java compilation.
 */
@Singleton
public final class SpoofaxPostBuilder extends MetaborgMetaBuilder2<SpoofaxPostTarget> {

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link SpoofaxPostBuilder} class.
     */
    @Inject
    private SpoofaxPostBuilder(
            final SpoofaxPostTargetType targetType,
            final IJpsProjectService projectService,
            final ISpoofaxLanguageSpecService languageSpecService,
            final JpsSpoofaxMetaBuilder jpsSpoofaxMetaBuilder) {
        super(targetType, jpsSpoofaxMetaBuilder, projectService, languageSpecService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPresentableName() {
        return "Spoofax post-Java builder";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildStarted(final CompileContext context) {
        this.logger.info("Build started!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doBuild(
            final LanguageSpecBuildInput metaInput,
            final SpoofaxPostTarget target,
            final DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPostTarget> holder,
            final BuildOutputConsumer consumer,
            final CompileContext context) throws Exception {

        this.jpsSpoofaxMetaBuilder.compilePostJava(metaInput, context);
        this.jpsSpoofaxMetaBuilder.afterBuild(metaInput, context);
    }

}