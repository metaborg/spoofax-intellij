package org.metaborg.spoofax.intellij.jps.targets;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.util.Collections;
import java.util.List;

public final class SpoofaxNewPreTargetType extends SpoofaxNewTargetType<SpoofaxNewPreTarget> {

    //public static final SpoofaxNewPreTargetType INSTANCE = new SpoofaxNewPreTargetType();   // TODO: Refactor

    @Inject
    private SpoofaxNewPreTargetType(IJpsProjectService projectService) {
        super("spoofax-pre-production", projectService);
    }

    @NotNull
    public SpoofaxNewPreTarget createTarget(@NotNull SpoofaxJpsProject project) {
        List<IBuildStep> steps = Collections.emptyList(); // TODO
        return new SpoofaxNewPreTarget(project, steps, this);
    }

}
