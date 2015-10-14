package org.metaborg.spoofax.intellij.jps.targetbuilders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;

import java.io.IOException;
import java.util.Collections;

/**
 * Spoofax builder.
 */
public abstract class SpoofaxBuilder<T extends SpoofaxTarget> extends TargetBuilder<SpoofaxSourceRootDescriptor, T> {

    /**
     * Initializes a new instance of the {@link SpoofaxBuilder} class.
     *
     * @param targetType The target type.
     */
    protected SpoofaxBuilder(@NotNull final BuildTargetType<T> targetType) {
        super(Collections.singletonList(targetType));
    }

    /**
     * Gets the presentable name of the builder.
     *
     * @return The name.
     */
    @NotNull
    @Override
    public abstract String getPresentableName();

    /**
     * Builds the build target.
     *
     * @param target   The build target.
     * @param holder   The dirty files holder.
     * @param consumer The build output consumer.
     * @param context  The compile context.
     * @throws ProjectBuildException
     * @throws IOException
     */
    @Override
    public abstract void build(@NotNull final T target,
                               @NotNull final DirtyFilesHolder<SpoofaxSourceRootDescriptor, T> holder,
                               @NotNull final BuildOutputConsumer consumer,
                               @NotNull final CompileContext context) throws ProjectBuildException, IOException;

}