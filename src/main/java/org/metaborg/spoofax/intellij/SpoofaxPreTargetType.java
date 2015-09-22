package org.metaborg.spoofax.intellij;

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
public final class SpoofaxPreTargetType extends ModuleBasedBuildTargetType<SpoofaxPreTarget> {

    public static final SpoofaxPreTargetType PRODUCTION = new SpoofaxPreTargetType("spoofax_pre_production", BuildTargetKind.PRODUCTION);
    public static final SpoofaxPreTargetType TESTS = new SpoofaxPreTargetType("spoofax_pre_tests", BuildTargetKind.TEST);

    private final BuildTargetKind kind;

    public BuildTargetKind kind() { return this.kind; }

    /**
     * Initializes a new instance of the {@link SpoofaxPreTargetType} class.
     * @param typeId The type ID of the build target.
     */
    protected SpoofaxPreTargetType(String typeId, BuildTargetKind kind) {
        super(typeId);
        this.kind = kind;
    }

    @NotNull
    @Override
    public List<SpoofaxPreTarget> computeAllTargets(@NotNull JpsModel model) {
        List<SpoofaxPreTarget> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
            targets.add(new SpoofaxPreTarget(module, this));
        }
        return targets;
    }

    @NotNull
    @Override
    public BuildTargetLoader<SpoofaxPreTarget> createLoader(@NotNull JpsModel model) {
        return new BuildTargetLoader<SpoofaxPreTarget>() {
            @Nullable
            @Override
            public SpoofaxPreTarget createTarget(@NotNull String targetId) {
                for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
                    if (module.getName().equals(targetId)) {
                        return new SpoofaxPreTarget(module, SpoofaxPreTargetType.this);
                    }
                }
                return null;
            }
        };
    }
}
