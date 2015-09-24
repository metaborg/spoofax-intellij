package org.metaborg.spoofax.intellij.jps.targets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.ModuleBasedTarget;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public final class SpoofaxNewBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxNewTarget> {

    @SuppressWarnings("unchecked")
    @Inject
    public SpoofaxNewBuilder(Collection<BuildTargetType<?>> targetTypes){
        super((Collection<? extends BuildTargetType<? extends SpoofaxNewTarget>>)(Collection<?>)targetTypes);
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax builder";
    }

    @Override
    public void buildStarted(CompileContext context) {
        List<JpsModule> modules = context.getProjectDescriptor().getProject().getModules();
        System.out.println(modules);
    }

    @Override
    public void build(@NotNull SpoofaxNewTarget target,
                      @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxNewTarget> holder,
                      @NotNull BuildOutputConsumer consumer,
                      @NotNull CompileContext context) throws ProjectBuildException, IOException {

        //JpsProjectService projectService = new JpsProjectService(target.getModule());


        context.processMessage(new ProgressMessage("PRE Compiling Spoofax sources"));
        //buildSpoofax(target.getModule());
        context.checkCanceled();

//        for (IBuildStep step : target.steps()) {
//            step.onBuild();
//        }

    }


}