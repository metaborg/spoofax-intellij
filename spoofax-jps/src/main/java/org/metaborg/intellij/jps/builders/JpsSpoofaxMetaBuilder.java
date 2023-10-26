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


import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.CompileGoal;
import org.metaborg.core.build.BuildInput;
import org.metaborg.core.build.BuildInputBuilder;
import org.metaborg.core.build.dependency.IDependencyService;
import org.metaborg.core.build.dependency.MissingDependencyException;
import org.metaborg.core.build.paths.ILanguagePathService;
import org.metaborg.core.config.ConfigException;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.processing.ITask;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.jps.configuration.IMetaborgConfigService;
import org.metaborg.intellij.jps.configuration.JpsMetaborgApplicationConfig;
import org.metaborg.intellij.jps.projects.IJpsProjectService;
import org.metaborg.intellij.jps.projects.JpsLanguageSpec;
import org.metaborg.intellij.jps.projects.MetaborgJpsProject;
import org.metaborg.intellij.languages.ILanguageManager;
import org.metaborg.intellij.languages.LanguageLoadingFailedException;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.spoofax.core.build.ISpoofaxBuildOutput;
import org.metaborg.spoofax.core.processing.SpoofaxProcessorRunner;
import org.metaborg.spoofax.core.resource.SpoofaxIgnoresSelector;
import org.metaborg.spoofax.meta.core.build.LanguageSpecBuildInput;
import org.metaborg.spoofax.meta.core.build.LanguageSpecBuilder;
import org.metaborg.spoofax.meta.core.build.ProjectBuildInput;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpec;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpecService;
import org.metaborg.util.file.CollectionFileAccess;
import org.metaborg.util.log.ILogger;

