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

package org.metaborg.intellij.jps.targetbuilders;

import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.project.ILanguageSpec;
import org.metaborg.core.project.ILanguageSpecService;
import org.metaborg.intellij.jps.project.JpsProjectService;
import org.metaborg.intellij.jps.project.MetaborgJpsProject;
import org.metaborg.spoofax.core.project.ISpoofaxLanguageSpecPaths;
import org.metaborg.spoofax.core.project.ISpoofaxLanguageSpecPathsService;
import org.metaborg.spoofax.core.project.configuration.ISpoofaxLanguageSpecConfig;
import org.metaborg.spoofax.core.project.configuration.ISpoofaxLanguageSpecConfigService;
import org.metaborg.spoofax.meta.core.LanguageSpecBuildInput;
import org.metaborg.util.log.ILogger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;

/**
 * Spoofax builder.
 */
public abstract class SpoofaxBuilder<T extends SpoofaxTarget> extends TargetBuilder<SpoofaxSourceRootDescriptor, T> {

    protected final JpsProjectService projectService;
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
            final JpsProjectService projectService,
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
    protected LanguageSpecBuildInput getBuildInput(final JpsModule module) throws ProjectBuildException,
            IOException {
        @Nullable final MetaborgJpsProject project = this.projectService.get(module);
        if (project == null)
            throw new ProjectBuildException(logger.format(
                    "Could not get a project for the module {}",
                    module
            ));
        @Nullable final ILanguageSpec languageSpec = this.languageSpecService.get(project);
        if (languageSpec == null)
            throw new ProjectBuildException(logger.format(
                    "Could not get a language specification for the project {}",
                    project
            ));
        @Nullable final ISpoofaxLanguageSpecConfig config = this.spoofaxLanguageSpecConfigService.get(languageSpec);
        if (config == null)
            throw new ProjectBuildException(logger.format(
                    "Could not get a configuration for language specification {}",
                    languageSpec
            ));
        final ISpoofaxLanguageSpecPaths paths = this.pathsService.get(languageSpec);

        return new LanguageSpecBuildInput(languageSpec, config, paths);
    }

}