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
import org.apache.commons.vfs2.*;
import org.apache.tools.ant.BuildListener;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.jps.project.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.spoofax.core.project.*;
import org.metaborg.spoofax.core.project.configuration.*;
import org.metaborg.spoofax.meta.core.*;
import org.metaborg.spoofax.meta.core.ant.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.io.*;
import java.net.*;

/**
 * Builder executed after Java compilation.
 */
@Singleton
public final class SpoofaxPostBuilder extends SpoofaxBuilder<SpoofaxPostTarget> {

    private final SpoofaxMetaBuilder builder;
    private final BuilderMessageFormatter messageFormatter;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link SpoofaxPostBuilder} class.
     */
    @Inject
    private SpoofaxPostBuilder(
            final SpoofaxPostTargetType targetType,
            final SpoofaxMetaBuilder builder,
            final JpsProjectService projectService,
            final ILanguageSpecService languageSpecService,
            final ISpoofaxLanguageSpecPathsService pathsService,
            final ISpoofaxLanguageSpecConfigService spoofaxLanguageSpecConfigService,
            final BuilderMessageFormatter messageFormatter) {
        super(targetType, projectService, languageSpecService, pathsService, spoofaxLanguageSpecConfigService);
        this.builder = builder;
        this.messageFormatter = messageFormatter;
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
        context.processMessage(this.messageFormatter.formatProgress(
                0f,
                "Packaging language project {}",
                metaInput.languageSpec
        ));
        this.builder.compilePostJava(metaInput);
    }

}