import jakarta.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public final class JpsSpoofaxMetaBuilder {

    private final LanguageSpecBuilder builder;
    private final ILanguageManager languageManager;
    private final IMetaborgConfigService extensionService;
    private final ILanguagePathService languagePathService;
    private final IDependencyService dependencyService;
    private final SpoofaxProcessorRunner processorRunner;
    private final BuilderMessageFormatter messageFormatter;
    private final IJpsProjectService projectService;
    private final ISpoofaxLanguageSpecService languageSpecService;
    @InjectLogger
    private ILogger logger;

    @jakarta.inject.Inject @javax.inject.Inject
    public JpsSpoofaxMetaBuilder(
            final LanguageSpecBuilder builder,
            final ILanguageManager languageManager,
            final IMetaborgConfigService extensionService,
            final ILanguagePathService languagePathService,
            final IDependencyService dependencyService,
            final SpoofaxProcessorRunner processorRunner,
            final BuilderMessageFormatter messageFormatter,
            final IJpsProjectService projectService,
            final ISpoofaxLanguageSpecService languageSpecService
    ) {
        this.builder = builder;
        this.languageManager = languageManager;
        this.extensionService = extensionService;
        this.languagePathService = languagePathService;
        this.dependencyService = dependencyService;
        this.processorRunner = processorRunner;
        this.messageFormatter = messageFormatter;
        this.projectService = projectService;
        this.languageSpecService = languageSpecService;
    }

    /**
     * Gets or creates a Metaborg project.
     *
     * @param module The module.
     * @return The Metaborg project.
     */
    public MetaborgJpsProject getOrCreateProject(final JpsModule module) {
        @Nullable MetaborgJpsProject project = this.projectService.get(module);
        if (project == null)
            project = this.projectService.create(module);
        return project;
    }

    /**
     * Gets or creates a language specification.
     *
     * @param module The module.
     * @return The language specification project.
     */
    public JpsLanguageSpec getOrCreateLanguageSpec(final JpsModule module) {
        @Nullable final MetaborgJpsProject project = getOrCreateProject(module);
        @Nullable final ISpoofaxLanguageSpec languageSpec;
        try {
            languageSpec = this.languageSpecService.get(project);

            if (languageSpec == null) {
                throw LoggerUtils2.exception(this.logger, RuntimeException.class,
                        "The project is not a language specification.");
            }
        } catch (final ConfigException e) {
            throw LoggerUtils2.exception(this.logger, UnhandledException.class,
                    "Unexpected exception while retrieving configuration for project {}", e, project);
        }
        return (JpsLanguageSpec)languageSpec;
    }

    /**
     * Executes any before-build tasks.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws ProjectBuildException
     */
    public void beforeBuild(final ProjectBuildInput metaInput,
                            final CompileContext context)
            throws ProjectBuildException {

        try {

            @Nullable final JpsMetaborgApplicationConfig configuration
                    = this.extensionService.getGlobalConfiguration(context.getProjectDescriptor().getModel().getGlobal());

            if (configuration != null) {
                final Set<LanguageIdentifier> appLanguages = configuration.getLoadedLanguages();
                this.logger.debug("Loading application languages: {}", appLanguages);
                this.languageManager.discoverRange(appLanguages);
                this.logger.info("Loaded application languages: {}", appLanguages);
            } else {
                this.logger.warn("No application configuration found.");
            }

            final Collection<LanguageIdentifier> compileDeps = metaInput.project().config().compileDeps();
            this.logger.debug("Loading module compile dependencies: {}", compileDeps);
            this.languageManager.discoverRange(compileDeps);
            this.logger.info("Loaded module compile dependencies: {}", compileDeps);

            final Collection<LanguageIdentifier> sourceDeps = metaInput.project().config().sourceDeps();
            this.logger.debug("Loading module source dependencies: {}", sourceDeps);
            this.languageManager.discoverRange(sourceDeps);
            this.logger.info("Loaded module source dependencies: {}", sourceDeps);

        } catch (final LanguageLoadingFailedException e) {
            throw new ProjectBuildException("Loading languages failed.", e);
        }
    }

    /**
     * Executes any after-build tasks.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws ProjectBuildException
     */
    public void afterBuild(final ProjectBuildInput metaInput,
                           final CompileContext context)
            throws ProjectBuildException {

        this.languageManager.unloadRange(this.languageManager.getLoadedLanguages());

    }

    /**
     * Executes the initialize meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws ProjectBuildException
     */
    public void clean(final LanguageSpecBuildInput metaInput,
                         final CompileContext context) throws
            ProjectBuildException {
        try {
            this.logger.debug("Cleaning {}", metaInput.languageSpec());

            context.checkCanceled();
            context.processMessage(this.messageFormatter.formatProgress(0f, "Cleaning {}", metaInput.languageSpec()));

            this.builder.clean(metaInput);

            this.logger.info("Cleaned {}", metaInput.languageSpec());
        } catch (final MetaborgException e) {
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
    public void initialize(final LanguageSpecBuildInput metaInput,
                              final CompileContext context) throws
            ProjectBuildException {
        try {
            this.logger.debug("Initializing {}", metaInput.languageSpec());

            context.checkCanceled();
            context.processMessage(this.messageFormatter.formatProgress(0f, "Initializing {}", metaInput.languageSpec()));

            this.builder.initialize(metaInput);

            // TODO: Report created output files to `consumer`.

            this.logger.info("Initialized {}", metaInput.languageSpec());
        } catch (final MetaborgException e) {
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
    public void generateSources(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {
        try {
            this.logger.debug("Generating sources for {}", metaInput.languageSpec());

            context.checkCanceled();
            context.processMessage(this.messageFormatter.formatProgress(
                    0f,
                    "Generating sources for {}",
                    metaInput.languageSpec()
            ));

            this.builder.generateSources(metaInput, new CollectionFileAccess());

            // TODO: Report created output files to `consumer`.

            this.logger.info("Generated sources for {}", metaInput.languageSpec());
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
    public void regularBuild(
            final ProjectBuildInput metaInput,
            final CompileContext context) throws Exception {

        this.logger.debug("Analyzing and transforming {}", metaInput.project());

        context.processMessage(this.messageFormatter.formatProgress(
                0f,
                "Analyzing and transforming {}",
                metaInput.project()
        ));
        context.checkCanceled();

        final BuildInput input = getBuildInput(metaInput);

        try {
            final ITask<ISpoofaxBuildOutput> task = this.processorRunner
                    .build(input, null, null)
                    .schedule()
                    .block();

            // TODO: Report created output files to `consumer`.

            if (!task.cancelled()) {
                @Nullable final ISpoofaxBuildOutput output = task.result();
                if (output != null) {
                    for (final IMessage msg : output.allMessages()) {
                        context.processMessage(this.messageFormatter.formatMessage("Metaborg", msg));
                    }
                    for (final IMessage msg : output.extraMessages()) {
                        context.processMessage(this.messageFormatter.formatMessage("Metaborg", msg));
                    }
                    // TODO:
                    if (!output.success()) {
                        throw LoggerUtils2.exception(this.logger, ProjectBuildException.class,
                                "Compilation finished but failed.");
                    }
                } else {
                    throw LoggerUtils2.exception(this.logger, ProjectBuildException.class,
                            "Compilation finished with no output.");
                }
            } else {
                throw LoggerUtils2.exception(this.logger, ProjectBuildException.class,
                        "Compilation cancelled.");
            }
        } catch (final InterruptedException e) {
            throw LoggerUtils2.exception(this.logger, ProjectBuildException.class,
                    "Interrupted!", e);
        }

        this.logger.info("Analyzed and transformed {}", metaInput.project());
    }

    /**
     * Executes the pre-Java compile meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws Exception
     * @throws ProjectBuildException
     */
    public void compilePreJava(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {

        this.logger.debug("Compile pre-Java for {}", metaInput.languageSpec());

        context.checkCanceled();
        context.processMessage(this.messageFormatter.formatProgress(0f, "Building language project {}",
                metaInput.languageSpec()));
        this.builder.compile(metaInput);

        // TODO: Report created output files to `consumer`.

        this.logger.info("Compiled pre-Java for {}", metaInput.languageSpec());
    }

    /**
     * Executes the post-Java compile meta-build step.
     *
     * @param metaInput The meta build input.
     * @param context   The compile context.
     * @throws Exception
     * @throws ProjectBuildException
     */
    public void compilePostJava(
            final LanguageSpecBuildInput metaInput,
            final CompileContext context) throws Exception {

        this.logger.debug("Compiling post-Java for {}", metaInput.languageSpec());

        context.checkCanceled();
        context.processMessage(this.messageFormatter.formatProgress(
                0f,
                "Packaging language project {}",
                metaInput.languageSpec()
        ));
        this.builder.pkg(metaInput);

        // TODO: Report created output files to `consumer`.

        this.logger.info("Compiled post-Java for {}", metaInput.languageSpec());
    }

    /**
     * Creates the {@link BuildInput} for the project.
     *
     * @param metaInput The meta input.
     * @return The created {@link BuildInput}.
     * @throws ProjectBuildException
     */
    private BuildInput getBuildInput(final ProjectBuildInput metaInput) throws
            ProjectBuildException {
        final BuildInput input;
        try {
            final BuildInputBuilder builder = new BuildInputBuilder(metaInput.project())
                    .withDefaultIncludePaths(true)
                    .withSourcesFromDefaultSourceLocations(true)
                    .withSelector(new SpoofaxIgnoresSelector())
                    .withThrowOnErrors(false)
                    .addTransformGoal(new CompileGoal());

            if (metaInput instanceof LanguageSpecBuildInput) {
                final ISpoofaxLanguageSpec languageSpec = ((LanguageSpecBuildInput)metaInput).languageSpec();
                builder
                    .withPardonedLanguageStrings(languageSpec.config().pardonedLanguages());
            }

            input = builder.build(this.dependencyService, this.languagePathService);
        } catch (final MissingDependencyException e) {
            // FIXME: Add language ID field to MissingDependencyException,
            // and print the missing language ID here.
            throw LoggerUtils2.exception(this.logger, ProjectBuildException.class,
                    "Missing language dependency: {}", e, e.getMessage());
        } catch (final MetaborgException e) {
            throw new ProjectBuildException(e);
        }
        return input;
    }

    /**
     * Gets the build input for a normal project.
     *
     * @param module The JPS module.
     * @return The build input.
     * @throws ProjectBuildException
     * @throws IOException
     */
    public ProjectBuildInput getProjectBuildInput(final JpsModule module)
            throws ProjectBuildException, IOException {

        @Nullable final MetaborgJpsProject project = this.projectService.get(module);
        if (project == null) {
            throw LoggerUtils2.exception(this.logger, ProjectBuildException.class,
                    "Could not get a project for the module {}", module);
        }

        return new ProjectBuildInput(project);
    }

    /**
     * Gets the build input for a language specification.
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
            throw LoggerUtils2.exception(this.logger, ProjectBuildException.class,
                    "Could not get a project for the module {}", module);
        }

        @Nullable ISpoofaxLanguageSpec languageSpec = null;
        try {
            languageSpec = this.languageSpecService.get(project);
        } catch (final ConfigException ex) {
            this.logger.error("Ignored exception while retrieving language specification.", ex);
        }
        if (languageSpec == null) {
            throw LoggerUtils2.exception(this.logger, ProjectBuildException.class,
                    "Could not get a language specification for the project {}", project);
        }

        return new LanguageSpecBuildInput(languageSpec);
    }

}
