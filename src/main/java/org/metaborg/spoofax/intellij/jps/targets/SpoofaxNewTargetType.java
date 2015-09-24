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

public abstract class SpoofaxNewTargetType<T extends SpoofaxNewTarget> extends ModuleBasedBuildTargetType<T> {

    protected SpoofaxNewTargetType(String typeId) {
        super(typeId);
    }

    @NotNull
    public abstract T createTarget(IProject project, JpsModule module);

    @NotNull
    @Override
    public final List<T> computeAllTargets(@NotNull JpsModel model) {
        IProject project = null;

        // Default implementation.
        List<T> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
            targets.add(createTarget(project, module));
        }
        return targets;
    }

    @NotNull
    @Override
    public final BuildTargetLoader<T> createLoader(@NotNull JpsModel model) {
        // Default implementation.
        return new BuildTargetLoader<T>() {
            @Nullable
            @Override
            public T createTarget(@NotNull String targetId) {
                for (T target : computeAllTargets(model)) {
                    if (target.getId().equals(targetId))
                        return target;
                }
                return null;
            }
        };
    }

}
