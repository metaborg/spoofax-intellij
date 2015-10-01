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

    @NotNull private final SpoofaxJpsProject project;
    /**
     * Gets the project that is being built.
     * @return The project.
     */
    @NotNull public final SpoofaxJpsProject project() {
        return this.project;
    }

    /**
     * Initializes a new instance of the {@link SpoofaxTarget} class.
     * @param project The project being built.
     * @param targetType The target type.
     */
    protected SpoofaxTarget(@NotNull final SpoofaxJpsProject project, @NotNull final ModuleBasedBuildTargetType<?> targetType) {
        super(targetType, project.module());
        this.project = project;
    }

    @Override
    public final boolean isTests() {
        // Default implementation.
        return false;
    }

    @NotNull
    private final SpoofaxTargetType<?> getSpoofaxTargetType() {
        // Default implementation.
        return (SpoofaxTargetType<?>)getTargetType();
    }

    @Override
    @NotNull
    public final String getId() {
        // Default implementation.
        return super.myModule.getName();
    }

    @NotNull
    @Override
    public final List<SpoofaxSourceRootDescriptor> computeRootDescriptors(@NotNull final JpsModel jpsModel, @NotNull final ModuleExcludeIndex moduleExcludeIndex, @NotNull final IgnoredFileIndex ignoredFileIndex, @NotNull final BuildDataPaths buildDataPaths) {
        // Default implementation.
        List<SpoofaxSourceRootDescriptor> result = new ArrayList<>();
        JavaSourceRootType type = isTests() ? JavaSourceRootType.TEST_SOURCE : JavaSourceRootType.SOURCE;
        for (JpsTypedModuleSourceRoot<JavaSourceRootProperties> root : super.myModule.getSourceRoots(type)) {
            result.add(new SpoofaxSourceRootDescriptor(root.getFile(), this));
        }
        return result;
    }

    @Nullable
    @Override
    public final SpoofaxSourceRootDescriptor findRootDescriptor(@NotNull final String rootId, @NotNull final BuildRootIndex rootIndex) {
        // Default implementation.
        return ContainerUtil.getFirstItem(rootIndex.getRootDescriptors(new File(rootId), Collections.singletonList(getSpoofaxTargetType()), null));
    }

    @NotNull
    @Override
    public final Collection<File> getOutputRoots(@NotNull final CompileContext compileContext) {
        // Default implementation.
        return ContainerUtil.createMaybeSingletonList(JpsJavaExtensionService.getInstance().getOutputDirectory(super.myModule, isTests()));
    }
}
