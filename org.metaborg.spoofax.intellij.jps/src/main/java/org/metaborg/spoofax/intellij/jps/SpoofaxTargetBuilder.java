package org.metaborg.spoofax.intellij.jps;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.incremental.java.JavaBuilder;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.metaborg.spoofax.intellij.SpoofaxBuildTarget;
import org.metaborg.spoofax.intellij.SpoofaxBuildTargetType;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;

import java.io.IOException;
import java.util.Collections;

public class SpoofaxTargetBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxBuildTarget> {

    protected SpoofaxTargetBuilder() {
        super(Collections.singletonList(SpoofaxBuildTargetType.INSTANCE));
    }

    @Override
    public void buildStarted(CompileContext context) {
        JavaBuilder.IS_ENABLED.set(context, Boolean.FALSE);
    }

    @Override
    public void build(SpoofaxBuildTarget target, DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxBuildTarget> holder, BuildOutputConsumer outputConsumer, CompileContext context) throws ProjectBuildException, IOException {
        context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.WARNING, "Compilation not implemented!"));
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax Target Builder";
    }

    // https://github.com/RudyChin/platform_tools_adt_idea/blob/ac0e992c63f9962b3b3fb7417a398b641f2bb1dc/android/jps-plugin/src/org/jetbrains/jps/android/AndroidLibraryPackagingBuilder.java
    // https://github.com/pantsbuild/intellij-pants-plugin/blob/6fe84a536b4275358a38487f751fd64d6d5c9163/jps-plugin/com/twitter/intellij/pants/jps/incremental/PantsTargetBuilder.java
}