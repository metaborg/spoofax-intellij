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

package org.metaborg.intellij.jps.builders;

import com.google.inject.*;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.intellij.logging.*;
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