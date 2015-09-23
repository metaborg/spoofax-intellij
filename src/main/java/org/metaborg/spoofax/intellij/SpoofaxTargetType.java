package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsTypedModule;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepDescriptor;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepProvider;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxProject;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base class for Spoofax build target types.
 */
public abstract class SpoofaxTargetType<T extends SpoofaxTarget> extends ModuleBasedBuildTargetType<T> {

    protected final IIntelliJResourceService resourceService;
    protected final IBuildStepProvider buildStepProvider;

    protected SpoofaxTargetType(String typeId, IBuildStepProvider buildStepProvider, IIntelliJResourceService resourceService) {
        super(typeId);
        this.buildStepProvider = buildStepProvider;
        this.resourceService = resourceService;
    }

    @NotNull
    public abstract T createTarget(IProject project, JpsTypedModule<JpsDummyElement> module);

    @NotNull
    @Override
    public final List<T> computeAllTargets(@NotNull JpsModel model) {
        FileObject location = resourceService.resolve("file:///home/daniel/repos/spoofax-test-project");
        IProject project = new SpoofaxProject(location);    // TODO: Get the project
        List<IBuildStep> steps = buildStepProvider.getBuildSteps(project);

        List<T> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
            targets.add(createTarget(project, module));
        }
        return targets;
    }

    @NotNull
    @Override
    public final BuildTargetLoader<T> createLoader(@NotNull JpsModel model) {
        return new BuildTargetLoader<T>() {
            @Nullable
            @Override
            public T createTarget(@NotNull String targetId) {
                for (T target : computeAllTargets(model)) {
                    if (target.getModule().getName().equals(targetId))
                        return target;
                }
                return null;
            }
        };
    }
}
