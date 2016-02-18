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

import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.meta.core.project.*;
import org.metaborg.spoofax.meta.core.*;
import org.metaborg.spoofax.meta.core.config.*;
import org.metaborg.spoofax.meta.core.project.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

/**
 * Spoofax builder.
 */
public abstract class SpoofaxBuilder<T extends SpoofaxTarget> extends TargetBuilder<SpoofaxSourceRootDescriptor, T> {

    protected final IJpsProjectService projectService;
    protected final ILanguageSpecService languageSpecService;
    protected final ISpoofaxLanguageSpecPathsService pathsService;
    protected final ISpoofaxLanguageSpecConfigService spoofaxLanguageSpecConfigService;
    @InjectLogger
    private ILogger logger;

    /**
     * Gets the presentable name of the builder.
     *
     * @return The name.
     */
    @Override
    public abstract String getPresentableName();

    /**
     * Initializes a new instance of the {@link SpoofaxBuilder} class.
     *
     * @param targetType The target type.
     */
    protected SpoofaxBuilder(
            final BuildTargetType<T> targetType,
            final IJpsProjectService projectService,
            final ILanguageSpecService languageSpecService,
            final ISpoofaxLanguageSpecPathsService pathsService,
            final ISpoofaxLanguageSpecConfigService spoofaxLanguageSpecConfigService) {
        super(Collections.singletonList(targetType));
        this.projectService = projectService;
        this.languageSpecService = languageSpecService;
        this.pathsService = pathsService;
        this.spoofaxLanguageSpecConfigService = spoofaxLanguageSpecConfigService;
    }

    /**
     * Builds the build target.
     *
     * @param target   The build target.
     * @param holder   The dirty files holder.
     * @param consumer The build output consumer.
     * @param context  The compile context.
     * @throws ProjectBuildException
     * @throws IOException
     */
    @Override
    public abstract void build(
            final T target,
            final DirtyFilesHolder<SpoofaxSourceRootDescriptor, T> holder,
            final BuildOutputConsumer consumer,
            final CompileContext context) throws ProjectBuildException, IOException;

    /**
     * Gets the build input.
     *
     * @param module The JPS module.
     * @return The build input.
     * @throws ProjectBuildException
     * @throws IOException
     */
    protected LanguageSpecBuildInput getBuildInput(final JpsModule module)
            throws ProjectBuildException, IOException {

        @Nullable final MetaborgJpsProject project = this.projectService.get(module);
        if (project == null) {
            throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                    "Could not get a project for the module {}", module);
        }

        @Nullable final ILanguageSpec languageSpec = this.languageSpecService.get(project);
        if (languageSpec == null) {
            throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                    "Could not get a language specification for the project {}", project);
        }

        @Nullable final ISpoofaxLanguageSpecConfig config = this.spoofaxLanguageSpecConfigService.get(languageSpec);
        if (config == null) {
            throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                    "Could not get a configuration for language specification {}", languageSpec);
        }

        final ISpoofaxLanguageSpecPaths paths = this.pathsService.get(languageSpec);
        return new LanguageSpecBuildInput(languageSpec, config, paths);
    }

}