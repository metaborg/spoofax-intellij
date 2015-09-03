package org.metaborg.spoofax.intellij;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildRootIndex;
import org.jetbrains.jps.builders.BuildTarget;
import org.jetbrains.jps.builders.BuildTargetRegistry;
import org.jetbrains.jps.builders.TargetOutputIndex;
import org.jetbrains.jps.builders.storage.BuildDataPaths;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.indices.IgnoredFileIndex;
import org.jetbrains.jps.indices.ModuleExcludeIndex;
import org.jetbrains.jps.model.JpsModel;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The compilation process of a single Spoofax project.
 */
public class SpoofaxBuildTarget extends BuildTarget<SpoofaxSourceRootDescriptor> {

    /**
     * The ID used for the {@link SpoofaxBuildTarget}.
     */
    public static final String TARGET_ID = "spoofax_build_target";

    public SpoofaxBuildTarget() {
        super(SpoofaxBuildTargetType.INSTANCE);
    }

    @Override
    public String getId() {
        return TARGET_ID;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax Target";
    }

    @Override
    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry targetRegistry, TargetOutputIndex outputIndex) {
        // This build target does not depend on any other targets.
        return Collections.emptyList();
    }

    /**
     * Returns the list of source roots for this target.
     *
     * @param model            the JPS project model.
     * @param index            the index of exclude roots.
     * @param ignoredFileIndex the index of ignore patterns.
     * @param dataPaths        the index of paths that can be used to serialize data related to build targets.
     * @return A list of source roots.
     */
    @NotNull
    @Override
    public List<SpoofaxSourceRootDescriptor> computeRootDescriptors(JpsModel model, ModuleExcludeIndex index, IgnoredFileIndex ignoredFileIndex, BuildDataPaths dataPaths) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public SpoofaxSourceRootDescriptor findRootDescriptor(String rootId, BuildRootIndex rootIndex) {
        // We don't have serialized source roots.
        return null;
    }

    /**
     * Returns the directories where this build target is going to produce its output.
     * @param context The compilation context.
     * @return A collection of directories.
     */
    @NotNull
    @Override
    public Collection<File> getOutputRoots(CompileContext context) {
        // TODO:
        return Collections.emptyList();
        /*
        final Set<File> result = new THashSet<>(FileUtil.FILE_HASHING_STRATEGY);
        final JpsProject jpsProject = context.getProjectDescriptor().getProject();
        processJavaModuleTargets(
                jpsProject,
                target -> {
                    result.addAll(target.getOutputRoots(context));
                    return true;
                }
        );
        return result;
        */
    }

    /*
    private void processJavaModuleTargets(@NotNull JpsProject jpsProject, @NotNull Processor<ModuleBuildTarget> processor) {
        for (JpsModule module : jpsProject.getModules()) {
            for (JavaModuleBuildTargetType buildTargetType : JavaModuleBuildTargetType.ALL_TYPES) {
                final ModuleBuildTarget moduleBuildTarget = new ModuleBuildTarget(module, buildTargetType);
                if (!processor.process(moduleBuildTarget)) {
                    return;
                }
            }
        }
    }
    */

    // https://github.com/pantsbuild/intellij-pants-plugin/blob/6fe84a536b4275358a38487f751fd64d6d5c9163/jps-plugin/com/twitter/intellij/pants/jps/incremental/model/PantsBuildTarget.java
}
