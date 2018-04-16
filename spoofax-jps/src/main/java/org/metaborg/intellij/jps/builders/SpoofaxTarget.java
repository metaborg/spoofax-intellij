/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.jps.builders;

import com.intellij.util.containers.ContainerUtil;
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
import org.metaborg.intellij.jps.projects.MetaborgJpsProject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        super(targetType, project.getModule());
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
