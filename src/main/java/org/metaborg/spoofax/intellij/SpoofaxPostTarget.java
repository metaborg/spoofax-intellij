package org.metaborg.spoofax.intellij;

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
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepProvider;

import java.io.File;
import java.util.*;

/**
 * A Spoofax module build target.
 */
public class SpoofaxPostTarget extends SpoofaxTarget {

    private final SpoofaxPreTarget preTarget;

    public SpoofaxPostTarget(
            @NotNull final SpoofaxPreTargetType preTargetType,
            @NotNull final IProject spoofaxModule,
            @NotNull final List<IBuildStep> steps,
            @NotNull final JpsTypedModule<JpsDummyElement> jpsModule,
            @NotNull final SpoofaxPostTargetType targetType) {
        super(spoofaxModule, steps, jpsModule, targetType);
        this.preTarget = preTargetType.createTarget(spoofaxModule, jpsModule);
    }

    @Override
    public boolean isCompiledBeforeModuleLevelBuilders() {
        return false;
    }

//    @Override
//    public boolean isTests() {
//        return getSpoofaxTargetType().kind() == BuildTargetKind.TEST;
//    }

//    @Override
//    public String getId() {
//        return super.myModule.getName();
//    }

    @Override
    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry targetRegistry, TargetOutputIndex outputIndex) {
        final List<BuildTarget<?>> dependencies = new ArrayList<>();
//        final Set<JpsModule> modules = JpsJavaExtensionService
//                .dependencies(super.myModule)
//                .includedIn(JpsJavaClasspathKind.compile(isTests()))
//                .getModules();
//        for (JpsModule module : modules) {
//            if (module.getModuleType().equals(JpsSpoofaxModuleType.INSTANCE)) {
//                dependencies.add(new SpoofaxPostTarget(module, getSpoofaxTargetType()));
//            }
//        }

        dependencies.add(this.preTarget);
        //dependencies.add(new ModuleBuildTarget(super.myModule, JavaModuleBuildTargetType.PRODUCTION));

        return dependencies;
    }

//    private SpoofaxPostTargetType getSpoofaxTargetType(){
//        return (SpoofaxPostTargetType)getTargetType();
//    }

    @Override
    protected SpoofaxPostTargetType getSpoofaxTargetType() {
        return (SpoofaxPostTargetType)getTargetType();
    }

    @NotNull
    @Override
    public List<SpoofaxSourceRootDescriptor> computeRootDescriptors(JpsModel model, ModuleExcludeIndex index, IgnoredFileIndex ignoredFileIndex, BuildDataPaths dataPaths) {
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
        return ContainerUtil.getFirstItem(rootIndex.getRootDescriptors(new File(rootId), Collections.singletonList(getSpoofaxTargetType()), null));
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax POST '" + super.myModule.getName() + "'";
    }

    @NotNull
    @Override
    public Collection<File> getOutputRoots(CompileContext context) {
        return ContainerUtil.createMaybeSingletonList(JpsJavaExtensionService.getInstance().getOutputDirectory(super.myModule, isTests()));
    }
}
