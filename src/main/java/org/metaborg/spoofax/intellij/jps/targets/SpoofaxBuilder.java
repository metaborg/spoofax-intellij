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
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.metaborg.spoofax.intellij.*;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;

import java.io.IOException;
import java.util.*;

public abstract class SpoofaxBuilder<T extends ModuleBasedTarget<SpoofaxSourceRootDescriptor>> extends TargetBuilder<SpoofaxSourceRootDescriptor, T> {

    //public static final SpoofaxBuilder INSTANCE = new SpoofaxBuilder();


    protected SpoofaxBuilder(BuildTargetType<? extends T> targetType) {
        super(Collections.singletonList(targetType));
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax Pre";
    }

    @Override
    public void buildStarted(CompileContext context) {
        List<JpsModule> modules = context.getProjectDescriptor().getProject().getModules();
        System.out.println(modules);
    }

    @Override
    public void build(@NotNull T target,
                      @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, T> holder,
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