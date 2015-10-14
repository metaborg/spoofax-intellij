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

/**
 * The spoofax build target type, which is a module-based build target.
 *
 * @param <T> The associated type of build target.
 */
public abstract class SpoofaxTargetType<T extends SpoofaxTarget> extends ModuleBasedBuildTargetType<T> {

    @NotNull
    private final IJpsProjectService projectService;

    protected SpoofaxTargetType(@NotNull final String typeId, @NotNull final IJpsProjectService projectService) {
        super(typeId);
        this.projectService = projectService;
    }

    /**
     * Creates a build target for the specified project.
     *
     * @param project The project.
     * @return The created build target.
     */
    @NotNull
    public abstract T createTarget(@NotNull SpoofaxJpsProject project);

    /**
     * Creates a build target for the specified JPS module.
     *
     * @param module The JPS module.
     * @return The created build target.
     */
    @NotNull
    public final T createTarget(@NotNull final JpsModule module) {
        SpoofaxJpsProject project = projectService.get(module);
        if (project == null)
            project = projectService.create(module);
        return createTarget(project);
    }

    /**
     * Computes all build targets for the specified JPS model.
     *
     * @param model The JPS model.
     * @return A list of build targets.
     */
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

    /**
     * Creates a build target loader, that creates a build target for a JPS module with a specified ID.
     *
     * @param model The JPS model.
     * @return The build target loader.
     */
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
