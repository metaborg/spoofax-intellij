package org.metaborg.spoofax.intellij.jps.targets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsTypedModule;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;

import java.util.ArrayList;
import java.util.List;

public final class SpoofaxNewPostTargetType extends ModuleBasedBuildTargetType<SpoofaxNewPostTarget> {

    public static final SpoofaxNewPostTargetType INSTANCE = new SpoofaxNewPostTargetType();   // TODO: Refactor

    public SpoofaxNewPostTargetType() {
        super("spoofax-post-production");
    }

    @NotNull
    public SpoofaxNewPostTarget createTarget(IProject project, JpsTypedModule<JpsDummyElement> module) {
        return new SpoofaxNewPostTarget(this, module);
    }

    @NotNull
    @Override
    public final List<SpoofaxNewPostTarget> computeAllTargets(@NotNull JpsModel model) {
        IProject project = null;

        // Default implementation.
        List<SpoofaxNewPostTarget> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
            targets.add(createTarget(project, module));
        }
        return targets;
    }

    @NotNull
    @Override
    public final BuildTargetLoader<SpoofaxNewPostTarget> createLoader(@NotNull JpsModel model) {
        // Default implementation.
        return new BuildTargetLoader<SpoofaxNewPostTarget>() {
            @Nullable
            @Override
            public SpoofaxNewPostTarget createTarget(@NotNull String targetId) {
                for (SpoofaxNewPostTarget target : computeAllTargets(model)) {
                    if (target.getId().equals(targetId))
                        return target;
                }
                return null;
            }
        };
    }

}
