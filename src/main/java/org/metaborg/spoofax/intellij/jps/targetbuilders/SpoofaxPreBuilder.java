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
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.build.BuildInput;
import org.metaborg.core.build.BuildInputBuilder;
import org.metaborg.core.build.IBuildOutput;
import org.metaborg.core.build.dependency.IDependencyService;
import org.metaborg.core.build.paths.ILanguagePathService;
import org.metaborg.core.language.*;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.processing.ITask;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.project.ProjectException;
import org.metaborg.core.transform.CompileGoal;
import org.metaborg.spoofax.core.processing.SpoofaxProcessorRunner;
import org.metaborg.spoofax.core.project.settings.ISpoofaxProjectSettingsService;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
import org.metaborg.spoofax.core.resource.SpoofaxIgnoresSelector;
import org.metaborg.spoofax.intellij.LanguageManager;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;
import org.metaborg.spoofax.intellij.jps.JpsPlugin;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;
import org.metaborg.spoofax.intellij.project.IntelliJProjectService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.meta.core.MetaBuildInput;
import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Singleton
public final class SpoofaxPreBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> {

    private static final Logger logger = LoggerFactory.getLogger(SpoofaxPreBuilder.class);

    private final ISpoofaxProjectSettingsService settingsService;
    private final SpoofaxMetaBuilder builder;
    private final JpsProjectService projectService;

    @Inject
    public SpoofaxPreBuilder(SpoofaxPreTargetType targetType, ISpoofaxProjectSettingsService settingsService, SpoofaxMetaBuilder builder, JpsProjectService projectService){
        super(Collections.singletonList(targetType));
        this.settingsService = settingsService;
        this.builder = builder;
        this.projectService = projectService;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax pre-Java builder";
    }

    @Override
    public void build(@NotNull SpoofaxPreTarget target,
                      @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> holder,
                      @NotNull BuildOutputConsumer consumer,
                      @NotNull CompileContext context) throws ProjectBuildException, IOException {

        try {
            final SpoofaxJpsProject project = projectService.get(target.getModule());
            final SpoofaxProjectSettings settings = settingsService.get(project);
            final MetaBuildInput input = new MetaBuildInput(project, settings);

            initialize(input, context);
            generateSources(input, context);
            regularBuild(project, context);
            compilePreJava(input, null, null, context);

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

    private void initialize(@NotNull MetaBuildInput input, @NotNull CompileContext context) throws FileSystemException, ProjectBuildException {
        context.checkCanceled();
        context.processMessage(BuilderUtils.formatProgress(0f, "Initializing language project {}", input.project));
        this.builder.initialize(input);
    }

    private void generateSources(@NotNull MetaBuildInput input, @NotNull CompileContext context) throws Exception, ProjectBuildException {
        context.checkCanceled();
        context.processMessage(BuilderUtils.formatProgress(0f, "Generating sources for language project {}", input.project));
        this.builder.generateSources(input);
    }

    private void regularBuild(@NotNull SpoofaxJpsProject project, @NotNull CompileContext context) throws ProjectBuildException {
        //JpsProjectService projectService = new JpsProjectService(target.getModule());

        //Injector injector = Guice.createInjector(new SpoofaxJpsDependencyModule(target.getModule()));

//        ILanguageService languageService = JpsPlugin.injector().getInstance(ILanguageService.class);
        ILanguagePathService languagePathService = JpsPlugin.injector().getInstance(ILanguagePathService.class);
//        IProjectService projectService = JpsPlugin.injector().getInstance(IProjectService.class);
        IDependencyService dependencyService = JpsPlugin.injector().getInstance(IDependencyService.class);
        SpoofaxProcessorRunner processorRunner = JpsPlugin.injector().getInstance(SpoofaxProcessorRunner.class);
//        ILanguageDiscoveryService discoveryService = JpsPlugin.injector().getInstance(ILanguageDiscoveryService.class);
//        IIntelliJResourceService resourceService = JpsPlugin.injector().getInstance(IIntelliJResourceService.class);
        LanguageManager languageManager = JpsPlugin.injector().getInstance(LanguageManager.class);

        //System.out.println(target.getOutputRoots(context));
        //File outputDirectory = getBuildOutputDirectory(target.getModule(), false, compileContext);
        context.processMessage(BuilderUtils.formatProgress(0f, "Analyzing and transforming language files {}", project));
        //buildSpoofax(target.getModule());
        context.checkCanceled();

        languageManager.loadMetaLanguages();

        //context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.INFO, "Using these languages: " + Joiner.on(", ").join(languageService.getAllLanguages())));



        //FileObject location = resourceService.resolve("file:///home/daniel/repos/spoofax-test-project");
        //IProject project = projectService.get(location);
        //IProject project = new SpoofaxProject(location);    // TODO: Get the project
        BuildInput input = getBuildInput(dependencyService, languagePathService, project);

        try {
            ITask<IBuildOutput<IStrategoTerm, IStrategoTerm, IStrategoTerm>> task = processorRunner
                    .build(input, null, null)
                    .schedule()
                    .block();

            if (!task.cancelled())
            {
                final IBuildOutput<?, ?, ?> output = task.result();
                if (output != null) {
                    for (IMessage msg : output.allMessages()) {
                        context.processMessage(BuilderUtils.formatMessage("Spoofax", msg));
                    }
                    for (IMessage msg : output.extraMessages()) {
                        context.processMessage(BuilderUtils.formatMessage("Spoofax", msg));
                    }
                    // TODO:
//                    if (!output.success()) {
//                        throw new ProjectBuildException("Compilation finished but failed.");
//                    }
                }
                else {
                    throw new ProjectBuildException("Compilation finished with no output.");
                }
            }
            else {
                throw new ProjectBuildException("Compilation cancelled.");
            }
        } catch (InterruptedException e) {
            throw new ProjectBuildException("Interrupted!", e);
        }
    }

    private void compilePreJava(@NotNull MetaBuildInput input, @Nullable URL[] classpath, @Nullable BuildListener listener, @NotNull CompileContext context) throws Exception, ProjectBuildException {
        context.checkCanceled();
        context.processMessage(BuilderUtils.formatProgress(0f, "Building language project {}", input.project));
        this.builder.compilePreJava(input, classpath, listener);
    }

    private BuildInput getBuildInput(IDependencyService dependencyService, ILanguagePathService languagePathService, IProject project) throws ProjectBuildException {
        BuildInputBuilder inputBuilder = new BuildInputBuilder(project);
        BuildInput input = null;
        try {
            inputBuilder.withDefaultIncludePaths(true);
            inputBuilder.withSourcesFromDefaultSourceLocations(true);
            inputBuilder.withSelector(new SpoofaxIgnoresSelector());
            inputBuilder.addTransformGoal(new CompileGoal());
//            // TODO: Pardon ESV
//            ILanguageService ls;
//            ILanguageIdentifierService lis;
//            inputBuilder.addPardonedLanguage(ls.getImpl(new LanguageIdentifier("", "", LanguageVersion.parse(""))));
            // <pardonedLanguage>EditorService</pardonedLanguage>
            //<pardonedLanguage>SDF</pardonedLanguage>
            //<pardonedLanguage>Stratego-Sugar</pardonedLanguage>
            input = inputBuilder.build(dependencyService, languagePathService);
        } catch (MetaborgException e) {
            throw new ProjectBuildException(e);
        }
        assert input != null;
        return input;
    }

}