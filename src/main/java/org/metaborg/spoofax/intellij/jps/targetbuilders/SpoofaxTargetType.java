package org.metaborg.spoofax.intellij.jps.targetbuilders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsTypedModule;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.util.ArrayList;
import java.util.List;

public abstract class SpoofaxTargetType<T extends SpoofaxTarget> extends ModuleBasedBuildTargetType<T> {

    @NotNull private final IJpsProjectService projectService;

    protected SpoofaxTargetType(@NotNull final String typeId, @NotNull final IJpsProjectService projectService) {
        super(typeId);
        this.projectService = projectService;
    }

    @NotNull
    public abstract T createTarget(@NotNull SpoofaxJpsProject project);

    @NotNull
    public final T createTarget(@NotNull final JpsModule module) {
        SpoofaxJpsProject project = projectService.get(module);
        if (project == null)
            project = projectService.create(module);
        return createTarget(project);
    }

    @NotNull
    @Override
    public final List<T> computeAllTargets(@NotNull final JpsModel model) {
        // Default implementation.
        final List<T> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
            targets.add(createTarget(module));
        }
        return targets;
    }

    @NotNull
    @Override
    public final BuildTargetLoader<T> createLoader(@NotNull final JpsModel model) {
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
