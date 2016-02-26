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
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.core.*;
import org.metaborg.core.action.*;
import org.metaborg.core.build.*;
import org.metaborg.core.build.dependency.*;
import org.metaborg.core.build.paths.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.processing.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.spoofax.core.processing.*;
import org.metaborg.spoofax.core.project.*;
import org.metaborg.spoofax.core.project.configuration.*;
import org.metaborg.spoofax.core.resource.*;
import org.metaborg.spoofax.meta.core.*;
import org.metaborg.util.file.*;
import org.metaborg.util.log.*;
import org.spoofax.interpreter.terms.*;

import javax.annotation.*;
import java.io.*;

public final class JpsSpoofaxMetaBuilder {

    private final SpoofaxMetaBuilder builder;
    private final ILanguagePathService languagePathService;
    private final IDependencyService dependencyService;
    private final SpoofaxProcessorRunner processorRunner;
    private final BuilderMessageFormatter messageFormatter;
    private final IJpsProjectService projectService;
    private final ILanguageSpecService languageSpecService;
    private final ISpoofaxLanguageSpecPathsService pathsService;
    private final ISpoofaxLanguageSpecConfigService spoofaxLanguageSpecConfigService;
    @InjectLogger
    private ILogger logger;

    @Inject
    public JpsSpoofaxMetaBuilder(
            final SpoofaxMetaBuilder builder,
            final ILanguagePathService languagePathService,
            final IDependencyService dependencyService,
            final SpoofaxProcessorRunner processorRunner,
            final BuilderMessageFormatter messageFormatter,
            final IJpsProjectService projectService,
            final ILanguageSpecService languageSpecService,
            final ISpoofaxLanguageSpecPathsService pathsService,
            final ISpoofaxLanguageSpecConfigService spoofaxLanguageSpecConfigService
    ) {
        this.builder = builder;
        this.languagePathService = languagePathService;
        this.dependencyService = dependencyService;
        this.processorRunner = processorRunner;
        this.messageFormatter = messageFormatter;
        this.projectService = projectService;
        this.languageSpecService = languageSpecService;
        this.pathsService = pathsService;
        this.spoofaxLanguageSpecConfigService = spoofaxLanguageSpecConfigService;
    }

    /**
     * Executes the initialize meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws ProjectBuildException
     */
    protected void clean(final LanguageSpecBuildInput metaInput,
                         final CompileContext context) throws
            ProjectBuildException {
        try {
            this.logger.debug("Cleaning {}", metaInput.languageSpec);

            context.checkCanceled();
            context.processMessage(this.messageFormatter.formatProgress(0f, "Cleaning {}", metaInput.languageSpec));

            this.builder.clean(metaInput);

            this.logger.info("Cleaned {}", metaInput.languageSpec);
        } catch (final IOException e) {
            throw new ProjectBuildException("Error cleaning", e);
        }
    }

    /**
     * Executes the initialize meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws ProjectBuildException
     */
    protected void initialize(final LanguageSpecBuildInput metaInput,
                              final CompileContext context) throws
            ProjectBuildException {
        try {
            this.logger.debug("Initializing {}", metaInput.languageSpec);

            context.checkCanceled();
            context.processMessage(this.messageFormatter.formatProgress(0f, "Initializing {}", metaInput.languageSpec));

            this.builder.initialize(metaInput);

            // TODO: Report created output files to `consumer`.

            this.logger.info("Initialized {}", metaInput.languageSpec);
        } catch (final FileSystemException e) {
            throw new ProjectBuildException("Error initializing", e);
        }
    }

