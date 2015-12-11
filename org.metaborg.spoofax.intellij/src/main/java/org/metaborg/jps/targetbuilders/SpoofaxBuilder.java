/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.jps.targetbuilders;

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
    public abstract void build(
            @NotNull final T target,
            @NotNull final DirtyFilesHolder<SpoofaxSourceRootDescriptor, T> holder,
            @NotNull final BuildOutputConsumer consumer,
            @NotNull final CompileContext context) throws ProjectBuildException, IOException;

}