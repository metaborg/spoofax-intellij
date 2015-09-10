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
public final class SpoofaxTargetType extends ModuleBasedBuildTargetType<SpoofaxTarget> {

    public static final SpoofaxTargetType PRODUCTION = new SpoofaxTargetType("spoofax_production", BuildTargetKind.PRODUCTION);
    public static final SpoofaxTargetType TESTS = new SpoofaxTargetType("spoofax_production", BuildTargetKind.TEST);

    private final BuildTargetKind kind;

    public BuildTargetKind kind() { return this.kind; }

    /**
     * Initializes a new instance of the {@link SpoofaxTargetType} class.
     * @param typeId The type ID of the build target.
     */
    protected SpoofaxTargetType(String typeId, BuildTargetKind kind) {
        super(typeId);
        this.kind = kind;
    }

    @NotNull
    @Override
    public List<SpoofaxTarget> computeAllTargets(@NotNull JpsModel model) {
        List<SpoofaxTarget> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
            targets.add(new SpoofaxTarget(module, this));
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
                for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
                    if (module.getName().equals(targetId)) {
                        return new SpoofaxTarget(module, SpoofaxTargetType.this);
                    }
                }
                return null;
            }
        };
    }
}
