package org.metaborg.spoofax.intellij.jps.builders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;
import org.metaborg.spoofax.intellij.SpoofaxTarget;

import java.io.IOException;
import java.util.List;

/**
 * The Spoofax regular builder.
 */
@Singleton
public final class SpoofaxRegularBuilder implements IBuilder {

    // TODO: Make private
    @Inject
    public SpoofaxRegularBuilder() {

    }

    @NotNull
    @Override
    public String name() {
        return "Spoofax: Regular build";
    }

    @NotNull
    @Override
    public List<IBuilder> dependencies() {
        return null;
    }

    @Override
    public void onStart(@NotNull CompileContext context) {

    }

    @Override
    public void onFinish(@NotNull CompileContext context) {

    }

    @Override
    public void onBuild(@NotNull SpoofaxTarget target, @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxTarget> holder, @NotNull BuildOutputConsumer consumer, @NotNull CompileContext context) {

        context.processMessage(new ProgressMessage("Compiling Spoofax sources"));


        context.processMessage(BuilderUtils.formatMessage(name(), BuildMessage.Kind.ERROR, "Spoofax build works!"));

        throw new RuntimeException("Some exception!");

        //return null;
    }
}
