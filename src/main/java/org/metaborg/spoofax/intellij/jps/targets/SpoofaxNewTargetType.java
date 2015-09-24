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
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepProvider;
import org.metaborg.spoofax.intellij.jps.builders.IJavaBuildStep;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.util.ArrayList;
import java.util.List;

public abstract class SpoofaxNewTargetType<T extends SpoofaxNewTarget> extends ModuleBasedBuildTargetType<T> {

    private final IJpsProjectService projectService;
    private final IBuildStepProvider buildStepProvider;

    protected SpoofaxNewTargetType(@NotNull String typeId, @NotNull IJpsProjectService projectService, @NotNull IBuildStepProvider buildStepProvider) {
        super(typeId);
        this.projectService = projectService;
        this.buildStepProvider = buildStepProvider;
    }

    /**
     * Gets the build steps before/after the Java build.
     * @param project The project.
     * @param beforeJava <code>true</code> to get the steps before the Java build;
     *                   otherwise, <code>false</code> to get the steps after the Java build.
     * @return The steps, which may be an empty list.
     */
    @NotNull
    protected List<IBuildStep> getSteps(@NotNull IProject project, boolean beforeJava) {
        List<IBuildStep> steps = this.buildStepProvider.getBuildSteps(project);
        List<IBuildStep> stepsBeforeJava = new ArrayList<IBuildStep>();
        List<IBuildStep> stepsAfterJava = new ArrayList<IBuildStep>();
        List<IBuildStep> current = stepsBeforeJava;
        for (IBuildStep step : steps) {
            if (step instanceof IJavaBuildStep)
            {
                current = stepsAfterJava;
                continue;
            }

            current.add(step);
        }

        return beforeJava ? stepsBeforeJava : stepsAfterJava;
    }

    @NotNull
    public abstract T createTarget(@NotNull SpoofaxJpsProject project);

    @NotNull
    public final T createTarget(@NotNull JpsModule module) {
        SpoofaxJpsProject project = projectService.get(module);
        if (project == null)
            project = projectService.create(module);
        return createTarget(project);
    }

    @NotNull
    @Override
    public final List<T> computeAllTargets(@NotNull JpsModel model) {
        // Default implementation.
        List<T> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
            targets.add(createTarget(module));
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
