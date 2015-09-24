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
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.java.JavaSourceRootProperties;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.java.JpsJavaExtensionService;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsTypedModuleSourceRoot;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepDescriptor;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base class for a Spoofax module build target.
 */
public abstract class SpoofaxTarget extends ModuleBasedTarget<SpoofaxSourceRootDescriptor> {

    private final IProject spoofaxModule;
    private final List<IBuildStep> steps;

    public IProject spoofaxModule() {
        return this.spoofaxModule;
    }
    public List<IBuildStep> steps() { return this.steps; }

    /**
     * Initializes a new instance of the {@link SpoofaxTarget} class.
     * @param spoofaxModule The Spoofax module.
     * @param steps The build steps.
     * @param jpsModule The JPS module to which this target applies.
     * @param targetType The target type.
     */
    protected SpoofaxTarget(
            @NotNull final IProject spoofaxModule,
            @NotNull final List<IBuildStep> steps,
            @NotNull final JpsModule jpsModule,
            @NotNull final SpoofaxTargetType targetType) {
        super(targetType, jpsModule);
        this.spoofaxModule = spoofaxModule;
        this.steps = steps;
    }

    @Override
    public final boolean isTests() {
        return false;
    }


    @Override
    public final String getId() {
        return super.myModule.getName();
    }
//    @Override
//    public boolean isCompiledBeforeModuleLevelBuilders() {
//        return true;
//    }

//    @Override
//    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry targetRegistry, TargetOutputIndex outputIndex) {
//        final List<BuildTarget<?>> dependencies = new ArrayList<>();
//        dependencies.add(new ModuleBuildTarget(super.myModule, JavaModuleBuildTargetType.PRODUCTION));
//        final Set<JpsModule> modules = JpsJavaExtensionService
//                .dependencies(super.myModule)
//                .includedIn(JpsJavaClasspathKind.compile(isTests()))
//                .getModules();
//        for (JpsModule module : modules) {
//            if (module.getModuleType().equals(JpsSpoofaxModuleType.INSTANCE)) {
//                dependencies.add(new SpoofaxNewPreTarget(module, getSpoofaxTargetType()));
//            }
//        }
//
//        return dependencies;
//    }

    protected abstract BuildTargetType<? extends BuildTarget<SpoofaxSourceRootDescriptor>> getSpoofaxTargetType();

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
        return getId(); //"Spoofax '" + super.myModule.getName() + "'"; }
    }

    @NotNull
    @Override
    public Collection<File> getOutputRoots(CompileContext context) {
        return ContainerUtil.createMaybeSingletonList(JpsJavaExtensionService.getInstance().getOutputDirectory(super.myModule, isTests()));
    }
}
