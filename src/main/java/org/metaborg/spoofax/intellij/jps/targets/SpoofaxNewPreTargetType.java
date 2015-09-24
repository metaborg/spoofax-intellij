package org.metaborg.spoofax.intellij.jps.targets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsTypedModule;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;

import java.util.ArrayList;
import java.util.List;

public final class SpoofaxNewPreTargetType extends ModuleBasedBuildTargetType<SpoofaxNewPreTarget> {

    public static final SpoofaxNewPreTargetType INSTANCE = new SpoofaxNewPreTargetType();   // TODO: Refactor

    public SpoofaxNewPreTargetType() {
        super("spoofax-pre-production");
    }

    @NotNull
    public SpoofaxNewPreTarget createTarget(IProject project, JpsModule module) {
        return new SpoofaxNewPreTarget(this, module);
    }

    @NotNull
    @Override
    public final List<SpoofaxNewPreTarget> computeAllTargets(@NotNull JpsModel model) {
        IProject project = null;

        // Default implementation.
        List<SpoofaxNewPreTarget> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
            targets.add(createTarget(project, module));
        }
        return targets;
    }

    @NotNull
    @Override
    public final BuildTargetLoader<SpoofaxNewPreTarget> createLoader(@NotNull JpsModel model) {
        // Default implementation.
        return new BuildTargetLoader<SpoofaxNewPreTarget>() {
            @Nullable
            @Override
            public SpoofaxNewPreTarget createTarget(@NotNull String targetId) {
                for (SpoofaxNewPreTarget target : computeAllTargets(model)) {
                    if (target.getId().equals(targetId))
                        return target;
                }
                return null;
            }
        };
    }

}
