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
 * Builder executed before Java compilation.
 */
@Singleton
public final class SpoofaxPreBuilder extends MetaborgMetaBuilder2<SpoofaxPreTarget> {

    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link SpoofaxPreBuilder} class.
     */
    @jakarta.inject.Inject
    public SpoofaxPreBuilder(
            final SpoofaxPreTargetType targetType,
            final IJpsProjectService projectService,
            final ISpoofaxLanguageSpecService languageSpecService,
            final JpsSpoofaxMetaBuilder jpsSpoofaxMetaBuilder) {
        super(targetType, jpsSpoofaxMetaBuilder, projectService, languageSpecService);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getPresentableName() {
        return "Spoofax pre-Java builder";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doBuild(
            final LanguageSpecBuildInput metaInput,
            final SpoofaxPreTarget target,
            final DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> holder,
            final BuildOutputConsumer consumer,
            final CompileContext context) throws Exception {

        final boolean buildForced = context.getScope().isBuildForced(target);

        this.jpsSpoofaxMetaBuilder.beforeBuild(metaInput, context);

        // FIXME: An issue with the IntelliJ VFS causes a non-forced rebuild to fail:
        // warning: directory specified with -I does not exist: "/home/daniel/repos/spoofax-intellij/
        //     org.metaborg.intellij/build/idea-sandbox/system/compile-server/_temp_/vfs_cache/tmp_26008_trans"
        // error: No matching subdirectory found in includes for wildcard 'runtime/refactoring/*'!
        // RequiredBuilderFailed: Required builder failed. Error occurred in build step "Compile Stratego code":
        //     java.lang.Error: Builder failed
        if (true || buildForced) {
            this.logger.info("Forced build; cleaning.");
            this.jpsSpoofaxMetaBuilder.clean(metaInput, context);
        } else {
            this.logger.info("Regular build; not cleaning.");
        }
        this.jpsSpoofaxMetaBuilder.initialize(metaInput, context);
        this.jpsSpoofaxMetaBuilder.generateSources(metaInput, context);
        this.jpsSpoofaxMetaBuilder.regularBuild(metaInput, context);
        this.jpsSpoofaxMetaBuilder.compilePreJava(metaInput, context);
    }

}