package org.metaborg.spoofax.intellij.jps;

import com.google.common.base.Joiner;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.build.BuildInput;
import org.metaborg.core.build.BuildInputBuilder;
import org.metaborg.core.build.IBuildOutput;
import org.metaborg.core.build.dependency.DependencyService;
import org.metaborg.core.build.dependency.IDependencyService;
import org.metaborg.core.build.paths.ILanguagePathService;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.processing.ITask;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.project.settings.YAMLProjectSettingsSerializer;
import org.metaborg.core.transform.CompileGoal;
import org.metaborg.spoofax.core.processing.ISpoofaxProcessorRunner;
import org.metaborg.spoofax.core.processing.SpoofaxProcessorRunner;
import org.metaborg.spoofax.core.resource.SpoofaxIgnoresSelector;
import org.metaborg.spoofax.intellij.*;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxProject;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.intellij.resources.IntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import java.io.IOException;
import java.util.Arrays;

/**
 * Builds the Spoofax build target.
 */
@Singleton
public final class SpoofaxBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxTarget> {

    public static final SpoofaxBuilder INSTANCE = new SpoofaxBuilder();

//    private final ILanguageService languageService;
//    private final ILanguagePathService languagePathService;
//    //private final IProjectService projectService;
//    private final IDependencyService dependencyService;
//    private final ISpoofaxProcessorRunner processorRunner;
//    private final ILanguageDiscoveryService discoveryService;
//    private final IIntelliJResourceService resourceService;
//    private final LanguageManager languageManager;

    @Inject
    private SpoofaxBuilder() {
        super(Arrays.asList(SpoofaxTargetType.PRODUCTION)); //, SpoofaxTargetType.TESTS));


        //ResourcesBuilder.registerEnabler(module -> module.getModuleType() != JpsSpoofaxModuleType.INSTANCE);
    }

    //@Override
    //public void buildStarted(CompileContext context) {
    //    JavaBuilder.IS_ENABLED.set(context, Boolean.FALSE);
    //}

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax";
    }


    @Override
    public void build(@NotNull SpoofaxTarget target,
                      @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxTarget> holder,
                      @NotNull BuildOutputConsumer consumer,
                      @NotNull CompileContext context) throws ProjectBuildException, IOException {

        //JpsProjectService projectService = new JpsProjectService(target.getModule());

        Injector injector = Guice.createInjector(new SpoofaxJpsDependencyModule(target.getModule()));

        ILanguageService languageService = injector.getInstance(ILanguageService.class);
        ILanguagePathService languagePathService = injector.getInstance(ILanguagePathService.class);
        IProjectService projectService = injector.getInstance(IProjectService.class);
        IDependencyService dependencyService = injector.getInstance(IDependencyService.class);
        SpoofaxProcessorRunner processorRunner = injector.getInstance(SpoofaxProcessorRunner.class);
        ILanguageDiscoveryService discoveryService = injector.getInstance(ILanguageDiscoveryService.class);
        IIntelliJResourceService resourceService = injector.getInstance(IIntelliJResourceService.class);
        LanguageManager languageManager = injector.getInstance(LanguageManager.class);

        //System.out.println(target.getOutputRoots(context));
        //File outputDirectory = getBuildOutputDirectory(target.getModule(), false, compileContext);
        context.processMessage(new ProgressMessage("Compiling Spoofax sources"));
        //buildSpoofax(target.getModule());
        context.checkCanceled();

        languageManager.loadMetaLanguages();

        context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.INFO, "Using these languages: " + Joiner.on(", ").join(languageService.getAllLanguages())));



        FileObject location = resourceService.resolve("file:///home/daniel/repos/spoofax-test-project");
        //IProject project = projectService.get(location);
        IProject project = new SpoofaxProject(location);    // TODO: Get the project
        BuildInput input = getBuildInput(dependencyService, languagePathService, project);

        try {
            ITask<IBuildOutput<IStrategoTerm, IStrategoTerm, IStrategoTerm>> task =
                    processorRunner.build(input, null, null)
                            .schedule().block();



            if (!task.cancelled())
            {
                final IBuildOutput<?, ?, ?> output = task.result();
                if (output != null) {
                    for (IMessage msg : output.allMessages())
                    {
                        displayMessage(msg, context);
                    }
                    for (IMessage msg : output.extraMessages())
                    {
                        displayMessage(msg, context);
                    }
                    if (output.success())
                        context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.INFO, "Compilation finished successfully!"));
                    else
                        context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.INFO, "Compilation finished but failed!"));
                }
                else {
                    context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.WARNING, "Compilation has no output?"));
                }
            }
            else {
                context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.WARNING, "Compilation cancelled!"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(IMessage msg, @NotNull CompileContext context)
    {
        BuildMessage.Kind kind;
        switch (msg.severity())
        {
            case NOTE: kind = BuildMessage.Kind.INFO; break;
            case WARNING: kind = BuildMessage.Kind.WARNING; break;
            case ERROR: kind = BuildMessage.Kind.ERROR; break;
            default:
                throw new UnsupportedOperationException();
        }
        context.processMessage(new CompilerMessage("Spoofax", kind, msg.message()));
    }

    private BuildInput getBuildInput(IDependencyService dependencyService, ILanguagePathService languagePathService, IProject project) {
        BuildInputBuilder inputBuilder = new BuildInputBuilder(project);
        BuildInput input = null;
        try {
            //org.codehaus.plexus.util.xml.pull.MXParser p;

            inputBuilder.withDefaultIncludePaths(true);
            inputBuilder.withSourcesFromDefaultSourceLocations(true);
            inputBuilder.withSelector(new SpoofaxIgnoresSelector());
            inputBuilder.addTransformGoal(new CompileGoal());
            input = inputBuilder.build(dependencyService, languagePathService);
        } catch (MetaborgException e) {
            e.printStackTrace();
        }
        assert input != null;
        return input;
    }

    // https://github.com/pbuda/intellij-pony/blob/52e40c55d56adc4d85a34ad8dffe45ca0c64967f/jps-plugin/src/me/piotrbuda/intellij/pony/jps/PonyBuilder.java
    // https://github.com/RudyChin/platform_tools_adt_idea/blob/ac0e992c63f9962b3b3fb7417a398b641f2bb1dc/android/jps-plugin/src/org/jetbrains/jps/android/AndroidLibraryPackagingBuilder.java
    // https://github.com/pantsbuild/intellij-pants-plugin/blob/6fe84a536b4275358a38487f751fd64d6d5c9163/jps-plugin/com/twitter/intellij/pants/jps/incremental/PantsTargetBuilder.java


}