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

package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.tools.ant.BuildListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.metaborg.core.project.ProjectException;
import org.metaborg.spoofax.core.project.settings.ISpoofaxProjectSettingsService;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.spoofax.meta.core.MetaBuildInput;
import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;
import org.metaborg.spoofax.meta.core.ant.AntSLF4JLogger;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;

/**
 * Builder executed after Java compilation.
 */
@Singleton
public final class SpoofaxPostBuilder extends SpoofaxBuilder<SpoofaxPostTarget> {

    @NotNull
    private final ISpoofaxProjectSettingsService settingsService;
    @NotNull
    private final SpoofaxMetaBuilder builder;
    @NotNull
    private final JpsProjectService projectService;
    @InjectLogger
    private Logger logger;

    @Inject
    private SpoofaxPostBuilder(@NotNull final SpoofaxPostTargetType targetType,
                               @NotNull final ISpoofaxProjectSettingsService settingsService,
                               @NotNull final SpoofaxMetaBuilder builder,
                               @NotNull final JpsProjectService projectService) {
        super(targetType);
        this.settingsService = settingsService;
        this.builder = builder;
        this.projectService = projectService;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax post-Java builder";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void build(@NotNull final SpoofaxPostTarget target,
                      @NotNull final DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPostTarget> holder,
                      @NotNull final BuildOutputConsumer consumer,
                      @NotNull final CompileContext context) throws ProjectBuildException, IOException {

        try {
            final SpoofaxJpsProject project = projectService.get(target.getModule());
            final SpoofaxProjectSettings settings = settingsService.get(project);
            final MetaBuildInput input = new MetaBuildInput(project, settings);

            compilePostJava(input, null, new AntSLF4JLogger(), context);

        } catch (FileSystemException e) {
            logger.error("An unexpected IO exception occurred.", e);
            throw e;
        } catch (ProjectException e) {
            logger.error("An unexpected project exception occurred.", e);
            throw new ProjectBuildException(e);
        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new ProjectBuildException(e);
        }

    }

    /**
     * Executes the post-Java compile meta-build step.
     *
     * @param metaInput The meta build input.
     * @param classpath The classpaths.
     * @param listener  The build listener.
     * @param context   The compile context.
     * @throws Exception
     * @throws ProjectBuildException
     */
    private void compilePostJava(@NotNull final MetaBuildInput metaInput,
                                 @Nullable final URL[] classpath,
                                 @Nullable final BuildListener listener,
                                 @NotNull final CompileContext context) throws Exception, ProjectBuildException {
        context.checkCanceled();
        context.processMessage(BuilderUtils.formatProgress(0f, "Packaging language project {}", metaInput.project));
        this.builder.compilePostJava(metaInput, classpath, listener, null);
    }


}