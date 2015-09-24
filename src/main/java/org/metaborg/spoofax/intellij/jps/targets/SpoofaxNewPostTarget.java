package org.metaborg.spoofax.intellij.jps.targets;

import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.builders.java.JavaModuleBuildTargetType;
import org.jetbrains.jps.builders.storage.BuildDataPaths;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ModuleBuildTarget;
import org.jetbrains.jps.indices.IgnoredFileIndex;
import org.jetbrains.jps.indices.ModuleExcludeIndex;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.java.JavaSourceRootProperties;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.java.JpsJavaExtensionService;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsTypedModule;
import org.jetbrains.jps.model.module.JpsTypedModuleSourceRoot;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class SpoofaxNewPostTarget extends ModuleBasedTarget<SpoofaxSourceRootDescriptor> {

    public SpoofaxNewPostTarget(SpoofaxNewPostTargetType targetType, @NotNull JpsModule module) {
        super(targetType, module);
    }

    @Override
    public boolean isTests() {
        // Default implementation.
        return false;
    }

    @Override
    public String getId() {
        // Default implementation.
        return super.myModule.getName();
    }

    @Override
    public boolean isCompiledBeforeModuleLevelBuilders() {
        return false;
    }

    public SpoofaxNewPostTargetType getSpoofaxTargetType() {
        // Default implementation.
        return (SpoofaxNewPostTargetType)getTargetType();
    }

    @Override
    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry buildTargetRegistry, TargetOutputIndex targetOutputIndex) {
        final List<BuildTarget<?>> dependencies = new ArrayList<>();
        dependencies.add(new ModuleBuildTarget(super.myModule, JavaModuleBuildTargetType.PRODUCTION));
        SpoofaxNewPreTargetType tt = SpoofaxNewPreTargetType.INSTANCE; // TODO: Refactor
        dependencies.add(tt.createTarget(null, super.myModule));
        return dependencies;
    }

    @NotNull
    @Override
    public List<SpoofaxSourceRootDescriptor> computeRootDescriptors(JpsModel jpsModel, ModuleExcludeIndex moduleExcludeIndex, IgnoredFileIndex ignoredFileIndex, BuildDataPaths buildDataPaths) {
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
    public SpoofaxSourceRootDescriptor findRootDescriptor(String rootId, BuildRootIndex rootIndex) {
        // Default implementation.
        return ContainerUtil.getFirstItem(rootIndex.getRootDescriptors(new File(rootId), Collections.singletonList(getSpoofaxTargetType()), null));
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax POST target 2 '" + getId() + "'";
    }

    @NotNull
    @Override
    public Collection<File> getOutputRoots(CompileContext compileContext) {
        // Default implementation.
        return ContainerUtil.createMaybeSingletonList(JpsJavaExtensionService.getInstance().getOutputDirectory(super.myModule, isTests()));
    }
}
