package org.metaborg.spoofax.intellij.jps.targets;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepProvider;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.util.Collections;
import java.util.List;

public final class SpoofaxNewPreTargetType extends SpoofaxNewTargetType<SpoofaxNewPreTarget> {

    @Inject
    private SpoofaxNewPreTargetType(@NotNull IJpsProjectService projectService, @NotNull IBuildStepProvider buildStepProvider) {
        super("spoofax-pre-production", projectService, buildStepProvider);
    }

    @NotNull
    public SpoofaxNewPreTarget createTarget(@NotNull SpoofaxJpsProject project) {
        List<IBuildStep> steps = getSteps(project, true);
        return new SpoofaxNewPreTarget(project, steps, this);
    }

}
