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

package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.builders.storage.BuildDataPaths;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.indices.IgnoredFileIndex;
import org.jetbrains.jps.indices.ModuleExcludeIndex;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.java.JavaSourceRootProperties;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.java.JpsJavaExtensionService;
import org.jetbrains.jps.model.module.JpsTypedModuleSourceRoot;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base class for Spoofax build targetbuilders.
 */
public abstract class SpoofaxTarget extends ModuleBasedTarget<SpoofaxSourceRootDescriptor> {

    @NotNull
    private final SpoofaxJpsProject project;

    /**
     * Initializes a new instance of the {@link SpoofaxTarget} class.
     *
     * @param project    The project being built.
     * @param targetType The target type.
     */
    protected SpoofaxTarget(@NotNull final SpoofaxJpsProject project,
                            @NotNull final ModuleBasedBuildTargetType<?> targetType) {
        super(targetType, project.module());
        this.project = project;
    }

    /**
     * Whether this target is compiled before the Java build target.
     *
     * @return <code>true</code> to compile this build target before the Java build target;
     * otherwise, <code>false</code> to compile this build target after the Java build target.
     */
    @Override
    public abstract boolean isCompiledBeforeModuleLevelBuilders();

    /**
     * Gets whether this build target is a testing target.
     *
     * @return <code>true</code> when it is a testing target;
     * otherwise, <code>false</code>.
     */
    @Override
    public final boolean isTests() {
        // Default implementation.
        return false;
    }

    /**
     * Gets the project that is being built.
     *
     * @return The project.
     */
    @NotNull
    public final SpoofaxJpsProject project() {
        return this.project;
    }

    /**
     * Gets the ID of the build target.
     *
     * @return The ID.
     */
    @Override
    @NotNull
    public final String getId() {
        // Default implementation.
        return super.myModule.getName();
    }

    /**
     * Computes the list of build targets on which this build target depends.
     *
     * @param buildTargetRegistry The build target registry.
     * @param targetOutputIndex   The target output index.
     * @return A collection of build targets.
     */
    @Override
    public abstract Collection<BuildTarget<?>> computeDependencies(final BuildTargetRegistry buildTargetRegistry,
                                                                   final TargetOutputIndex targetOutputIndex);

    /**
     * Computes the root descriptors for the build target.
     *
     * @param jpsModel           The JPS model.
     * @param moduleExcludeIndex The module exclude index.
     * @param ignoredFileIndex   The ignored file index.
     * @param buildDataPaths     The build data paths.
     * @return A list of source root descriptors.
     */
    @NotNull
    @Override
    public final List<SpoofaxSourceRootDescriptor> computeRootDescriptors(@NotNull final JpsModel jpsModel,
                                                                          @NotNull final ModuleExcludeIndex moduleExcludeIndex,
                                                                          @NotNull final IgnoredFileIndex ignoredFileIndex,
                                                                          @NotNull final BuildDataPaths buildDataPaths) {
        // Default implementation.
        List<SpoofaxSourceRootDescriptor> result = new ArrayList<>();
        JavaSourceRootType type = isTests() ? JavaSourceRootType.TEST_SOURCE : JavaSourceRootType.SOURCE;
        for (JpsTypedModuleSourceRoot<JavaSourceRootProperties> root : super.myModule.getSourceRoots(type)) {
            result.add(new SpoofaxSourceRootDescriptor(root.getFile(), this));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final SpoofaxSourceRootDescriptor findRootDescriptor(@NotNull final String rootId,
                                                                @NotNull final BuildRootIndex rootIndex) {
        // Default implementation.
        return ContainerUtil.getFirstItem(rootIndex.getRootDescriptors(new File(rootId),
                                                                       Collections.singletonList(getSpoofaxTargetType()),
                                                                       null));
    }

    /**
     * Gets a presentable name for this build target.
     *
     * @return The presentable name.
     */
    @NotNull
    @Override
    public abstract String getPresentableName();

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final Collection<File> getOutputRoots(@NotNull final CompileContext compileContext) {
        // Default implementation.
        return ContainerUtil.createMaybeSingletonList(JpsJavaExtensionService.getInstance().getOutputDirectory(super.myModule,
                                                                                                               isTests()));
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    private final SpoofaxTargetType<?> getSpoofaxTargetType() {
        // Default implementation.
        return (SpoofaxTargetType<?>) getTargetType();
    }
}
