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
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.build.BuildInput;
import org.metaborg.core.build.BuildInputBuilder;
import org.metaborg.core.build.IBuildOutput;
import org.metaborg.core.build.dependency.IDependencyService;
import org.metaborg.core.build.paths.ILanguagePathService;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.processing.ITask;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.transform.CompileGoal;
import org.metaborg.spoofax.core.processing.SpoofaxProcessorRunner;
import org.metaborg.spoofax.core.resource.SpoofaxIgnoresSelector;
import org.metaborg.spoofax.intellij.*;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxProject;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import java.io.IOException;
import java.util.Arrays;

/**
 * Builds the Spoofax build target.
 */
@Singleton
public final class SpoofaxPreBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> {

    public static final SpoofaxPreBuilder INSTANCE = new SpoofaxPreBuilder();

//    private final ILanguageService languageService;
//    private final ILanguagePathService languagePathService;
//    //private final IProjectService projectService;
//    private final IDependencyService dependencyService;
//    private final ISpoofaxProcessorRunner processorRunner;
//    private final ILanguageDiscoveryService discoveryService;
//    private final IIntelliJResourceService resourceService;
//    private final LanguageManager languageManager;

    @Inject
    private SpoofaxPreBuilder() {
        super(Arrays.asList(SpoofaxPreTargetType.PRODUCTION)); //, SpoofaxTargetType.TESTS));


        //ResourcesBuilder.registerEnabler(module -> module.getModuleType() != JpsSpoofaxModuleType.INSTANCE);
    }


//@Override
    //public void buildStarted(CompileContext context) {
    //    JavaBuilder.IS_ENABLED.set(context, Boolean.FALSE);
    //}

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax Pre";
    }


    @Override
    public void build(@NotNull SpoofaxPreTarget target,
                      @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> holder,
                      @NotNull BuildOutputConsumer consumer,
                      @NotNull CompileContext context) throws ProjectBuildException, IOException {

        //JpsProjectService projectService = new JpsProjectService(target.getModule());


        context.processMessage(new ProgressMessage("PRE Compiling Spoofax sources"));
        //buildSpoofax(target.getModule());
        context.checkCanceled();

    }


}