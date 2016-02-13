/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.jps.builders;

import com.intellij.util.containers.*;
import org.jetbrains.annotations.*;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.builders.storage.*;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.indices.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.java.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.intellij.jps.projects.*;

import java.io.*;
import java.util.*;

/**
 * Base class for Spoofax build target builders.
 */
public abstract class SpoofaxTarget extends ModuleBasedTarget<SpoofaxSourceRootDescriptor> {

    private final MetaborgJpsProject project;

    /**
     * Initializes a new instance of the {@link SpoofaxTarget} class.
     *
     * @param project    The project being built.
     * @param targetType The target type.
     */
    protected SpoofaxTarget(
            final MetaborgJpsProject project,
            final ModuleBasedBuildTargetType<?> targetType) {
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
    public final MetaborgJpsProject project() {
        return this.project;
    }

    /**
     * Gets the ID of the build target.
     *
     * @return The ID.
     */
    @Override
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
    public abstract Collection<BuildTarget<?>> computeDependencies(
            final BuildTargetRegistry buildTargetRegistry,
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
    @Override
    public final List<SpoofaxSourceRootDescriptor> computeRootDescriptors(
            final JpsModel jpsModel,
            final ModuleExcludeIndex moduleExcludeIndex,
            final IgnoredFileIndex ignoredFileIndex,
            final BuildDataPaths buildDataPaths) {
        // Default implementation.
        final List<SpoofaxSourceRootDescriptor> result = new ArrayList<>();
        final JavaSourceRootType type = isTests() ? JavaSourceRootType.TEST_SOURCE : JavaSourceRootType.SOURCE;
        for (final JpsTypedModuleSourceRoot<JavaSourceRootProperties> root : super.myModule.getSourceRoots(type)) {
            result.add(new SpoofaxSourceRootDescriptor(root.getFile(), this));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final SpoofaxSourceRootDescriptor findRootDescriptor(
            final String rootId,
            final BuildRootIndex rootIndex) {
        // Default implementation.
        return ContainerUtil.getFirstItem(rootIndex.getRootDescriptors(
                new File(rootId),
                Collections.singletonList(getSpoofaxTargetType()),
                null
        ));
    }

    /**
     * Gets a presentable name for this build target.
     *
     * @return The presentable name.
     */
    @Override
    public abstract String getPresentableName();

    /**
     * {@inheritDoc}
     */
    @Override
    public final Collection<File> getOutputRoots(final CompileContext compileContext) {
        // Default implementation.
        return ContainerUtil.createMaybeSingletonList(JpsJavaExtensionService.getInstance().getOutputDirectory(
                super.myModule,
                isTests()
        ));
    }

    private SpoofaxTargetType<? extends BuildTarget<SpoofaxSourceRootDescriptor>> getSpoofaxTargetType() {
        // Default implementation.
        return (SpoofaxTargetType<?>)getTargetType();
    }
}
