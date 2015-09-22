package org.metaborg.spoofax.intellij.jps.builders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;
import org.metaborg.spoofax.intellij.SpoofaxTarget;

import java.util.Collections;
import java.util.List;

public abstract class JpsBuilderBase implements IBuilder {



    @NotNull
    @Override
    public abstract String name();

    @NotNull
    @Override
    public List<IBuilder> dependencies() {
        return Collections.emptyList();
    }

    @Override
    public void onStart(@NotNull CompileContext context) {
        // Noting to do.
    }

    @Override
    public void onFinish(@NotNull CompileContext context) {
        // Noting to do.
    }

    @Override
    public void onBuild(@NotNull SpoofaxTarget target, @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxTarget> holder, @NotNull BuildOutputConsumer consumer, @NotNull CompileContext context) {
        // Noting to do.
    }

}
