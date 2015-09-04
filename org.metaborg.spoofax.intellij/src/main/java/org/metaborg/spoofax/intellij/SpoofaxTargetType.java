package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsTypedModule;

import java.util.ArrayList;
import java.util.List;

/**
 * The Spoofax build target type.
 */
@Singleton
public class SpoofaxTargetType extends ModuleBasedBuildTargetType<SpoofaxTarget> {

    public static final String TARGET_TYPE_ID = "spoofax_production";

    private final JpsSpoofaxModuleType spoofaxModuleType;

    @Inject
    private SpoofaxTargetType(JpsSpoofaxModuleType spoofaxModuleType) {
        // TODO: Distinguish between the Production and Test target, by making `typeId` a parameter.
        // Or, equivalently, make Production and Test two different classes.
        // See: https://github.com/pbuda/intellij-pony/blob/52e40c55d56adc4d85a34ad8dffe45ca0c64967f/jps-plugin/src/me/piotrbuda/intellij/pony/jps/PonyTargetType.java
        super(TARGET_TYPE_ID);

        this.spoofaxModuleType = spoofaxModuleType;
    }

    @NotNull
    @Override
    public List<SpoofaxTarget> computeAllTargets(JpsModel model) {
        List<SpoofaxTarget> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(this.spoofaxModuleType)) {
            targets.add(new SpoofaxTarget(module, this, this.spoofaxModuleType));
        }
        return targets;
    }

    @NotNull
    @Override
    public BuildTargetLoader<SpoofaxTarget> createLoader(@NotNull JpsModel model) {
        return new BuildTargetLoader<SpoofaxTarget>() {
            @Nullable
            @Override
            public SpoofaxTarget createTarget(@NotNull String targetId) {
                for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(SpoofaxTargetType.this.spoofaxModuleType)) {
                    if (module.getName().equals(targetId)) {
                        return new SpoofaxTarget(module, SpoofaxTargetType.this, SpoofaxTargetType.this.spoofaxModuleType);
                    }
                }
                return null;
            }
        };
    }
}
