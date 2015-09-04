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
public class SpoofaxTargetType extends ModuleBasedBuildTargetType<SpoofaxTarget> {

    private final JpsSpoofaxModuleType moduleType;
    private final BuildTargetKind kind;

    /**
     * Initializes a new instance of the {@link SpoofaxTargetType} class.
     * @param typeId The type ID of the build target.
     * @param kind The kind of build target.
     * @param moduleType The Spoofax module type.
     */
    protected SpoofaxTargetType(String typeId, BuildTargetKind kind, JpsSpoofaxModuleType moduleType) {
        super(typeId);

        this.moduleType = moduleType;
        this.kind = kind;
    }

    @NotNull
    @Override
    public List<SpoofaxTarget> computeAllTargets(JpsModel model) {
        List<SpoofaxTarget> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(this.moduleType)) {
            targets.add(new SpoofaxTarget(module, this, this.moduleType));
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
                for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(SpoofaxTargetType.this.moduleType)) {
                    if (module.getName().equals(targetId)) {
                        return new SpoofaxTarget(module, SpoofaxTargetType.this, SpoofaxTargetType.this.moduleType);
                    }
                }
                return null;
            }
        };
    }

    /**
     * Gets the kind of build target.
     * @return A member of the {@link BuildTargetKind} enumeration.
     */
    public BuildTargetKind getKind() {
        return this.kind;
    }
}