    /**
     * Executes the generate-sources meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws Exception
     * @throws ProjectBuildException
     */
    protected void generateSources(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {
        try {
            this.logger.debug("Generating sources for {}", metaInput.languageSpec);

            context.checkCanceled();
            context.processMessage(this.messageFormatter.formatProgress(
                    0f,
                    "Generating sources for {}",
                    metaInput.languageSpec
            ));

            this.builder.generateSources(metaInput, new FileAccess());

            // TODO: Report created output files to `consumer`.

            this.logger.info("Generated sources for {}", metaInput.languageSpec);
        } catch (final Exception e) {
            throw new ProjectBuildException(e.getMessage(), e);
        }
    }

    /**
     * Executes the regular build meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws Exception
     * @throws ProjectBuildException
     */
    protected void regularBuild(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {

        this.logger.debug("Analyzing and transforming {}", metaInput.languageSpec);

        context.processMessage(this.messageFormatter.formatProgress(
                0f,
                "Analyzing and transforming {}",
                metaInput.languageSpec
        ));
        context.checkCanceled();

        final BuildInput input = getBuildInput(metaInput);

        try {
            final ITask<IBuildOutput<IStrategoTerm, IStrategoTerm, IStrategoTerm>> task = this.processorRunner
                    .build(input, null, null)
                    .schedule()
                    .block();

            // TODO: Report created output files to `consumer`.

            if (!task.cancelled()) {
                @Nullable final IBuildOutput<?, ?, ?> output = task.result();
                if (output != null) {
                    for (final IMessage msg : output.allMessages()) {
                        context.processMessage(this.messageFormatter.formatMessage("Metaborg", msg));
                    }
                    for (final IMessage msg : output.extraMessages()) {
                        context.processMessage(this.messageFormatter.formatMessage("Metaborg", msg));
                    }
                    // TODO:
                    if (!output.success()) {
                        throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                                "Compilation finished but failed.");
                    }
                } else {
                    throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                            "Compilation finished with no output.");
                }
            } else {
                throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                        "Compilation cancelled.");
            }
        } catch (final InterruptedException e) {
            throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                    "Interrupted!", e);
        }

        this.logger.info("Analyzed and transformed {}", metaInput.languageSpec);
    }

    /**
     * Executes the pre-Java compile meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws Exception
     * @throws ProjectBuildException
     */
    protected void compilePreJava(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {

        this.logger.debug("Compile pre-Java for {}", metaInput.languageSpec);

        context.checkCanceled();
        context.processMessage(this.messageFormatter.formatProgress(0f, "Building language project {}",
                metaInput.languageSpec));
        this.builder.compilePreJava(metaInput);

        // TODO: Report created output files to `consumer`.

        this.logger.info("Compiled pre-Java for {}", metaInput.languageSpec);
    }

    /**
     * Executes the post-Java compile meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws Exception
     * @throws ProjectBuildException
     */
    protected void compilePostJava(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {

        this.logger.debug("Compiling post-Java for {}", metaInput.languageSpec);

        context.checkCanceled();
        context.processMessage(this.messageFormatter.formatProgress(
                0f,
                "Packaging language project {}",
                metaInput.languageSpec
        ));
        this.builder.compilePostJava(metaInput);

        // TODO: Report created output files to `consumer`.

        this.logger.info("Compiled post-Java for {}", metaInput.languageSpec);
    }

    /**
     * Creates the {@link BuildInput} for the project.
     *
     * @param metaInput The meta input.
     * @return The created {@link BuildInput}.
     * @throws ProjectBuildException
     */
    private BuildInput getBuildInput(final LanguageSpecBuildInput metaInput) throws
            ProjectBuildException {
        final BuildInput input;
        try {
            input = new BuildInputBuilder(metaInput.languageSpec)
                    .withDefaultIncludePaths(true)
                    .withSourcesFromDefaultSourceLocations(true)
                    .withSelector(new SpoofaxIgnoresSelector())
                    .withThrowOnErrors(false)
                    .withPardonedLanguageStrings(metaInput.config.pardonedLanguages())
                    .addTransformGoal(new CompileGoal())
                    .build(this.dependencyService, this.languagePathService);
        } catch (final MissingDependencyException e) {
            // FIXME: Add language ID field to MissingDependencyException,
            // and print the missing language ID here.
            throw LoggerUtils.exception(this.logger, ProjectBuildException.class,
                    "Missing language dependency: {}", e, e.getMessage());
        } catch (final MetaborgException e) {
            throw new ProjectBuildException(e);
        }
        return input;
    }

    /**
     * Gets the build input.
     *
     * @param module The JPS module.
     * @return The build input.
     * @throws ProjectBuildException
     * @throws IOException
     */
    public LanguageSpecBuildInput getLanguageSpecBuildInput(final JpsModule module)
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
