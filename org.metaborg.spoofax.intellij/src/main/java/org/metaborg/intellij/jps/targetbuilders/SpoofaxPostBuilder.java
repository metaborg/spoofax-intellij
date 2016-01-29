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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.tools.ant.BuildListener;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.project.ILanguageSpecService;
import org.metaborg.core.project.ProjectException;
import org.metaborg.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.core.project.ISpoofaxLanguageSpecPathsService;
import org.metaborg.spoofax.core.project.configuration.ISpoofaxLanguageSpecConfigService;
import org.metaborg.spoofax.meta.core.LanguageSpecBuildInput;
import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;
import org.metaborg.spoofax.meta.core.ant.AntSLF4JLogger;
import org.metaborg.util.log.ILogger;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;

//import org.jetbrains.jps.model.module.JpsModule;
//import org.metaborg.core.MessageFormatter;
//import org.metaborg.core.project.ILanguageSpec;
//import org.metaborg.spoofax.core.project.configuration.ISpoofaxLanguageSpecConfig;
//import org.metaborg.spoofax.core.project.settings.ISpoofaxProjectSettingsService;
//import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
//import org.metaborg.intellij.jps.project.MetaborgJpsProject;
//import org.metaborg.spoofax.meta.core.MetaBuildInput;
//import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;

/**
 * Builder executed after Java compilation.
 */
@Singleton
public final class SpoofaxPostBuilder extends SpoofaxBuilder<SpoofaxPostTarget> {

    private final SpoofaxMetaBuilder builder;
    @InjectLogger
    private ILogger logger;

    @Inject
    private SpoofaxPostBuilder(
            final SpoofaxPostTargetType targetType,
            final SpoofaxMetaBuilder builder,
            final JpsProjectService projectService,
            final ILanguageSpecService languageSpecService,
            final ISpoofaxLanguageSpecPathsService pathsService,
            final ISpoofaxLanguageSpecConfigService spoofaxLanguageSpecConfigService) {
        super(targetType, projectService, languageSpecService, pathsService, spoofaxLanguageSpecConfigService);
        this.builder = builder;
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
    public void build(
            final SpoofaxPostTarget target,
            final DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPostTarget> holder,
            final BuildOutputConsumer consumer,
            final CompileContext context) throws ProjectBuildException, IOException {

        try {
            final LanguageSpecBuildInput metaInput = getBuildInput(target.getModule());

            compilePostJava(metaInput, null, new AntSLF4JLogger(), context);

        } catch (final FileSystemException e) {
            this.logger.error("An unexpected IO exception occurred.", e);
            throw e;
        } catch (final ProjectBuildException e) {
            this.logger.error("An unexpected project build exception occurred.", e);
            throw e;
        } catch (final ProjectException e) {
            this.logger.error("An unexpected project exception occurred.", e);
            throw new ProjectBuildException(e);
        } catch (final Exception e) {
            this.logger.error("An unexpected exception occurred.", e);
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
    private void compilePostJava(
            final LanguageSpecBuildInput metaInput,
            @Nullable final URL[] classpath,
            @Nullable final BuildListener listener,
            final CompileContext context) throws Exception {
        context.checkCanceled();
        context.processMessage(BuilderUtils.formatProgress(
                0f,
                "Packaging language project {}",
                metaInput.languageSpec
        ));
        this.builder.compilePostJava(metaInput);
    }

}