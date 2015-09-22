package org.metaborg.spoofax.intellij.jps.builders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;
import org.metaborg.spoofax.intellij.SpoofaxTarget;

import java.util.List;

/**
 * A builder.
 */
public interface IBuilder {

    /**
     * Gets the name of the builder.
     * @return The human-readable name of the builder.
     */
    @NotNull String name();

    /**
     * Gets the builders on which this builder depends.
     * @return A list of builders.
     */
    @NotNull List<IBuilder> dependencies();

    /**
     * Called when the build starts.
     * @param context The compile context.
     */
    void onStart(@NotNull CompileContext context);

    /**
     * Called when the build finishes.
     * @param context The compile context.
     */
    void onFinish(@NotNull CompileContext context);

    /**
     * Called when building.
     * @param target The target.
     * @param holder The holder.
     * @param consumer The consumer.
     * @param context The compile context.
     */
    void onBuild(@NotNull SpoofaxTarget target,
                 @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxTarget> holder,
                 @NotNull BuildOutputConsumer consumer,
                 @NotNull CompileContext context);
}
