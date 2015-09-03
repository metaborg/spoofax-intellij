package org.metaborg.spoofax.intellij;

import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.model.JpsModel;

import java.util.List;

public class SpoofaxBuildTargetType extends BuildTargetType<SpoofaxBuildTarget> {

    /**
     * The ID used for the {@link SpoofaxBuildTargetType}.
     */
    public static final String TARGET_TYPE_ID = "spoofax_build_target_type";

    public static SpoofaxBuildTargetType INSTANCE = new SpoofaxBuildTargetType();

    protected SpoofaxBuildTargetType() {
        super(TARGET_TYPE_ID);
    }

    /**
     * Gets the build target for the specified JPS model.
     * @param model The JPS model.
     * @return The build target; or null when the Spoofax build target does
     * not apply to the JPS model.
     */
    @Nullable
    public SpoofaxBuildTarget getBuildTarget(JpsModel model)
    {
        return new SpoofaxBuildTarget();
    }

    @NotNull
    @Override
    public List<SpoofaxBuildTarget> computeAllTargets(JpsModel model) {
        // There is at most one target.
        SpoofaxBuildTarget target = getBuildTarget(model);
        return ContainerUtil.createMaybeSingletonList(target);
    }

    @NotNull
    @Override
    public BuildTargetLoader<SpoofaxBuildTarget> createLoader(@NotNull final JpsModel model) {
        return new BuildTargetLoader<SpoofaxBuildTarget>() {
            @Nullable
            @Override
            public SpoofaxBuildTarget createTarget(@NotNull String targetId) {
                return getBuildTarget(model);
            }
        };
    }

    // https://github.com/pantsbuild/intellij-pants-plugin/blob/6fe84a536b4275358a38487f751fd64d6d5c9163/jps-plugin/com/twitter/intellij/pants/jps/incremental/model/PantsBuildTargetType.java
}
