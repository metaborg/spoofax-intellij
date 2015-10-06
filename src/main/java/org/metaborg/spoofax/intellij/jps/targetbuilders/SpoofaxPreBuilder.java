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
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.build.BuildInput;
import org.metaborg.core.build.BuildInputBuilder;
import org.metaborg.core.build.IBuildOutput;
import org.metaborg.core.build.dependency.IDependencyService;
import org.metaborg.core.build.paths.ILanguagePathService;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.processing.ITask;
import org.metaborg.core.project.ProjectException;
import org.metaborg.core.transform.CompileGoal;
import org.metaborg.spoofax.core.processing.SpoofaxProcessorRunner;
import org.metaborg.spoofax.core.project.settings.ISpoofaxProjectSettingsService;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
import org.metaborg.spoofax.core.resource.SpoofaxIgnoresSelector;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.spoofax.meta.core.MetaBuildInput;
import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;
import org.metaborg.spoofax.meta.core.ant.AntSLF4JLogger;
import org.slf4j.Logger;
import org.spoofax.interpreter.terms.IStrategoTerm;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

@Singleton
public final class SpoofaxPreBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> {

    @NotNull
    private final ISpoofaxProjectSettingsService settingsService;

//    private static final Logger logger = LoggerFactory.getLogger(SpoofaxPreBuilder.class);
    @NotNull
    private final SpoofaxMetaBuilder builder;
    @NotNull
    private final JpsProjectService projectService;
    @NotNull
    private final LanguageManager languageManager;
    @NotNull
    private final ILanguagePathService languagePathService;
    @NotNull
    private final IDependencyService dependencyService;
    @NotNull
    private final SpoofaxProcessorRunner processorRunner;
    @InjectLogger
    private Logger logger;

    @Inject
    public SpoofaxPreBuilder(@NotNull final SpoofaxPreTargetType targetType,
                             @NotNull final ISpoofaxProjectSettingsService settingsService,
                             @NotNull final SpoofaxMetaBuilder builder,
                             @NotNull final JpsProjectService projectService,
                             @NotNull final LanguageManager languageManager,
                             @NotNull ILanguagePathService languagePathService,
                             @NotNull IDependencyService dependencyService,
                             @NotNull SpoofaxProcessorRunner processorRunner) {
        super(Collections.singletonList(targetType));
        this.settingsService = settingsService;
        this.builder = builder;
        this.projectService = projectService;
        this.languageManager = languageManager;
        this.languagePathService = languagePathService;
        this.dependencyService = dependencyService;
        this.processorRunner = processorRunner;
    }

    @NotNull
    @Override
    public final String getPresentableName() {
        return "Spoofax pre-Java builder";
    }

    @Override
    public final void build(@NotNull final SpoofaxPreTarget target,
                            @NotNull final DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> holder,
                            @NotNull final BuildOutputConsumer consumer,
                            @NotNull final CompileContext context) throws ProjectBuildException, IOException {

        try {
            final SpoofaxJpsProject project = this.projectService.get(target.getModule());
            final SpoofaxProjectSettings settings = this.settingsService.get(project);
            final MetaBuildInput metaInput = new MetaBuildInput(project, settings);

            this.languageManager.loadMetaLanguages();

            initialize(metaInput, context);
            generateSources(metaInput, context);
            regularBuild(metaInput, context);
            compilePreJava(metaInput, null, new AntSLF4JLogger(), context);

        } catch (ProjectException e) {
            this.logger.error("An unexpected project exception occurred.", e);
            throw new ProjectBuildException(e);
        } catch (Exception e) {
            this.logger.error("An unexpected exception occurred.", e);
            throw new ProjectBuildException(e);
        }

    }

    private final void initialize(@NotNull final MetaBuildInput metaInput, @NotNull final CompileContext context) throws
            ProjectBuildException {
        try {
            context.checkCanceled();
            context.processMessage(BuilderUtils.formatProgress(0f, "Initializing {}", metaInput.project));

            this.builder.initialize(metaInput);
        } catch (FileSystemException e) {
            throw new ProjectBuildException("Error initializing", e);
        }
    }

    private final void generateSources(@NotNull final MetaBuildInput metaInput,
                                       @NotNull final CompileContext context) throws Exception, ProjectBuildException {
        try {
            context.checkCanceled();
            context.processMessage(BuilderUtils.formatProgress(0f,
                                                               "Generating Spoofax sources for {}",
                                                               metaInput.project));

            this.builder.generateSources(metaInput);
        } catch (Exception e) {
            throw new ProjectBuildException(e.getMessage(), e);
        }
    }

    private final void regularBuild(@NotNull final MetaBuildInput metaInput,
                                    @NotNull final CompileContext context) throws ProjectBuildException {

        context.processMessage(BuilderUtils.formatProgress(0f, "Analyzing and transforming {}", metaInput.project));
        context.checkCanceled();

        final BuildInput input = getBuildInput(metaInput, dependencyService, languagePathService);

        try {
            final ITask<IBuildOutput<IStrategoTerm, IStrategoTerm, IStrategoTerm>> task = processorRunner
                    .build(input, null, null)
                    .schedule()
                    .block();

            if (!task.cancelled()) {
                final IBuildOutput<?, ?, ?> output = task.result();
                if (output != null) {
                    for (IMessage msg : output.allMessages()) {
                        context.processMessage(BuilderUtils.formatMessage("Spoofax", msg));
                    }
                    for (IMessage msg : output.extraMessages()) {
                        context.processMessage(BuilderUtils.formatMessage("Spoofax", msg));
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
        } catch (InterruptedException e) {
            throw new ProjectBuildException("Interrupted!", e);
        }
    }

    private final void compilePreJava(@NotNull final MetaBuildInput metaInput,
                                      @Nullable final URL[] classpath,
                                      @Nullable final BuildListener listener,
                                      @NotNull final CompileContext context) throws Exception, ProjectBuildException {
        context.checkCanceled();
        context.processMessage(BuilderUtils.formatProgress(0f, "Building language project {}", metaInput.project));
        this.builder.compilePreJava(metaInput, classpath, listener, null);
    }

    private final BuildInput getBuildInput(@NotNull final MetaBuildInput metaInput,
                                           @NotNull final IDependencyService dependencyService,
                                           @NotNull final ILanguagePathService languagePathService) throws
            ProjectBuildException {
        BuildInput input = null;
        try {
            input = new BuildInputBuilder(metaInput.project)
                    .withDefaultIncludePaths(true)
                    .withSourcesFromDefaultSourceLocations(true)
                    .withSelector(new SpoofaxIgnoresSelector())
                            //.withMessagePrinter()
                    .withThrowOnErrors(false)
                    .withPardonedLanguageStrings(metaInput.settings.pardonedLanguages())
                    .addTransformGoal(new CompileGoal())
                    .build(dependencyService, languagePathService);
        } catch (MetaborgException e) {
            throw new ProjectBuildException(e);
        }
        assert input != null;
        return input;
    }

}