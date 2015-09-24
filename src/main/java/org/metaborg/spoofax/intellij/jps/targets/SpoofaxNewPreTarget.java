package org.metaborg.spoofax.intellij.jps.targets;

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
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsTypedModuleSourceRoot;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class SpoofaxNewPreTarget extends SpoofaxNewTarget {

    public SpoofaxNewPreTarget(SpoofaxNewPreTargetType targetType, @NotNull JpsModule module) {
        super(targetType, module);
    }
//
//    @Override
//    public boolean isTests() {
//        // Default implementation.
//        return false;
//    }
//
//    @Override
//    public String getId() {
//        // Default implementation.
//        return super.myModule.getName();
//    }

    @Override
    public boolean isCompiledBeforeModuleLevelBuilders() {
        return true;
    }
//
//    public SpoofaxNewPreTargetType getSpoofaxTargetType() {
//        // Default implementation.
//        return (SpoofaxNewPreTargetType)getTargetType();
//    }

    @Override
    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry buildTargetRegistry, TargetOutputIndex targetOutputIndex) {
        return Collections.emptyList();
    }
//
//    @NotNull
//    @Override
//    public List<SpoofaxSourceRootDescriptor> computeRootDescriptors(JpsModel jpsModel, ModuleExcludeIndex moduleExcludeIndex, IgnoredFileIndex ignoredFileIndex, BuildDataPaths buildDataPaths) {
//        // Default implementation.
//        List<SpoofaxSourceRootDescriptor> result = new ArrayList<>();
//        JavaSourceRootType type = isTests() ? JavaSourceRootType.TEST_SOURCE : JavaSourceRootType.SOURCE;
//        for (JpsTypedModuleSourceRoot<JavaSourceRootProperties> root : super.myModule.getSourceRoots(type)) {
//            result.add(new SpoofaxSourceRootDescriptor(root.getFile(), this));
//        }
//        return result;
//    }
//
//    @Nullable
//    @Override
//    public SpoofaxSourceRootDescriptor findRootDescriptor(String rootId, BuildRootIndex rootIndex) {
//        // Default implementation.
//        return ContainerUtil.getFirstItem(rootIndex.getRootDescriptors(new File(rootId), Collections.singletonList(getSpoofaxTargetType()), null));
//    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax PRE target 2 '" + getId() + "'";
    }

//    @NotNull
//    @Override
//    public Collection<File> getOutputRoots(CompileContext compileContext) {
//        // Default implementation.
//        return ContainerUtil.createMaybeSingletonList(JpsJavaExtensionService.getInstance().getOutputDirectory(super.myModule, isTests()));
//    }
}
