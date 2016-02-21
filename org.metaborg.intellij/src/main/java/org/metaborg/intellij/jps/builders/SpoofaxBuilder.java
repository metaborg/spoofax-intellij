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

import java.io.IOException;
import java.util.Collections;

import javax.annotation.Nullable;

import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.config.ConfigException;
import org.metaborg.intellij.jps.projects.IJpsProjectService;
import org.metaborg.intellij.jps.projects.MetaborgJpsProject;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.spoofax.meta.core.build.LanguageSpecBuildInput;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpec;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpecService;
import org.metaborg.util.log.ILogger;

/**
 * Spoofax builder.
 */
public abstract class SpoofaxBuilder<T extends SpoofaxTarget> extends TargetBuilder<SpoofaxSourceRootDescriptor, T> {

    protected final IJpsProjectService projectService;
    protected final ISpoofaxLanguageSpecService languageSpecService;
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
            final ISpoofaxLanguageSpecService languageSpecService) {
        super(Collections.singletonList(targetType));
        this.projectService = projectService;
        this.languageSpecService = languageSpecService;
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

        try {
            @Nullable final ISpoofaxLanguageSpec languageSpec = this.languageSpecService.get(project);
            if (languageSpec == null) {
                throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                        "Could not get a language specification for the project {}", project);
            }

            return new LanguageSpecBuildInput(languageSpec);
        } catch(ConfigException e) {
            throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                "Could not get a language specification for the project {}", e, project);
        }
    }

}