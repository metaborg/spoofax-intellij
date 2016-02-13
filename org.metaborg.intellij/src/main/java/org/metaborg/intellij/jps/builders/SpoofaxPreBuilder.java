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
import org.metaborg.core.*;
import org.metaborg.core.action.*;
import org.metaborg.core.build.*;
import org.metaborg.core.build.dependency.*;
import org.metaborg.core.build.paths.*;
import org.metaborg.core.language.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.processing.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.intellij.jps.configuration.*;
import org.metaborg.intellij.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.intellij.projects.*;
import org.metaborg.spoofax.core.processing.*;
import org.metaborg.spoofax.core.project.*;
import org.metaborg.spoofax.core.project.configuration.*;
import org.metaborg.spoofax.core.resource.*;
import org.metaborg.spoofax.meta.core.*;
import org.metaborg.spoofax.meta.core.ant.*;
import org.metaborg.util.log.*;
import org.spoofax.interpreter.terms.*;

import javax.annotation.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Builder executed before Java compilation.
 */
@Singleton
public final class SpoofaxPreBuilder extends SpoofaxBuilder<SpoofaxPreTarget> {

    private final SpoofaxMetaBuilder builder;
    private final ILanguageManager languageManager;
    private final ILanguagePathService languagePathService;
    private final IDependencyService dependencyService;
    private final SpoofaxProcessorRunner processorRunner;
    private final BuilderMessageFormatter messageFormatter;
    private final IMetaborgConfigService extensionService;
    private final ProjectUtils projectUtils;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link SpoofaxPreBuilder} class.
     */
    @Inject
    public SpoofaxPreBuilder(
            final SpoofaxPreTargetType targetType,
            final SpoofaxMetaBuilder builder,
            final IJpsProjectService projectService,
            final ILanguageSpecService languageSpecService,
            final ISpoofaxLanguageSpecConfigService spoofaxLanguageSpecConfigService,
            final ILanguagePathService languagePathService,
            final ILanguageManager languageManager,
            final IDependencyService dependencyService,
            final SpoofaxProcessorRunner processorRunner,
            final ISpoofaxLanguageSpecPathsService pathsService,
            final BuilderMessageFormatter messageFormatter,
            final IMetaborgConfigService extensionService,
            final ProjectUtils projectUtils) {
        super(targetType, projectService, languageSpecService, pathsService, spoofaxLanguageSpecConfigService);
        this.builder = builder;
        this.languageManager = languageManager;
        this.languagePathService = languagePathService;
        this.dependencyService = dependencyService;
        this.processorRunner = processorRunner;
        this.messageFormatter = messageFormatter;
        this.extensionService = extensionService;
        this.projectUtils = projectUtils;
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
    public final void build(
            final SpoofaxPreTarget target,
            final DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> holder,
            final BuildOutputConsumer consumer,
            final CompileContext context) throws ProjectBuildException, IOException {

        try {
            final LanguageSpecBuildInput metaInput = getBuildInput(target.getModule());

            final JpsMetaborgApplicationConfig configuration = this.extensionService.getConfiguration(
                    context.getProjectDescriptor().getModel().getGlobal());


            final Set<LanguageIdentifier> appLanguages = configuration.getLoadedLanguages();
            this.logger.debug("Loading application languages: {}", appLanguages);
            this.languageManager.discoverRange(appLanguages);
            this.logger.info("Loaded application languages: {}", appLanguages);

            final Collection<LanguageIdentifier> languages = metaInput.config.compileDependencies();
            this.logger.debug("Loading module languages: {}", languages);
            this.languageManager.discoverRange(languages);
            this.logger.info("Loaded module languages: {}", languages);

            initialize(metaInput, context);
            generateSources(metaInput, context);
            regularBuild(metaInput, context);
            compilePreJava(metaInput, null, new AntSLF4JLogger(), context);
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
     * Executes the initialize meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws ProjectBuildException
     */
    private void initialize(final LanguageSpecBuildInput metaInput, final CompileContext context) throws
            ProjectBuildException {
        try {
            context.checkCanceled();
            context.processMessage(this.messageFormatter.formatProgress(0f, "Initializing {}", metaInput.languageSpec));

            this.builder.initialize(metaInput);
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
    private void generateSources(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {
        try {
            context.checkCanceled();
            context.processMessage(this.messageFormatter.formatProgress(
                    0f,
                    "Generating Spoofax sources for {}",
                    metaInput.languageSpec
            ));

            this.builder.generateSources(metaInput, null);
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
    private void regularBuild(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {

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

            if (!task.cancelled()) {
                @Nullable final IBuildOutput<?, ?, ?> output = task.result();
                if (output != null) {
                    for (final IMessage msg : output.allMessages()) {
                        context.processMessage(this.messageFormatter.formatMessage("Spoofax", msg));
                    }
                    for (final IMessage msg : output.extraMessages()) {
                        context.processMessage(this.messageFormatter.formatMessage("Spoofax", msg));
                    }
                    // TODO:
                    if (!output.success()) {
                        throw new ProjectBuildException("Compilation finished but failed.");
                    }
                } else {
                    throw new ProjectBuildException("Compilation finished with no output.");
                }
            } else {
                throw new ProjectBuildException("Compilation cancelled.");
            }
        } catch (final InterruptedException e) {
            throw new ProjectBuildException("Interrupted!", e);
        }
    }

    /**
     * Executes the pre-Java compile meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws Exception
     * @throws ProjectBuildException
     */
    @SuppressWarnings("UnusedParameters")
    private void compilePreJava(
            final LanguageSpecBuildInput metaInput,
            @Nullable final URL[] classpath,
            @Nullable final BuildListener listener,
            final CompileContext context) throws Exception {
        context.checkCanceled();
        context.processMessage(this.messageFormatter.formatProgress(0f, "Building language project {}",
                metaInput.languageSpec));
        this.builder.compilePreJava(metaInput);
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

}