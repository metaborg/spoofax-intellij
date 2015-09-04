package org.metaborg.spoofax.intellij;

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
import org.jetbrains.jps.model.java.JpsJavaClasspathKind;
import org.jetbrains.jps.model.java.JpsJavaExtensionService;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsTypedModuleSourceRoot;

import java.io.File;
import java.util.*;

/**
 * A Spoofax module build target.
 */
public class SpoofaxTarget extends ModuleBasedTarget<SpoofaxSourceRootDescriptor> {

    private final JpsSpoofaxModuleType moduleType;

    /**
     * Initializes a new instance of the {@link SpoofaxTarget} class.
     * @param module The JPS module to which this target applies.
     * @param targetType The target type.
     */
    public SpoofaxTarget(
            @NotNull final JpsModule module,
            @NotNull final SpoofaxTargetType targetType,
            @NotNull final JpsSpoofaxModuleType moduleType) {
        super(targetType, module);
        this.moduleType = moduleType;
    }

    @Override
    public boolean isTests() {
        return getSpoofaxTargetType().getKind() == BuildTargetKind.TEST;
    }

    @Override
    public String getId() {
        return super.myModule.getName();
    }

    @Override
    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry targetRegistry, TargetOutputIndex outputIndex) {
        final List<BuildTarget<?>> dependencies = new ArrayList<>();
        final Set<JpsModule> modules = JpsJavaExtensionService
                .dependencies(super.myModule)
                .includedIn(JpsJavaClasspathKind.compile(isTests()))
                .getModules();
        for (JpsModule module : modules) {
            if (module.getModuleType().equals(this.moduleType)) {
                dependencies.add(new SpoofaxTarget(module, getSpoofaxTargetType(), this.moduleType));
            }
        }
        return dependencies;
    }

    private SpoofaxTargetType getSpoofaxTargetType(){
        return (SpoofaxTargetType)getTargetType();
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
        return "Spoofax";
    }

    @NotNull
    @Override
    public Collection<File> getOutputRoots(CompileContext context) {
        return ContainerUtil.createMaybeSingletonList(JpsJavaExtensionService.getInstance().getOutputDirectory(super.myModule, isTests()));
    }
}
